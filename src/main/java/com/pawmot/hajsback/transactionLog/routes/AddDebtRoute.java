package com.pawmot.hajsback.transactionLog.routes;

import com.auth0.jwt.JWTVerifier;
import com.google.gson.JsonSyntaxException;
import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pawmot.hajsback.internal.api.transactions.QueueNames.ADD_DEBT_QUEUE;
import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
@Slf4j
public class AddDebtRoute extends SpringRouteBuilder {
    private final TransactionService transactionService;
    @Value("${security.secret}")
    private String secret;

    @Autowired
    public AddDebtRoute(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void configure() throws Exception {
        onException(JsonSyntaxException.class)
                .handled(true)
                .process(ex -> {
                    ex.getIn().setBody(Result.builder().resultKind(ResultKind.ValidationError).build());
                    log.info("Exception");
                })
                .marshal().json(Gson);

        from("jms:queue:" + ADD_DEBT_QUEUE + "?jmsMessageType=Text")
                .routeId("add_debt")
                .process(ex -> {
                    String jwt = ex.getIn().getHeader("JWT", String.class);

                    final String issuer = "com.pawmot.hajsback.api";
                    final String audience = "com.pawmot.hajsback";

                    JWTVerifier verifier = new JWTVerifier(secret, audience, issuer);
                    Map<String, Object> claims = verifier.verify(jwt);

                    ex.setProperty("userEmail", claims.get("com.pawmot.hajsback.user.email"));
                })
                .unmarshal().json(Gson, AddDebtRequest.class)
                .log("body")
                .bean(transactionService)
                .marshal().json(Gson);
    }
}

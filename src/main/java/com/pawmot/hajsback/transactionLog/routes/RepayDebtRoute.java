package com.pawmot.hajsback.transactionLog.routes;

import com.auth0.jwt.JWTVerifier;
import com.google.gson.JsonSyntaxException;
import com.pawmot.hajsback.common.JmsEndpointFactory;
import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import com.pawmot.hajsback.transactionLog.dto.transactions.RepayDebtRequest;
import com.pawmot.hajsback.transactionLog.services.TransactionService;
import org.apache.camel.component.jms.JmsMessageType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pawmot.hajsback.internal.api.transactions.QueueNames.REPAY_DEBT_QUEUE;
import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
public class RepayDebtRoute extends SpringRouteBuilder {
    private final TransactionService transactionService;
    private final JmsEndpointFactory jmsEndpointFactory;

    @Value("${security.secret}")
    private String secret;

    @Autowired
    public RepayDebtRoute(TransactionService transactionService, JmsEndpointFactory jmsEndpointFactory) {
        this.transactionService = transactionService;
        this.jmsEndpointFactory = jmsEndpointFactory;
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

        from(jmsEndpointFactory.createListeningEndpoint(REPAY_DEBT_QUEUE, JmsMessageType.Text))
                .routeId("repay_debt")
                .process(ex -> {
                    String jwt = ex.getIn().getHeader("JWT", String.class);

                    final String issuer = "com.pawmot.hajsback.api";
                    final String audience = "com.pawmot.hajsback";

                    JWTVerifier verifier = new JWTVerifier(secret, audience, issuer);
                    Map<String, Object> claims = verifier.verify(jwt);

                    ex.setProperty("userEmail", claims.get("com.pawmot.hajsback.user.email"));
                })
                .unmarshal().json(Gson, RepayDebtRequest.class)
                .log("body")
                .bean(transactionService, "repayDebt")
                .wireTap("seda:debt-graph")
                .marshal().json(Gson);
    }
}

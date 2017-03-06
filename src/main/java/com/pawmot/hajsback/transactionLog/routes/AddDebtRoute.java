package com.pawmot.hajsback.transactionLog.routes;

import com.auth0.jwt.JWTVerifier;
import com.google.gson.JsonSyntaxException;
import com.pawmot.hajsback.transactionLog.dto.Result;
import com.pawmot.hajsback.transactionLog.dto.ResultKind;
import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.jms.JmsEndpointFactory;
import com.pawmot.hajsback.transactionLog.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.jms.JmsMessageType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
@Slf4j
public class AddDebtRoute extends SpringRouteBuilder {
    private final TransactionService transactionService;
    private final JmsEndpointFactory jmsEndpointFactory;
    private String addDebtQueueName;

    @Value("${security.secret}")
    private String secret;

    @Autowired
    public AddDebtRoute(TransactionService transactionService,
                        JmsEndpointFactory jmsEndpointFactory,
                        @Value("${queues.addDebt}") String addDebtQueueName) {
        this.transactionService = transactionService;
        this.jmsEndpointFactory = jmsEndpointFactory;
        this.addDebtQueueName = addDebtQueueName;
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

        from(jmsEndpointFactory.createListeningEndpoint(addDebtQueueName, JmsMessageType.Text))
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
                .bean(transactionService, "addDebt")
                .wireTap("seda:debt-graph")
                .marshal().json(Gson);
    }
}

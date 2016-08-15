package com.pawmot.hajsback.transactionLog.routes;

import com.auth0.jwt.JWTVerifier;
import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pawmot.hajsback.internal.api.transactions.QueueNames.ADD_DEBT_QUEUE;

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
        from("jms:queue:" + ADD_DEBT_QUEUE)
                .routeId("add_debt")
                .process(ex -> {
                    String jwt = ex.getIn().getHeader("JWT", String.class);

                    final String issuer = "com.pawmot.hajsback.api";
                    final String audience = "com.pawmot.hajsback";

                    JWTVerifier verifier = new JWTVerifier(secret, audience, issuer);
                    Map<String, Object> claims = verifier.verify(jwt);

                    AddDebtRequest request = ex.getIn().getBody(AddDebtRequest.class);
                    log.info("Received request: {}", request);
                })
                .bean(transactionService);
    }
}

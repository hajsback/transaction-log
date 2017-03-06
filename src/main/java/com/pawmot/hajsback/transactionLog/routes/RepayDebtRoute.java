package com.pawmot.hajsback.transactionLog.routes;

import com.google.gson.JsonSyntaxException;
import com.pawmot.hajsback.transactionLog.dto.Result;
import com.pawmot.hajsback.transactionLog.dto.ResultKind;
import com.pawmot.hajsback.transactionLog.dto.transactions.RepayDebtRequest;
import com.pawmot.hajsback.transactionLog.jms.JmsEndpointFactory;
import com.pawmot.hajsback.transactionLog.routes.security.JWTProcessorFactory;
import com.pawmot.hajsback.transactionLog.services.TransactionService;
import org.apache.camel.component.jms.JmsMessageType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
public class RepayDebtRoute extends SpringRouteBuilder {
    private final TransactionService transactionService;
    private final JmsEndpointFactory jmsEndpointFactory;
    private final JWTProcessorFactory jwtProcessorFactory;
    private final String secret;
    private final String repayDebtQueueName;

    @Autowired
    public RepayDebtRoute(TransactionService transactionService,
                          JmsEndpointFactory jmsEndpointFactory,
                          JWTProcessorFactory jwtProcessorFactory,
                          @Value("${security.secret}") String secret,
                          @Value("${queues.repayDebt}") String repayDebtQueueName) {
        this.transactionService = transactionService;
        this.jmsEndpointFactory = jmsEndpointFactory;
        this.jwtProcessorFactory = jwtProcessorFactory;
        this.secret = secret;
        this.repayDebtQueueName = repayDebtQueueName;
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

        from(jmsEndpointFactory.createListeningEndpoint(repayDebtQueueName, JmsMessageType.Text))
                .routeId("repay_debt")
                .process(jwtProcessorFactory.create(secret))
                .unmarshal().json(Gson, RepayDebtRequest.class)
                .log("body")
                .bean(transactionService, "repayDebt")
                .wireTap("seda:debt-graph")
                .marshal().json(Gson);
    }
}

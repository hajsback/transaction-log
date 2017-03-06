package com.pawmot.hajsback.transactionLog.routes;

import com.google.gson.JsonSyntaxException;
import com.pawmot.hajsback.transactionLog.dto.Result;
import com.pawmot.hajsback.transactionLog.dto.ResultKind;
import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.jms.JmsEndpointFactory;
import com.pawmot.hajsback.transactionLog.routes.security.JWTProcessorFactory;
import com.pawmot.hajsback.transactionLog.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.jms.JmsMessageType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
@Slf4j
public class AddDebtRoute extends SpringRouteBuilder {
    private final TransactionService transactionService;
    private final JmsEndpointFactory jmsEndpointFactory;
    private final JWTProcessorFactory jwtProcessorFactory;
    private final String secret;
    private final String addDebtQueueName;

    @Autowired
    public AddDebtRoute(TransactionService transactionService,
                        JmsEndpointFactory jmsEndpointFactory,
                        JWTProcessorFactory jwtProcessorFactory,
                        @Value("${security.secret}") String secret,
                        @Value("${queues.addDebt}") String addDebtQueueName) {
        this.transactionService = transactionService;
        this.jmsEndpointFactory = jmsEndpointFactory;
        this.jwtProcessorFactory = jwtProcessorFactory;
        this.secret = secret;
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
                .process(jwtProcessorFactory.create(secret))
                .unmarshal().json(Gson, AddDebtRequest.class)
                .log("body")
                .bean(transactionService, "addDebt")
                .wireTap("seda:debt-graph")
                .marshal().json(Gson);
    }
}

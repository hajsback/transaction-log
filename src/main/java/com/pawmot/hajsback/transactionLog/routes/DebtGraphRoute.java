package com.pawmot.hajsback.transactionLog.routes;

import com.pawmot.hajsback.transactionLog.dto.Result;
import com.pawmot.hajsback.transactionLog.dto.ResultKind;
import com.pawmot.hajsback.transactionLog.jms.JmsEndpointFactory;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.apache.camel.ExchangePattern.InOnly;
import static org.apache.camel.component.jms.JmsMessageType.Text;
import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
public class DebtGraphRoute extends SpringRouteBuilder {

    private final JmsEndpointFactory jmsEndpointFactory;
    private String debtGraphQueueName;

    public DebtGraphRoute(JmsEndpointFactory jmsEndpointFactory, @Value("${queues.debtGraph}") String debtGraphQueueName) {
        this.jmsEndpointFactory = jmsEndpointFactory;
        this.debtGraphQueueName = debtGraphQueueName;
    }

    @Override
    public void configure() throws Exception {
        from("seda:debt-graph")
                .filter(ex -> {
                    Result body = ex.getIn().getBody(Result.class);
                    return body.getResultKind() == ResultKind.OK;
                })
                .process(ex -> {
                    Result body = ex.getIn().getBody(Result.class);
                    ex.getIn().setBody(body.getData());
                })
                .marshal().json(Gson)
                .to(InOnly, jmsEndpointFactory.createMessageEndpoint(debtGraphQueueName, Text))
                .end();
    }
}

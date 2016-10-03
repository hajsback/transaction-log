package com.pawmot.hajsback.transactionLog.routes;

import com.pawmot.hajsback.common.JmsEndpointFactory;
import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import static com.pawmot.hajsback.internal.api.transactions.QueueNames.DEBT_GRAPH_QUEUE;
import static org.apache.camel.ExchangePattern.InOnly;
import static org.apache.camel.component.jms.JmsMessageType.Text;
import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

@Component
public class DebtGraphRoute extends SpringRouteBuilder {
    private final JmsEndpointFactory jmsEndpointFactory;

    public DebtGraphRoute(JmsEndpointFactory jmsEndpointFactory) {
        this.jmsEndpointFactory = jmsEndpointFactory;
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
                .to(InOnly, jmsEndpointFactory.createMessageEndpoint(DEBT_GRAPH_QUEUE, Text))
                .end();
    }
}

package com.pawmot.hajsback.transactionLog.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TestRoute extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jms:queue:transations")
                .process(ex -> {
                    System.out.println(ex.getIn().getBody());
                    ex.getIn().setBody("REPLY");
                });
    }
}

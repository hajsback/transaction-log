package com.pawmot.hajsback.transactionLog.jms;

import org.apache.camel.component.jms.JmsMessageType;
import org.springframework.stereotype.Component;

@Component
public class JmsEndpointFactory {

    public String createRequestResponseEndpoint(String queueName, JmsMessageType messageType) {
        return String.format("jms:queue:%1$s?replyTo=%1$s.response&replyToType=Exclusive&jmsMessageType=%2$s", queueName, messageType.toString());
    }

    public String createListeningEndpoint(String queueName, JmsMessageType messageType) {
        return String.format("jms:queue:%1$s?jmsMessageType=%2$s", queueName, messageType.toString());
    }

    public String createMessageEndpoint(String queueName, JmsMessageType messageType) {
        return String.format("jms:queue:%1$s?jmsMessageType=%2$s", queueName, messageType.toString());
    }
}

package com.pawmot.hajsback.transactionLog.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
public class JmsConfig {
    @Value("${activemq.host}")
    private String activeMQHost;

    @Value("${activemq.port:61616}")
    private String activeMQPort;

    @Value("${activemq.user}")
    private String activeMQUser;

    @Value("${activemq.pass}")
    private String activeMQPassword;

    @Bean
    ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(
                activeMQUser,
                activeMQPassword,
                String.format("tcp://%1$s:%2$s", activeMQHost, activeMQPort)
        );
    }

    @Bean
    JmsComponent jmsComponent(ConnectionFactory connFactory) {
        return JmsComponent.jmsComponentAutoAcknowledge(connFactory);
    }
}

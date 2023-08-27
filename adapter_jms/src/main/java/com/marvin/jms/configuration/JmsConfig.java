package com.marvin.jms.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@EnableJms
public class JmsConfig {

    private final String username;
    private final String password;

    public JmsConfig(
            @Value("${jms.username}") String username,
            @Value("${jms.password}") String password
    ) {
        this.username = username;
        this.password = password;
    }

    @Bean
    public Context namingContext(
            @Value("${jms.broker-url}") String brokerUrl,
            @Value("${jms.context-factory}") String contextFactory
    ) throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        env.put(Context.PROVIDER_URL, brokerUrl);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);

        return new InitialContext(env);
    }

    @Bean
    public ConnectionFactory connectionFactory(
            Context namingContext,
            @Value("${jms.connection-factory}") String factoryName
    ) throws NamingException {

        ActiveMQJMSConnectionFactory connectionFactory = (ActiveMQJMSConnectionFactory) namingContext.lookup(factoryName);
        connectionFactory.setEnable1xPrefixes(false);

        UserCredentialsConnectionFactoryAdapter credentialAdapter = new UserCredentialsConnectionFactoryAdapter();
        credentialAdapter.setTargetConnectionFactory(connectionFactory);
        credentialAdapter.setUsername(username);
        credentialAdapter.setPassword(password);

        return credentialAdapter;
    }

    @Bean
    public JmsListenerContainerFactory<?> listenerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setEncoding(StandardCharsets.UTF_8.name());
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }
}

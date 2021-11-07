package com.learningnotes.integration.managebatchfile;

import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptAllFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;
import org.springframework.integration.sftp.inbound.SftpStreamingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.integration.transformer.StreamTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@SpringBootApplication
public class ManagebatchfileApplication
{

    public static void main(final String[] args)
    {
        SpringApplication.run(ManagebatchfileApplication.class, args);
    }

    @Bean
    public SessionFactory<LsEntry> sftpSessionFactory()
    {
        final DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost("localhost");
        factory.setPort(22);
        factory.setUser("sftpuser");
        factory.setPassword("sftpuser");
        factory.setAllowUnknownKeys(true);
        // factory.setTestSession(true);
        return new CachingSessionFactory<LsEntry>(factory);
    }

    @Bean
    @InboundChannelAdapter(channel = "stream", poller = @Poller(fixedDelay = "5000"))
    public MessageSource<InputStream> ftpMessageSource()
    {
        final SftpStreamingMessageSource messageSource = new SftpStreamingMessageSource(template());
        messageSource.setRemoteDirectory("sftpdir");
        messageSource.setFilter(new AcceptAllFileListFilter<>());
        messageSource.setMaxFetchSize(1);
        return messageSource;
    }

    @Bean
    @Transformer(inputChannel = "stream", outputChannel = "data")
    public org.springframework.integration.transformer.Transformer transformer()
    {
        return new StreamTransformer("UTF-8");
    }

    @Bean
    @Transformer(inputChannel = "data", outputChannel = "dataString")
    public org.springframework.integration.transformer.Transformer transformerString()
    {
        return new ObjectToStringTransformer("UTF-8");
    }

    @Bean
    public SftpRemoteFileTemplate template()
    {
        return new SftpRemoteFileTemplate(sftpSessionFactory());
    }

    @ServiceActivator(inputChannel = "dataString", adviceChain = "after")
    @Bean
    public MessageHandler handle()
    {
        return message -> {
            if (message.getPayload() instanceof String) {
                System.out.println("String payload");
            }
            System.out.println(message.getPayload());
        };
    }

    public void messageHandler(final Message<String> messageobj)
    {

        System.out.println(messageobj);
    }

    @Bean
    public ExpressionEvaluatingRequestHandlerAdvice after()
    {
        final ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
        advice.setOnSuccessExpressionString(
                "@template.remove(headers['file_remoteDirectory'] + headers['file_remoteFile'])");
        advice.setPropagateEvaluationFailures(true);
        return advice;
    }

}

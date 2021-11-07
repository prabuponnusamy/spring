package com.learningnotes.integration.sftp;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@SpringBootApplication
public class SftpApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(SftpApplication.class, args);
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
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer()
    {
        final SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory());
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory("sftpdir");
        fileSynchronizer.setFilter(new SftpSimplePatternFileListFilter("*.txt"));
        return fileSynchronizer;
    }

    @Bean
    @InboundChannelAdapter(channel = "sftpChannel", poller = @Poller(fixedDelay = "5000"))
    public MessageSource<File> sftpMessageSource()
    {
        final SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(
                sftpInboundFileSynchronizer());
        source.setLocalDirectory(new File("/home/prabu/D/ftp/sftp-inbound"));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
        source.setMaxFetchSize(1);
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler handler()
    {
        return message -> System.out.println(message.getPayload());
    }
}

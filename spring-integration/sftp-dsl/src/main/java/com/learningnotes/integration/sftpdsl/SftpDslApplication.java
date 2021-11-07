package com.learningnotes.integration.sftpdsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.dsl.Sftp;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@SpringBootApplication
public class SftpDslApplication
{

    public static void main(final String[] args)
    {
        SpringApplication.run(SftpDslApplication.class, args);
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
    /**
     * Copy files to the local directory.
     *
     * @return
     */
    public IntegrationFlow sftpInbountFlow()
    {
        final SftpRemoteFileTemplate sfrt = new SftpRemoteFileTemplate(this.sftpSessionFactory());

        return IntegrationFlows
                .from(Sftp.inboundAdapter(this.sftpSessionFactory())
                        .preserveTimestamp(true)
                        .remoteDirectory("sftpdir")
                        .regexFilter(".*\\.txt$"),

                        e -> e.id("sftpInbountAdapter").autoStartup(true)
                                .poller(Pollers.fixedDelay(5000)))
                .handle(message -> System.out.println(message.getPayload()))
                .get();
    }

}

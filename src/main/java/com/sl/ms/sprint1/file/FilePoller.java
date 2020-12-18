package com.sl.ms.sprint1.file;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * This class keeps polling for *.csv in the given location and triggers stock summary event
 * @author MANI
 *
 */
@Configuration
@EnableIntegration
public class FilePoller {

	public static final String POLLING_LOCATION = "C:/Files/input";
	//public static final String POLLING_LOCATION = "classpath:source/";
	public static final String FILE_PATTERN = "*.csv";
	public static final String ARCHIVE_LOCATION = "C:/Files/archive";

	@Bean
	public MessageChannel fileChannel() {
		return new DirectChannel();
	}
	
	@Bean
	@InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> fileReadingMessageSource() throws FileNotFoundException {
		File directory = new File(POLLING_LOCATION);
		//File directory = ResourceUtils.getFile(POLLING_LOCATION);
		SimplePatternFileListFilter filter = new SimplePatternFileListFilter(FILE_PATTERN);
		FileReadingMessageSource msgSource=new FileReadingMessageSource();
		msgSource.setAutoCreateDirectory(false);
		msgSource.setDirectory(directory);
		msgSource.setFilter(filter);
		return msgSource;
	}
	@Bean
	@ServiceActivator(inputChannel = "fileChannel")
	public MessageHandler messageHandler() {
		return new FileHandler(ARCHIVE_LOCATION);
	}
}
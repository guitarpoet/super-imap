package info.thinkingcloud.superimap.components;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import info.thinkingcloud.superimap.handlers.ErrorHandler;

@Component
public class Console implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	@Autowired
	private Shell shell;

	@Autowired
	private ErrorHandler errorHandler;

	private LineReader getReader() {
		return LineReaderBuilder.builder().terminal(shell).build();
	}

	private void loop() {
		LineReader reader = getReader();
		while (true) {
			try {
				String value = reader.readLine("imap> ");
				if (value.equals("exit")) {
					break;
				}
				logger.info(value);
			} catch (UserInterruptException uie) {
				// User wants to quit for pressing ctrl-c
				break;
			} catch (Exception e) {
				errorHandler.handle(e);
			}
		}
		logger.info("Bye");
	}

	@Override
	public void run(String... args) throws Exception {
		loop();
	}
}
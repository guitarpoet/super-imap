package info.thinkingcloud.superimap.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ErrorHandler {
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

	public void handle(Throwable e) {
		logger.error(e.getMessage(), e);
	}
}

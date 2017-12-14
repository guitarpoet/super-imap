package info.thinkingcloud.superimap.components;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ServiceLoader;
import java.util.function.IntConsumer;

import org.jline.terminal.Attributes;
import org.jline.terminal.Cursor;
import org.jline.terminal.Size;
import org.jline.terminal.impl.AbstractTerminal;
import org.jline.terminal.impl.CursorSupport;
import org.jline.terminal.spi.JnaSupport;
import org.jline.terminal.spi.Pty;
import org.jline.utils.NonBlockingReader;
import org.jline.utils.Signals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import info.thinkingcloud.superimap.handlers.ErrorHandler;

@Component
@ConfigurationProperties(prefix = "shell")
public class Shell extends AbstractTerminal {

	private static final Logger logger = LoggerFactory.getLogger(Shell.class);
	private static PrintWriter writer;
	private static NonBlockingReader reader;
	private static Pty pty;
	@Autowired
	private ErrorHandler errorHandler;

	public Shell() throws IOException {
		this("super-imap-terminal", System.getenv("TERM"), Charset.forName("UTF-8"), null);
	}

	public Shell(String name, String type, Charset encoding, SignalHandler signalHandler) throws IOException {
		super(name, type, encoding, signalHandler);
		pty = ServiceLoader.load(JnaSupport.class).iterator().next().current();
		logger.debug("Initializing the shell using name {} type {} and encoding {} for pty {}", name, type, encoding,
				pty);
		parseInfoCmp();
	}

	@Override
	public NonBlockingReader reader() {
		if (reader == null) {
			try {
				reader = new NonBlockingReader("super-imap", new InputStreamReader(pty.getSlaveInput()));
			} catch (IOException e) {
				errorHandler.handle(e);
			}
		}
		return reader;
	}

	@Override
	public PrintWriter writer() {
		if (writer == null) {
			try {
				writer = new PrintWriter(pty.getSlaveOutput());
			} catch (IOException e) {
				errorHandler.handle(e);
			}
		}
		return writer;
	}

	@Override
	public InputStream input() {
		try {
			return pty.getSlaveInput();
		} catch (IOException e) {
			errorHandler.handle(e);
		}
		return System.in;
	}

	@Override
	public OutputStream output() {
		try {
			return pty.getSlaveOutput();
		} catch (IOException e) {
			errorHandler.handle(e);
		}
		return System.out;
	}

	@Override
	public Attributes getAttributes() {
		try {
			return pty.getAttr();
		} catch (IOException e) {
			errorHandler.handle(e);
		}
		return null;
	}

	@Override
	public void setAttributes(Attributes attr) {
		try {
			pty.setAttr(attr);
		} catch (IOException e) {
			errorHandler.handle(e);
		}
	}

	@Override
	public Size getSize() {
		try {
			return pty.getSize();
		} catch (IOException e) {
			errorHandler.handle(e);
		}
		return null;
	}

	@Override
	public void setSize(Size size) {
		try {
			pty.setSize(size);
		} catch (IOException e) {
			errorHandler.handle(e);
		}
	}

	@Override
	public void close() throws IOException {
		pty.close();
	}

	@Override
	public Cursor getCursorPosition(IntConsumer discarded) {
		return CursorSupport.getCursorPosition(this, discarded);
	}

	@Override
	public SignalHandler handle(Signal signal, SignalHandler handler) {
		SignalHandler prev = super.handle(signal, handler);
		if (prev != handler) {
			if (handler == SignalHandler.SIG_DFL) {
				Signals.registerDefault(signal.name());
			} else {
				Signals.register(signal.name(), () -> raise(signal));
			}
		}
		return prev;
	}
}
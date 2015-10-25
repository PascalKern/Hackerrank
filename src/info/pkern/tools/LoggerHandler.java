package info.pkern.tools;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerHandler {

//	private static final String LOG_FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] (%2$s) %5$s %6$s%n";
	private static final String LOG_FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s (%2$s) %5$s %6$s%n";
	
	public static void enableSingleLineFineConsoleLogging() {
		
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);

		LogManager.getLoggingMXBean().setLoggerLevel("", Level.FINE.toString());
		
		setAllLogHanderLevels(Level.FINE);
	}

	private static void setAllLogHanderLevels(Level level) {
		for (Handler handler : Logger.getLogger("").getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				handler.setLevel(level);
			}
		}
	}

	public static void disableLogging() {
		setAllLogHanderLevels(Level.OFF);
	}
	
}

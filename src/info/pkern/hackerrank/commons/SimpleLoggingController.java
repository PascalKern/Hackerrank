package info.pkern.hackerrank.commons;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SimpleLoggingController {

	private static final String LOG_FORMAT = "%1$tF %1$tT,%1$tL %4$-7s [%3$s] %2$s - %5$s%6$s%n";
//	private static final String LOG_FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s (%2$s) %5$s %6$s%n";
	
	public static void enableSingleLineFineConsoleLogging() {
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		LogManager.getLoggingMXBean().setLoggerLevel("", Level.FINE.toString());
		setAllLogHanderLevels(Level.FINE);
	}

	public static void enableSingleLineFineConsoleLoggingExcludePackages(List<String> packagesFQN) {
		enableSingleLineFineConsoleLogging();
		for (String packageFQN : packagesFQN) {
			disablePackage(packageFQN);
		}
	}
	
	public static void disableLogging() {
		setAllLogHanderLevels(Level.OFF);
	}

	private static void setAllLogHanderLevels(Level level) {
		for (Handler handler : Logger.getLogger("").getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				handler.setLevel(level);
			}
		}
	}

	private static void disablePackage(String packageFQN) {
		Logger logger = Logger.getLogger(packageFQN);
		logger.setUseParentHandlers(false);
		//Just to secure no handler is assigned and or can log messages!
		for (Handler handler : logger.getHandlers()) {
			handler.setLevel(Level.OFF);
		}
	}
	
}

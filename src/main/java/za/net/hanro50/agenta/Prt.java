package za.net.hanro50.agenta;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Prt {
    public static enum LEVEL {
        FETAL,
        ERROR,
        INFO
    }

    public static Log systemLogger;
    static {
        try {
            systemLogger = new log4jImp();
        } catch (Throwable w) {
            systemLogger = new sysImp();
        }
    }

    static class log4jImp implements Log {
        Logger logger1;

        public log4jImp() {
            logger1 = LogManager.getLogger("Agenta");

        }

        @Override
        public void log(LEVEL level, String string2, Object... args) {
            string2 = String.format(string2, args);
            switch (level) {
                case ERROR:
                    logger1.warn(string2);
                    break;
                case FETAL:
                    logger1.log(Level.FATAL, string2);
                    break;
                default:
                case INFO:
                    logger1.info(string2);
                    break;
            }
        }
    }

    public static class sysImp implements Log {
        String RED = "\033[1;33m";
        String CLR = "\033[0m";

        public sysImp() {
            if (System.getProperty("agenta.console.colour", "true").equals("false")) {
                RED = "";
                CLR = "";
            }
        }

        @Override
        public void log(LEVEL level, String string2, Object... args) {
            string2 = String.format(string2, args);
            switch (level) {
                case ERROR:
                    System.err.println(RED + string2 + CLR);
                    break;
                case FETAL:
                    System.err.println(RED + string2 + CLR);
                    break;
                default:
                case INFO:
                    System.out.println(CLR + string2);
                    break;
            }

        }

    }

    static public interface Log {
        void log(LEVEL level, String string2, Object... args);
    }

    public static void info(String info, Object... args) {
        log(LEVEL.INFO, info, args);
    }

    public static void log(LEVEL level, String messsage, Object... args) {
        systemLogger.log(level, messsage, args);
    }

    public static void warn(String wrn, Object... args) {
        log(LEVEL.ERROR, wrn, args);
    }

}

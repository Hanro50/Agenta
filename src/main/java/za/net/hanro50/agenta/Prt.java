package za.net.hanro50.agenta;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Prt {
    public final static boolean DEBUG = Config.get("agenta.prt.debug").equals("true");

    public static enum LEVEL {
        FETAL,
        ERROR,
        INFO,
        DEBUG
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
        public void log(LEVEL level, String string2) {

            switch (level) {
                case ERROR:
                    logger1.warn(string2);
                    break;
                case FETAL:
                    logger1.log(Level.FATAL, string2);
                    break;
                case DEBUG:
                    logger1.log(Level.DEBUG, string2);
                    break;
                case INFO:
                default:
                    logger1.info(string2);
                    break;
            }
        }
    }

    public static class sysImp implements Log {
        String RED = "\033[1;33m";
        String CLR = "\033[0m";

        public sysImp() {
            if (Config.get("agenta.prt.color").equals("false")
                    || System.console() == null
                    || System.getenv().get("TERM") == null) {
                RED = "";
                CLR = "";
            }
        }

        @Override
        public void log(LEVEL level, String string2) {
            switch (level) {
                case ERROR:
                    System.err.println(RED + "[Agenta:wrn] " + string2 + CLR);
                    break;
                case FETAL:
                    System.err.println(RED + "[Agenta:err] " + string2 + CLR);
                    break;
                case DEBUG:
                    System.out.println(CLR + "[Agenta:dbg] " + string2);
                    break;
                case INFO:
                default:
                    System.out.println(CLR + "[Agenta:log] " + string2);
                    break;
            }

        }

    }

    static public interface Log {
        void log(LEVEL level, String string2);
    }

    // string2 = String.format(string2, args);
    public static void log(LEVEL level, String messsage, Object... args) {
        if (args.length > 0) {
            try {
                systemLogger.log(level, String.format(messsage, args));
                return;
            } catch (java.util.MissingFormatArgumentException e) {
                systemLogger.log(LEVEL.ERROR, "Missing:" + e.getMessage());
            } catch (java.util.UnknownFormatConversionException e) {
                systemLogger.log(LEVEL.ERROR, "Conversion error:" + e.getMessage());
            }
        }
        systemLogger.log(level, messsage);
    }

    public static void info(String info, Object... args) {
        log(LEVEL.INFO, info, args);
    }

    public static void warn(String wrn, Object... args) {
        log(LEVEL.ERROR, wrn, args);
    }

    public static void debug(String wrn, Object... args) {
        if (DEBUG)
            log(LEVEL.DEBUG, wrn, args);
    }

}

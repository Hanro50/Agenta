package za.net.hanro50.mod;

import nilloader.api.NilLogger;
import nilloader.api.NilMetadata;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.Prt.LEVEL;
import za.net.hanro50.agenta.Prt.Log;

public class AgentaNil implements Runnable {
  public static final NilLogger logger = NilLogger.get("Agenta");

  private class nilLogger implements Log {

    public nilLogger() {
      Prt.systemLogger = this;
    }

    @Override
    public void log(LEVEL level, String message) {
      switch (level) {
        case ERROR:
          logger.warn(message);
          break;
        case FETAL:
          logger.error(message);
          break;
        case DEBUG:
          logger.debug(message);
        case INFO:
        default:
          logger.info(message);
          break;
      }
    }
  }

  @Override
  public void run() {
    try {
      new nilLogger();
    } catch (Throwable e) {
    }
    Commom.load("NilLoader");
  }

}

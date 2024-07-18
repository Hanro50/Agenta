package za.net.hanro50.mod;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.Prt.LEVEL;
import za.net.hanro50.agenta.Prt.Log;

public class FmlPrt implements Log {
    private Logger logger;

    public FmlPrt() {
        logger = LogManager.getLogManager().getLogger("Agenta");

        if (logger == null) {
            try {
                Logger l = cpw.mods.fml.common.FMLLog.getLogger();
                logger = Logger.getLogger("Agenta");
                logger.setParent(l);
                logger.setUseParentHandlers(true);
            } catch (Throwable e) {
            }
        }
        if (logger != null)
            Prt.systemLogger = this;
    }

    @Override
    public void log(LEVEL level, String string2) {
        if (string2 == null)
            string2 = "NULL";

        switch (level) {
            case ERROR:
                logger.warning(string2);
                break;
            case FETAL:
                logger.log(Level.SEVERE, string2);
                break;
            case DEBUG:
            case INFO:
            default:
                logger.info(string2);
                break;
        }
    }
}

package za.net.hanro50.mod;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.CannotProceedException;

import org.bukkit.plugin.java.JavaPlugin;

import za.net.hanro50.agenta.Config;
import za.net.hanro50.agenta.Main;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.Prt.LEVEL;
import za.net.hanro50.agenta.Prt.Log;

public class AgentaPlugin extends JavaPlugin {
    private class bukkitLogger implements Log {
        private Logger logger;

        public bukkitLogger() {
            logger = getLogger();
            if (logger != null) {
                Prt.systemLogger = this;
                Prt.info("Using Bukkit logger");
            }
        }

        @Override
        public void log(LEVEL level, String string2) {
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

    public void onLoad() {
    }

    public void onEnable() {
        try {
            Config.getInstance().load(getDataFolder());
        } catch (Throwable e) {
        }
        try {
            new bukkitLogger();
        } catch (Throwable e) {
        }

        Prt.info("Starting as Bukkit plugin");
        try {
            Main.flight();
        } catch (CannotProceedException e) {
            Prt.log(LEVEL.FETAL, "Cannot manually stop Bukkit server.");
        }
    }

    public void onDisable() {
    }
}

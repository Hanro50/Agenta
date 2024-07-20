package za.net.hanro50.agenta;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import javax.naming.CannotProceedException;

import za.net.hanro50.agenta.Prt.LEVEL;
import za.net.hanro50.agenta.handler.Deligator;

public class Main {
    private static transient boolean init = false;
    public static final String errorStr = "\nClass \"sun.net.www.protocol.http.Handler\" is not accessable!\n" +
            "\nThe following can fix this error\n" +
            "\t1) Try launching with the following JVM paramer \"--add-exports java.base/sun.net.www.protocol.http=ALL-UNNAMED\"\n"
            +
            "\t2) Use java 11\n" +
            "\t3) Report this error so it can be resolved! (If non of the above worked)\n" +
            "\nSupport (discord): https://discord.gg/f7THdzEPH2\n" +
            "Agenta cannot continue. Exiting...\n";

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            NegativeArraySizeException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InstantiationException, CannotProceedException {
        Prt.info("Running in static mode!");
        flight();

        String main = System.getProperty("gmll.main.class", "net.minecraft.client.main.Main");
        Class<?> C = Class.forName(main);
        Method M = C.getMethod("main", new Class[] { Array.newInstance(String.class, 0).getClass() });
        M.invoke(null, new Object[] { args });
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws CannotProceedException {
        Prt.info("Running in java agent mode!");
        flight();
    }

    @SuppressWarnings("restriction")
    public static boolean flight() throws CannotProceedException {
        if (init)
            return true;
        System.setProperty("fml.ignoreInvalidMinecraftCertificates", "true");
        try {
            new sun.net.www.protocol.http.Handler();
        } catch (Throwable e) {
            Prt.log(Prt.LEVEL.FETAL, errorStr);
            throw new CannotProceedException();
        }
        try {
            URL.setURLStreamHandlerFactory(new Deligator());
        } catch (Error err) {
            try {
                new URL("forward://127.0.0.1:80").openConnection();
                Prt.info("Agenta is already loaded. Returning");
                init = true;
                return false;
            } catch (Error | IOException e) {
                Prt.log(LEVEL.FETAL, "AGENTA FAILED TO LOAD!\nURL Stream Handler Factory is already set.");
                e.printStackTrace();
                return false;
            }
        }
        init = true;
        File config = Config.getInstance().getConfigFile();
        if (config == null) {
            Prt.info("Using java arguments for configuration");
        } else {
            Prt.info("Loaded config from: " + config.getAbsolutePath());
        }
        return true;
    }
}

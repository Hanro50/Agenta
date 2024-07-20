package za.net.hanro50.mod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import javax.naming.CannotProceedException;

import za.net.hanro50.agenta.Config;
import za.net.hanro50.agenta.Main;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.Prt.sysImp;

public class Commom {

    static public void load(String src) {
        try {
            Class<?> C = Class.forName("net.minecraft.client.Minecraft");

            Method[] methods = C.getMethods();

            for (Method method : methods) {
                if (method.getParameterTypes().length == 0 && method.getReturnType().equals(File.class)
                        && Modifier.isStatic(method.getModifiers())) {
                    File file = (File) method.invoke(null);
                    load(src, new File(file, "config"));
                    return;
                }
            }
        } catch (ClassNotFoundException e) {

        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (System.getProperty("agenta.onesix.config.fallback", "false").equals("true")) {
            load(src, null);
        }
    }

    static public void load(String src, File config) {
        Config.getInstance().load(config);
        if (Prt.systemLogger instanceof sysImp)
            new FmlPrt();

        Prt.info("Starting as " + src + " mod?!");
        try {
            if (!Main.flight()) {
                try {
                    new URL("prtconfig:fml").openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (CannotProceedException e) {
            try {
                cpw.mods.fml.common.FMLCommonHandler.instance().raiseException(e, Main.errorStr, true);
            } catch (Throwable e2) {
                new Fallback();
            }
        }
    }
}

package za.net.hanro50.mod;

import java.io.IOException;
import java.net.URL;

import javax.naming.CannotProceedException;

import za.net.hanro50.agenta.Main;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.Prt.sysImp;

public class Commom {

    static public void load(String src) {
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

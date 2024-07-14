package za.net.hanro50.agenta.handler.protocols;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.Prt.sysImp;
import za.net.hanro50.mod.FmlPrt;

public class PrtConfig extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        if (u.getPath().equals("fml"))
            new FmlPrt();
        if (u.getPath().equals("system"))
            Prt.systemLogger = new sysImp();
        Prt.info("Updating logger...[" + u.getPath() + "]");
        return null;
    }

}

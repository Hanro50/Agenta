package za.net.hanro50.agenta.handler.deligates;

import java.io.IOException;
import java.net.URL;

import za.net.hanro50.agenta.Config;
import za.net.hanro50.agenta.Prt;

public class FmlFix extends Deligate {
    final String urlFix = Config.get("agenta.assets.fml");
    final String urlOld = "http://files.minecraftforge.net/fmllibs";

    @Override
    public Boolean check(URL url) {
        return url.toString().startsWith(urlOld);
    }

    @Override
    public URL run(URL url) throws IOException {
        String urlStr = url.toString();
        urlStr = (urlStr.startsWith(urlOld)) ? urlStr.substring(urlOld.length()) : "";
        URL t = new URL(urlFix + urlStr);
        Prt.info(urlFix + urlStr);
        return t;
    }

}

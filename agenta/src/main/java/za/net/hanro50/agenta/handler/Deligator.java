package za.net.hanro50.agenta.handler;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.handler.deligates.AuthFix;
import za.net.hanro50.agenta.handler.deligates.Deligate;
import za.net.hanro50.agenta.handler.deligates.FmlFix;
import za.net.hanro50.agenta.handler.deligates.ResourceText;
import za.net.hanro50.agenta.handler.deligates.ResourceXML;
import za.net.hanro50.agenta.handler.deligates.SkinDeligate;
import za.net.hanro50.agenta.handler.protocols.PrtConfig;

public class Deligator extends URLStreamHandler implements URLStreamHandlerFactory {
    private static List<Deligate> Deligates = new ArrayList<>();
    private static Map<String, URLStreamHandler> protocols = new HashMap<>();
    static {
        addDeligate(new SkinDeligate(true, "/MinecraftSkins/"));
        addDeligate(new SkinDeligate(true, "/skin/"));
        addDeligate(new SkinDeligate(false, "/MinecraftCloaks/"));
        addDeligate(new SkinDeligate(false, "/cloak/"));
        addDeligate(new AuthFix());
        addDeligate(new FmlFix());
        addDeligate(new ResourceXML());
        addDeligate(new ResourceText());

        addprotocol("http", new Deligator());
        addprotocol("prtconfig", new PrtConfig());
        addprotocol("forward", new sun.net.www.protocol.http.Handler());
    }

    public static void addDeligate(Deligate handler) {
        Deligates.add(handler);
    }

    public static void addprotocol(String protocol, URLStreamHandler handler) {
        protocols.put(protocol, handler);
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        return protocols.get(protocol);
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return openConnection(u, null);
    }

    @Override
    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        Prt.info("Routing: " + url);
        for (Deligate deligate : Deligates) {
            if (deligate.check(url))
                return deligate.run(url, proxy);
        }
        Prt.info("Deligate miss");
        return Deligate.forward(url, proxy);
    }

}

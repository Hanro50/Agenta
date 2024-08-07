package za.net.hanro50.agenta.handler.deligates;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import za.net.hanro50.agenta.objects.Error404Connection;

public abstract class Deligate {
    public abstract Boolean check(URL url);

    public abstract URL run(URL url) throws IOException;

    public URLConnection run(URL url, Proxy proxy) throws IOException {
        return forward(this.run(url), proxy);
    }

    public static URLConnection forward(URL url, Proxy proxy) throws IOException {
        if (url == null)
            return new Error404Connection(url, proxy != null);

        String protocol = url.getProtocol().equals("http") ? "forward" : url.getProtocol();
        URL urlForward = new URL(protocol, url.getHost(), url.getPort(), url.getFile());

        if (proxy != null)
            return urlForward.openConnection(proxy);
        return urlForward.openConnection();
    }
}

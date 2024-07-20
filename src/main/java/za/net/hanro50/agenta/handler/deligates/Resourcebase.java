package za.net.hanro50.agenta.handler.deligates;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import za.net.hanro50.agenta.Config;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.handler.Fetch;
import za.net.hanro50.agenta.objects.AssetIndex;
import za.net.hanro50.agenta.objects.HTTPException;

public abstract class Resourcebase extends Deligate {
    final String urlFix;

    public Resourcebase(String fixed) {
        urlFix = fixed;
    }

    static final String assetURL = Config.get("agenta.assets.url");
    AssetIndex index;

    AssetIndex getIndex() {
        if (index == null)
            try {
                Prt.info("Getting resource index");
                index = Fetch.<AssetIndex>get(
                        Config.get("agenta.assets.index"),
                        AssetIndex.class);
            } catch (IOException | InterruptedException | HTTPException e) {
                e.printStackTrace();
            }
        return index;
    }

    class resourcesHandler extends URLConnection {
        String urlStr;
        Proxy proxy;

        protected resourcesHandler(URL url, Proxy proxy) {
            super(url);
            urlStr = url.toString();
            urlStr = (urlStr.startsWith(urlFix)) ? urlStr.substring(urlFix.length()) : "";
            urlStr = urlStr.replaceAll("%20", " ");
            this.proxy = proxy;
        }

        @Override
        public void connect() throws IOException {
            // TODO Auto-generated method stub

        }

        @Override
        public InputStream getInputStream() throws IOException {
            return get(urlStr, this.proxy);
        }
    }

    public abstract InputStream get(String urlStr, Proxy proxy) throws IOException;

    public void connect() throws IOException {
    }

    @Override
    public Boolean check(URL url) {
        return url.toString().startsWith(urlFix);
    }

    @Override
    public URL run(URL url) throws IOException {
        throw new UnsupportedOperationException("Unsupported method");
    }

    @Override
    public URLConnection run(URL url, Proxy proxy) throws IOException {
        return new resourcesHandler(url, proxy);
    }
}

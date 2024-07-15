package za.net.hanro50.agenta.objects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ErrorConnection extends HttpURLConnection {
    Boolean proxy;

    public ErrorConnection(URL u, Boolean proxy) {
        super(u);
        this.proxy = proxy;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void disconnect() {
    }

    @Override
    public boolean usingProxy() {
        return proxy;
    }

    @Override
    public void connect() throws IOException {
    }

    @Override
    public int getResponseCode() {
        return 404;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return InputStream.nullInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return OutputStream.nullOutputStream();
    }

}

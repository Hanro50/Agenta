package za.net.hanro50.agenta.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Error404Connection extends HttpURLConnection {
    Boolean proxy;

    public Error404Connection(URL u, Boolean proxy) {
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
        return HttpURLConnection.HTTP_NOT_FOUND;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[] {});
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }

}

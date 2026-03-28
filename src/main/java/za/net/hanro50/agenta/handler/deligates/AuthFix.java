package za.net.hanro50.agenta.handler.deligates;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.google.gson.JsonSyntaxException;
import java.net.Proxy;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.handler.Fetch;
import za.net.hanro50.agenta.objects.HTTPException;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class AuthFix extends Deligate {
    @Override
    public Boolean check(URL url) {
        return url.toString().startsWith("http://www.minecraft.net/game/");
    }

    private static String getRandomHexString() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 10)
            sb.append(Integer.toHexString(r.nextInt()));

        return sb.toString().substring(0, 10);
    }

    final static String serverID = getRandomHexString();

    class resourcesHandler extends URLConnection {
        Boolean authed;

        protected resourcesHandler(URL url, Boolean authed) {
            super(url);
            this.authed = authed;

        }

        @Override
        public void connect() throws IOException {
            // TODO Auto-generated method stub

        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (authed) {
                Prt.info("Authed");
                return new ByteArrayInputStream("0".getBytes(StandardCharsets.UTF_8));
            }
            Prt.info("ERROR");
            return new ByteArrayInputStream("ERROR".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public URLConnection run(URL url, Proxy proxy) throws IOException {
        if (url.toString().contains("joinserver.jsp"))
            return forward(this.run(url), proxy);

        String[] q = url.getQuery().split("&", 2);

        String user = q[0].split("=")[1];
        String token = q[1].split("=")[1];

        try {
            // I don't want to bypass mojang auth. So we do an equivalent modern call to
            // this endpoint.
            InputStream res = Fetch
                    .get("https://session.minecraft.net/game/joinserver.jsp?user=" + user + "&sessionId=" + token
                            + "&serverId=" + serverID);

            try (Scanner s = new Scanner(res).useDelimiter("\\A")) {
                String result = s.hasNext() ? s.next() : "";
                s.close();

                return new resourcesHandler(url, result.equals("OK"));

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Prt.info("FAIL");
        return new resourcesHandler(url, false);
    }

    public URL run(URL url) throws IOException {
        return new URL(url.toString().replace("www", "session"));
    }

}

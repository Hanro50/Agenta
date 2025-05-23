package za.net.hanro50.agenta.handler.deligates;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import za.net.hanro50.agenta.Config;
import za.net.hanro50.agenta.Prt;
import za.net.hanro50.agenta.handler.Fetch;
import za.net.hanro50.agenta.objects.Error404Connection;
import za.net.hanro50.agenta.objects.Player;
import za.net.hanro50.agenta.objects.Player2;
import za.net.hanro50.agenta.objects.Profile;
import za.net.hanro50.agenta.objects.Textures;

public class SkinDeligate extends Deligate {
    private boolean skin;
    private String endpoint;
    private static boolean resize;
    static {
        resize = Config.get("agenta.skin.resize").equals("true");
        if (!resize) {
            Prt.info("Disabling skin resizing!");
        }
    }

    public SkinDeligate(boolean skin, String endpoint) {
        this.skin = skin;
        this.endpoint = endpoint;
    }

    @Override
    public Boolean check(URL url) {
        return url.toString().contains(endpoint);
    }

    @Override
    public URL run(URL u) throws IOException {
        String username = u.toString();

        if (username.indexOf("get.jsp?user=") != -1) {
            username = username.substring(username.lastIndexOf("=") + 1);

        } else {
            username = username.substring(username.lastIndexOf("/") + 1);
            username = username.substring(0, username.length() - 4);
        }
        try {

            Player player = Fetch.get("https://api.mojang.com/users/profiles/minecraft/" + username,
                    Player.class);
            if (player != null) {
                Textures[] textures = Fetch.get(
                        "https://sessionserver.mojang.com/session/minecraft/profile/" + player.id,
                        Profile.class).properties;
                if (textures.length >= 1) {
                    Player2 plr = textures[0].decompile();
                    String text = skin ? plr.getSkin() : plr.getCape();
                    if (text != null)
                        return new URL(text);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (za.net.hanro50.agenta.objects.HTTPException e) {
            Prt.warn("Experienced HTTP error!");
            e.printStackTrace();
        }
        Prt.warn("Failed to get " + (skin ? "skin" : "cape") + " for " + username);
        return null;
    }

    public URLConnection run(final URL url, final Proxy proxy) throws IOException {
        final URL send = this.run(url);
        if (send == null)
            return new Error404Connection(url, proxy != null);
        if (skin && resize) {
            return new HttpURLConnection(url) {
                @Override
                public void connect() throws IOException {
                }

                public InputStream getInputStream() throws IOException {
                    // Fixes issues with handling modern skin formats on old versions
                    // (Thanks OptiFine!)
                    BufferedImage img;
                    img = ImageIO.read(send);

                    if (img.getHeight() >= 64) {
                        img = img.getSubimage(0, 0, 64, 32);
                    }
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(img, "png", os);
                    return new ByteArrayInputStream(os.toByteArray());
                }

                @Override
                public int getResponseCode() {
                    return send == null ? HttpURLConnection.HTTP_NOT_FOUND : HttpURLConnection.HTTP_ACCEPTED;
                }

                @Override
                public void disconnect() {
                }

                @Override
                public boolean usingProxy() {
                    return proxy != null;
                }
            };
        }

        return Deligate.forward(send, proxy);
    }

}

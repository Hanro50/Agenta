package za.net.hanro50.agenta.handler.deligates;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

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
  private Map<String, URLConnection> textureCache = new HashMap<>();
  private boolean skin;
  private String endpoint;
  private static boolean resize;
  private static boolean mergeLayers;
  private static boolean cacheSkins;
  static {
    resize = Config.get("agenta.skin.resize").equals("true");
    mergeLayers = Config.get("agenta.skin.merge").equals("true");
    cacheSkins = Config.get("agenta.skin.cache").equals("true");
    if (!resize) {
      Prt.info("Disabling skin resizing!");
    }
    if (!mergeLayers) {
      Prt.info("Disabling merging skin layers!");
    }
    if (!cacheSkins) {
      Prt.info("Disabling skin caching!");
    }
  }

  public URL run(URL url) throws IOException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'run'");
  }

  public SkinDeligate(boolean skin, String endpoint) {
    this.skin = skin;
    this.endpoint = endpoint;
  }

  @Override
  public Boolean check(URL url) {
    return url.toString().contains(endpoint);
  }

  public URL getURL(String username) throws IOException {
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
    String username = url.toString();

    if (username.indexOf("get.jsp?user=") != -1) {
      username = username.substring(username.lastIndexOf("=") + 1);

    } else {
      username = username.substring(username.lastIndexOf("/") + 1);
      username = username.substring(0, username.length() - 4);
    }
    if (textureCache.containsKey(username) && cacheSkins)
      return textureCache.get(username);

    final URL send = this.getURL(username);
    if (send == null)
      return new Error404Connection(url, proxy != null);

    URLConnection result = new HttpURLConnection(url) {

      @Override
      public void connect() throws IOException {
      }

      BufferedImage cachedImg;

      public InputStream getInputStream() throws IOException {
        if (cachedImg == null) {
          cachedImg = ImageIO.read(send);
          if (cachedImg.getHeight() >= 64) {
            if (mergeLayers) {
              BufferedImage processedImage = new BufferedImage(cachedImg.getWidth(),
                  cachedImg.getHeight(),
                  BufferedImage.TYPE_INT_ARGB);
              Graphics2D g2d = processedImage.createGraphics();
              g2d.drawImage(cachedImg, 0, -16, null);
              g2d.setComposite(AlphaComposite.Clear);
              g2d.fillRect(0, 0, 64, 16);
              g2d.setComposite(AlphaComposite.DstOver);
              g2d.drawImage(cachedImg, 0, 0, null);
              g2d.dispose();
              cachedImg = processedImage;
            }
            if (resize) {
              cachedImg = cachedImg.getSubimage(0, 0, 64, 32);
            }
          }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(cachedImg, "png", os);
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
    Prt.info("MEW4");
    textureCache.put(username, result);
    return result;

    // return Deligate.forward(send, proxy);
  }

}

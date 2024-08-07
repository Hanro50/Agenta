package za.net.hanro50.agenta.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import za.net.hanro50.agenta.objects.HTTPException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Fetch {
  static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

  static SSLContext sslContext;

  static {
    // Thanks OptiFine for somehow breaking https requests
    try {
      sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, null, new SecureRandom());

    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      e.printStackTrace();
    }
  }

  public static <T> T get(String url, Class<T> ClassOfT) throws IOException, InterruptedException, HTTPException {
    InputStream res = get(url);
    try (Scanner s = new Scanner(res).useDelimiter("\\A")) {
      String result = s.hasNext() ? s.next() : "";
      s.close();
      return gson.fromJson(result, ClassOfT);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static InputStream get(String url) throws MalformedURLException, IOException, HTTPException {
    HttpsURLConnection httpURLConnection = (HttpsURLConnection) (new URL(url)).openConnection();
    httpURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
    httpURLConnection.connect();
    if (Math.floor((httpURLConnection.getResponseCode() / 100)) == 3.0D)
      return get(httpURLConnection.getHeaderField("Location"));
    if (Math.floor((httpURLConnection.getResponseCode() / 100)) == 2.0D)
      return httpURLConnection.getInputStream();
    throw new HTTPException(url, httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
  }
}

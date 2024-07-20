package za.net.hanro50.agenta.handler.deligates;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import za.net.hanro50.agenta.Config;
import za.net.hanro50.agenta.Prt;

public class MapFix extends Deligate {
    File savejson = new File(Config.get("agenta.save.file"));
    Gson gson = new GsonBuilder().create();

    static class saveData {
        public String name;
        public byte[] data;

        saveData() {
        }

        saveData(String name, byte[] data) {
            this.data = data;
            this.name = name;
        }
    }

    saveData[] saves = new saveData[5];

    static class Data {
        saveData[] saves = new saveData[5];

        Data() {
        }

        Data(saveData[] saves) {
            this.saves = saves;
        }
    }

    boolean loaded = false;

    void save() {
        String data = gson.toJson(new Data(saves));
        try {
            if (!savejson.exists())
                savejson.createNewFile();
            Files.write(savejson.toPath(), data.getBytes());
            Prt.info("Saving saves to :" + savejson.getAbsolutePath());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void load() {
        if (loaded)
            return;
        loaded = true;
        if (!savejson.exists())
            return;
        Prt.info("Loading saves from :" + savejson.getAbsolutePath());
        try {
            InputStream is = new FileInputStream(savejson);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = buf.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            buf.close();

            String data = sb.toString();

            saveData[] retriedSaves = gson.fromJson(data, Data.class).saves;

            for (int i = 0; i < retriedSaves.length && i < 5; i++) {
                saves[i] = retriedSaves[i];
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    class resourcesHandler extends HttpURLConnection {
        String urlStr;
        Proxy proxy;

        protected resourcesHandler(URL url, Proxy proxy) {
            super(url);
            this.proxy = proxy;
        }

        @Override
        public void connect() throws IOException {
            // TODO Auto-generated method stub

        }

        public OutputStream getOutputStream() throws IOException {

            return new ByteArrayOutputStream() {

                public void close() {
                    final byte[] bytes = this.toByteArray();
                    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bytes));

                    // DataInputStream stream = new DataInputStream(istream);

                    try {
                        String username = stream.readUTF();
                        String token = stream.readUTF();
                        String name = stream.readUTF();
                        int mapId = stream.readByte();
                        int length = stream.readInt();
                        byte[] data = new byte[length];

                        for (int i = 0; i < length; i++)
                            data[i] = stream.readByte();

                        Prt.info("username:" + username);
                        Prt.info("token:" + token);
                        Prt.info("name:" + name);

                        Prt.info("mapId:" + mapId);

                        stream.close();

                        saves[mapId] = new saveData(name, data);

                        save();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            };

        }

        public InputStream getInputStream() throws IOException {
            Prt.info(this.getURL().getPath());
            switch (url.getPath()) {

                case "/level/save.html":

                    return postSave(this, this.proxy);

                case "/level/load.html":

                    Prt.info(this.getURL().getQuery());
                    String levelId = this.getURL().getQuery();
                    levelId = levelId.substring(levelId.indexOf("id=") + 3);
                    levelId = levelId.substring(0, levelId.indexOf("&"));
                    int id = Integer.parseInt(levelId);
                    saveData save = saves[id];
                    byte[] data = new byte[save.data.length + 4];
                    byte[] header = new byte[] { 0x00, 0x02, 0x6F, 0x6B };
                    for (int i = 0; i < 4; i++)
                        data[i] = header[i];
                    for (int i = 0; i < save.data.length; i++) {
                        data[i + 4] = save.data[i];
                    }
                    return new ByteArrayInputStream(data);

                case "/listmaps.jsp":
                default:
                    load();
                    String info = "";

                    for (int i = 0; i < 5; i++) {
                        info += (saves[i] == null ? "-" : saves[i].name) + (i == 4 ? "" : ";");
                    }

                    return new ByteArrayInputStream(info.getBytes(StandardCharsets.UTF_8));
            }

        }

        @Override
        public void disconnect() {

        }

        @Override
        public boolean usingProxy() {
            return this.proxy != null;
        }

    }

    public InputStream postSave(resourcesHandler handler, Proxy proxy) throws IOException {
        return new ByteArrayInputStream("ok".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Boolean check(URL url) {
        switch (url.getPath()) {
            case "/listmaps.jsp":
            case "/level/save.html":
            case "/level/load.html":
                return true;
        }
        return false;
    }

    @Override
    public URLConnection run(URL url, Proxy proxy) throws IOException {
        return new resourcesHandler(url, proxy);
    }

    @Override
    public URL run(URL url) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}

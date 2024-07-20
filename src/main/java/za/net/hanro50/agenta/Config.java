package za.net.hanro50.agenta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private File configFile;
    private static Config instance = new Config();
    private final Map<String, String> config = new HashMap<>();

    private void setSys(String name, String defaultValue) {
        config.put(name, System.getProperty(name, defaultValue));
    }

    private void setSystemConfig() {
        config.put("agenta.config.version", Version.get());
        setSys("agenta.prt.debug", "false");
        setSys("agenta.prt.color", "true");
        setSys("agenta.save.file", "saves.json");
        setSys("agenta.skin.resize", "true");
        setSys("agenta.assets.routing", "true");
        setSys("agenta.assets.url", "https://resources.download.minecraft.net/");
        setSys("agenta.assets.index",
                "https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json");
        setSys("agenta.assets.fml", "https://download.hanro50.net.za/fmllibs");

    }

    public File getConfigFile() {
        return this.configFile;
    }

    public static Config getInstance() {
        return instance;
    }

    public static String get(String name) {
        return instance.config.get(name);
    }

    private Config() {
        setSystemConfig();
    }

    private void save(File configFile) throws IOException {
        config.put("agenta.config.version", Version.get());
        configFile.createNewFile();
        String configData = "# Generated by Agenta " + Version.get() + " \n";
        configData += "# For more information visit: https://github.com/Hanro50/Agenta/ \n";
        List<String> keys = new ArrayList<String>(config.keySet());
        keys.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                int len = o1.length();
                if (o2.length() < len)
                    len = o2.length();
                for (int i = 0; i < len; i++) {
                    int diff = o1.charAt(i) - o2.charAt(i);
                    if (diff != 0)
                        return diff;
                }
                return o1.length() - o2.length();
            }

        });
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            configData += key + "=" + config.get(key) + "\n";
        }
        configData = configData.trim();
        Files.write(configFile.toPath(), configData.getBytes());
    }

    public void load(File configDir) {
        if (configDir == null)
            return;
        try {
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            configFile = new File(configDir, "agenta.txt");
            if (!configFile.exists()) {
                save(configFile);
            } else {
                InputStream is = new FileInputStream(configFile);
                BufferedReader buf = new BufferedReader(new InputStreamReader(is));

                String line = buf.readLine();
                while (line != null) {
                    if (!line.startsWith("#") && line.indexOf("=") > 0) {
                        String[] keySet = line.split("=", 2);
                        if (config.containsKey(keySet[0]))
                            config.put(keySet[0], keySet[1]);
                    }

                    line = buf.readLine();
                }
                buf.close();

                if (!config.containsKey("agenta.config.version")
                        || !config.get("agenta.config.version").equals(Version.get())) {
                    save(configFile);
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
package za.net.hanro50.agenta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Version {
    private final static String VERSION;
    static {
        InputStream inputStream = Version.class.getResourceAsStream("/agenta.version");
        BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            line = buf.readLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        VERSION = line != null ? line : "0.0.0";
    }

    public static String get() {
        return VERSION;
    }
}

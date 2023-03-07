package za.net.hanro50;

import java.io.File;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("HELLO WORLD");
        System.out.println(new File("/home/Hanro50/.minecraft/").getAbsolutePath());
        System.out.println(new File("/home/Hanro50/.minecraft").getAbsolutePath());
    }

    public static String URLRewrite(String str) {
        System.out.println(str);

        return str;

    }
}

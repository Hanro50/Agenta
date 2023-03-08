package net.minecraft.launchwrapper.injector;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;



public class VanillaTweakInjector implements IClassTransformer {
    public VanillaTweakInjector() {
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        return bytes;

    }

    public static File inject() {
        // Speed up imageloading
        System.out.println("Turning off ImageIO disk-caching");
        ImageIO.setUseCache(false);

        loadIconsOnFrames();

        // Set the workdir, return value will get assigned
        System.out.println("Setting gameDir to: " + Launch.minecraftHome);
        return Launch.minecraftHome;
    }

    public static void loadIconsOnFrames() {
        try {
            // Load icon from disk
            final File smallIcon = new File(Launch.assetsDir, "icons/icon_16x16.png");
            final File bigIcon = new File(Launch.assetsDir, "icons/icon_32x32.png");
            System.out.println("Loading current icons for window from: " + smallIcon + " and " + bigIcon);
            Display.setIcon(new ByteBuffer[]{
                    loadIcon(smallIcon),
                    loadIcon(bigIcon)
            });
            Frame[] frames = Frame.getFrames();

            if (frames != null) {
                final List<Image> icons = Arrays.<Image>asList(ImageIO.read(smallIcon), ImageIO.read(bigIcon));

                for (Frame frame : frames) {
                    try {
                        frame.setIconImages(icons);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

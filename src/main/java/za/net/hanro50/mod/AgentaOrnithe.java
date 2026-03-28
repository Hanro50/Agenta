package za.net.hanro50.mod;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import java.io.File;

public class AgentaOrnithe implements ModInitializer {
    @Override
    public void init() {
        try {
            File configDir = new File("config");
            Commom.load("Fabric", configDir);
        } catch (Throwable t) {
            Commom.load("Cursed Legacy");
        }
    }
}

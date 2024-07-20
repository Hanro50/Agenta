package za.net.hanro50.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;

public class AgentaFabric implements ModInitializer {
    @SuppressWarnings("deprecation")
    @Override
    public void onInitialize() {
        try {
            Commom.load("Fabric", FabricLoader.INSTANCE.getConfigDirectory());
        } catch (java.lang.NoClassDefFoundError e) {
            Commom.load("Cursed Legacy");
        }
    }

}

package za.net.hanro50.mod;

import net.fabricmc.api.ModInitializer;

public class AgentaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Commom.load("Fabric");
    }

}

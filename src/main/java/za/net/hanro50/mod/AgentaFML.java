package za.net.hanro50.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "Agenta", name = "Agenta", version = "1.8.5", useMetadata = true)
public class AgentaFML {
    @EventHandler
    public void loadConfigurationOld(FMLPreInitializationEvent evt) {
        Commom.load("Forge", evt.getModConfigurationDirectory());
    }

}

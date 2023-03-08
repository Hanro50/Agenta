package za.net.hanro50.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import za.net.hanro50.agenta.Prt;

@TransformerExclusions({  "org.apache.ibatis" })
@Mod(modid = "Wrapart", name = "Agenta", version = "1.7.1", useMetadata = true)
public class Wrapart {
    static {
        Prt.warn(
                "Please ignore the warnings about forge being unable to read 'org/apache/ibatis/annotations/Arg.class'. It is an expected error!");
    }
}

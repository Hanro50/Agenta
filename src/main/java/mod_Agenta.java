
import za.net.hanro50.agenta.Version;

public class mod_Agenta extends BaseMod {
    static {
        za.net.hanro50.mod.Commom.load("Legacy Modloader");
    }

    public String getName() {
        return "Agenta";
    }

    public String getVersion() {
        return Version.get();
    }

    public String Version() {
        return Version.get();
    }

    public void load() {
    }
}

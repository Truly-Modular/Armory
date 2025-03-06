package smartin.armory.forge;


import net.neoforged.fml.common.Mod;
import smartin.armory.Armory;

@Mod(Armory.MOD_ID)
public class ArmoryForge {
    public ArmoryForge() {
        Armory.init();
    }
}
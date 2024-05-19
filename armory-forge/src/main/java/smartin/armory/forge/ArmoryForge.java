package smartin.armory.forge;

import dev.architectury.platform.forge.EventBuses;
import smartin.armory.Armory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Armory.MOD_ID)
public class ArmoryForge {
    public ArmoryForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Armory.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            Armory.init();
    }
}
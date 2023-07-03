package smartin.armory.fabric;

import smartin.armory.Armory;
import net.fabricmc.api.ModInitializer;

public class ArmoryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Armory.init();
    }
}
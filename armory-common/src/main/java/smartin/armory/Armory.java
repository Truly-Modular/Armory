package smartin.armory;

import dev.architectury.event.events.common.LifecycleEvent;

public class Armory
{
	public static final String MOD_ID = "tm_armory";

	public static void init() {
		LifecycleEvent.SETUP.register(()-> new GenerateArmorModularConverter());
	}
}
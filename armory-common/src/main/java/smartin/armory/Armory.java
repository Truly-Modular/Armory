package smartin.armory;

import dev.architectury.event.EventResult;
import smartin.miapi.events.MiapiEvents;

public class Armory
{
	public static final String MOD_ID = "tm_armory";

	public static void init() {
		MiapiEvents.GENERATE_MATERIAL_CONVERTERS.register((material, tools, armorItems, isClient) -> {
			GenerateConvertersHelperArmor.setup(armorItems, material);
			return EventResult.pass();
		});
	}
}
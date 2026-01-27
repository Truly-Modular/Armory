package smartin.armory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import smartin.miapi.Miapi;
import smartin.miapi.item.ModularItemStackConverter;
import smartin.miapi.material.MaterialProperty;
import smartin.miapi.material.base.Material;
import smartin.miapi.material.generated.GeneratedMaterial;
import smartin.miapi.modules.ModuleInstance;
import smartin.miapi.modules.properties.ItemIdProperty;
import smartin.miapi.registries.RegistryInventory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenerateConvertersHelperArmor {

    public static void setup(List<ArmorItem> armorItems, Material material) {
        if (armorItems.isEmpty()) {
            Miapi.LOGGER.error("Armor setup aborted: armorItems list is empty.");
            return;
        }

        ArmorItem armorItem = armorItems.getFirst();
        if (armorItem == null) {
            Miapi.LOGGER.error("Armor setup aborted: first ArmorItem is null.");
            return;
        }

        if (!(material instanceof GeneratedMaterial generatedMaterial)) {
            Miapi.LOGGER.error(
                    "Armor setup aborted: material is not a GeneratedMaterial (material = {}).",
                    material
            );
            return;
        }

        // At this point we are guaranteed to reach toughness logic
        generatedMaterial.stats.put(
                "toughness",
                (double) armorItem.getMaterial().value().toughness()
        );

        if (armorItems.size() == 4) {
            double totalArmor = armorItems.stream()
                    .collect(Collectors.summarizingInt(ArmorItem::getDefense))
                    .getSum();

            double desiredHardness =
                    (totalArmor
                     + (generatedMaterial.stats.get("flexibility") / 4)
                     + (generatedMaterial.stats.get("density") / 4)
                     - 1
                    ) / 4.05;

            double max = Math.max(totalArmor, desiredHardness);

            if (!(Math.abs(totalArmor - desiredHardness) <= (15 / 100.0) * max)) {
                generatedMaterial.stats.put("armor_hardness", desiredHardness);
            }
        }

        addArmorConverter(armorItems, EquipmentSlot.HEAD, GenerateConvertersHelperArmor::helmetItem, material);
        addArmorConverter(armorItems, EquipmentSlot.CHEST, GenerateConvertersHelperArmor::chestplateItem, material);
        addArmorConverter(armorItems, EquipmentSlot.LEGS, GenerateConvertersHelperArmor::leggingsItem, material);
        addArmorConverter(armorItems, EquipmentSlot.FEET, GenerateConvertersHelperArmor::bootsItem, material);

    }

    private static void addArmorConverter(
            Collection<? extends ArmorItem> armorItems,
            EquipmentSlot slot,
            Function<Material, ItemStack> itemFactory,
            Material material
    ) {
        armorItems.stream()
                .filter(a -> a.getEquipmentSlot() == slot)
                .map(a -> (Item) a)
                .findFirst()
                .ifPresent(item ->
                        ModularItemStackConverter.converters.add(stack -> {
                            if (stack.getItem().equals(item)) {
                                return itemFactory.apply(material);
                            }
                            return stack;
                        })
                );
    }

    private static ModuleInstance module(String moduleId, Material material) {
        ModuleInstance instance = new ModuleInstance(
                Objects.requireNonNull(RegistryInventory.ITEM_MODULE_MIAPI_REGISTRY.get(
                        Miapi.id("tm_armory", moduleId)
                ))
        );
        MaterialProperty.setMaterial(instance, material);
        return instance;
    }

    private static ItemStack buildArmorItem(
            String rootModuleId,
            Material material,
            Map<String, String> subModules
    ) {
        ModuleInstance root = new ModuleInstance(
                Objects.requireNonNull(RegistryInventory.ITEM_MODULE_MIAPI_REGISTRY.get(
                        Miapi.id("tm_armory", rootModuleId)
                ))
        );

        subModules.forEach((slot, moduleId) ->
                root.setSubModule(slot, module(moduleId, material))
        );

        ItemStack stack = new ItemStack(RegistryInventory.modularItem);
        root.writeToItem(stack);
        return ItemIdProperty.changeId(stack);
    }



    public static ItemStack bootsItem(Material material) {
        return buildArmorItem(
                "armor/boots",
                material,
                Map.of(
                        "boot_left",  "armor/default/boot_left",
                        "boot_right", "armor/default/boot_right"
                )
        );
    }

    public static ItemStack leggingsItem(Material material) {
        return buildArmorItem(
                "armor/pants",
                material,
                Map.of(
                        "belt",      "armor/default/belt",
                        "leg_left",  "armor/default/leg_left",
                        "leg_right", "armor/default/leg_right"
                )
        );
    }


    public static ItemStack chestplateItem(Material material) {
        return buildArmorItem(
                "armor/chestplate",
                material,
                Map.of(
                        "chest_front", "armor/default/front_chest",
                        "chest_back",  "armor/default/back_chest",
                        "arm_left",    "armor/default/arm_left",
                        "arm_right",   "armor/default/arm_right"
                )
        );
    }


    public static ItemStack helmetItem(Material material) {
        return buildArmorItem(
                "armor/helmet",
                material,
                Map.of(
                        "hat", "armor/default/helmet"
                )
        );
    }
}

package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.gui.ModMenus;
import net.hydra.jojomod.client.gui.diverdown.custom_workbench_code.*;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;

public final class FabricMenus {
    //there's no registry yet for custom UI that doesn't use the default minecraft abstract menu, so I had to make one for the custom Diver Down workbenches textures.
    //If somebody has a better way to do this, please message me on discord- 88superguy
    public static final MenuType<DiverDownCraftingMenu> DIVER_DOWN_CRAFTING = Registry.register(
            BuiltInRegistries.MENU,
            new ResourceLocation(Roundabout.MOD_ID, "diver_down_crafting"),
            new MenuType<>(DiverDownCraftingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final MenuType<AnvilMenu> DIVER_DOWN_ANVIL = Registry.register(
            BuiltInRegistries.MENU,
            new ResourceLocation(Roundabout.MOD_ID, "diver_down_anvil"),
            new MenuType<>(DiverDownAnvilMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final MenuType<SmithingMenu> DIVER_DOWN_SMITHING = Registry.register(
            BuiltInRegistries.MENU,
            new ResourceLocation(Roundabout.MOD_ID, "diver_down_smithing"),
            new MenuType<>(DiverDownSmithingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final MenuType<StonecutterMenu> DIVER_DOWN_STONECUTTER = Registry.register(
            BuiltInRegistries.MENU,
            new ResourceLocation(Roundabout.MOD_ID, "diver_down_stonecutter"),
            new MenuType<>(DiverDownStonecutterMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final MenuType<LoomMenu> DIVER_DOWN_LOOM = Registry.register(
            BuiltInRegistries.MENU,
            new ResourceLocation(Roundabout.MOD_ID, "diver_down_loom"),
            new MenuType<>(DiverDownLoomMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private FabricMenus() {
    }

    public static void register() {
        ModMenus.DIVER_DOWN_CRAFTING = DIVER_DOWN_CRAFTING;
        ModMenus.DIVER_DOWN_ANVIL = DIVER_DOWN_ANVIL;
        ModMenus.DIVER_DOWN_SMITHING = DIVER_DOWN_SMITHING;
        ModMenus.DIVER_DOWN_STONECUTTER = DIVER_DOWN_STONECUTTER;
        ModMenus.DIVER_DOWN_LOOM = DIVER_DOWN_LOOM;
    }

}
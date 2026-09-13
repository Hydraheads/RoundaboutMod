package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.gui.diverdown.custom_workbench_code.*;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeMenus {
        //there's no registry yet for custom UI that doesn't use the default minecraft abstract menu, so I had to make one for the custom Diver Down workbenches textures.
        //If somebody has a better way to do this, please message me on discord- 88superguy
        public static final DeferredRegister<MenuType<?>> MENUS =
                DeferredRegister.create(ForgeRegistries.MENU_TYPES, Roundabout.MOD_ID);

        //Diver Down Crafting Table registry
        public static final RegistryObject<MenuType<DiverDownCraftingMenu>> DIVER_DOWN_CRAFTING =
                MENUS.register("diver_down_crafting", () ->
                        new MenuType<>(DiverDownCraftingMenu::new, FeatureFlags.DEFAULT_FLAGS));
        //Diver Down Anvil register
        public static final RegistryObject<MenuType<AnvilMenu>> DIVER_DOWN_ANVIL =
                MENUS.register("diver_down_anvil", () ->
                        new MenuType<>(DiverDownAnvilMenu::new, FeatureFlags.DEFAULT_FLAGS));
        //Diver Down Smithing Table registry
        public static final RegistryObject<MenuType<SmithingMenu>> DIVER_DOWN_SMITHING =
                MENUS.register("diver_down_smithing", () ->
                        new MenuType<>(DiverDownSmithingMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private ForgeMenus() {
    }

    public static void initialize() {
        ModMenus.DIVER_DOWN_CRAFTING = DIVER_DOWN_CRAFTING.get();
        ModMenus.DIVER_DOWN_ANVIL = DIVER_DOWN_ANVIL.get();
        ModMenus.DIVER_DOWN_SMITHING = DIVER_DOWN_SMITHING.get();
    }
}
package net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.StonecutterMenu;

public class DiverDownStonecutterScreen extends StonecutterScreen {
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/gui/diver_down/workbench_ui/stonecutter.png");

    public DiverDownStonecutterScreen(StonecutterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
package net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.LoomMenu;

public class DiverDownLoomScreen extends LoomScreen {
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/gui/diver_down/workbench_ui/loom.png");

    public DiverDownLoomScreen(LoomMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
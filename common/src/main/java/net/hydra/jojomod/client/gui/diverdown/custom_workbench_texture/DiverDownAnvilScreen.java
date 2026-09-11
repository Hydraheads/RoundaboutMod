package net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DiverDownAnvilScreen extends AnvilScreen {
    private static final ResourceLocation TEXTURE =
        new ResourceLocation(Roundabout.MOD_ID, "textures/gui/diver_down/workbench_ui/anvil.png");

    public DiverDownAnvilScreen(AnvilMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        // the entire "costs too much" etc. etc. text relies on renderlabels so the easiest solution is to literally just shove this entire text offscreen.
        this.inventoryLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Custom background texture
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Vanilla overlay for text field box and hammer / error cross
        graphics.blit(TEXTURE, x + 59, y + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
            graphics.blit(TEXTURE, x + 99, y + 45, this.imageWidth, 0, 28, 21);
        }
    }
}
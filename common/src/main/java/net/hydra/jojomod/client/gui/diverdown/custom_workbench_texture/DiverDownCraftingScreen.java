package net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.gui.diverdown.custom_workbench_code.DiverDownCraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DiverDownCraftingScreen extends AbstractContainerScreen<DiverDownCraftingMenu> {
    //this code handles the TEXTURES, NOT the actual crafting code.

    private static final ResourceLocation TEXTURE =
        new ResourceLocation(Roundabout.MOD_ID, "textures/gui/diver_down/workbench_ui/crafting_table.png");

    public DiverDownCraftingScreen(DiverDownCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, Component.translatable("container.inventory"), 8, 72, 4210752, false);
    }
}
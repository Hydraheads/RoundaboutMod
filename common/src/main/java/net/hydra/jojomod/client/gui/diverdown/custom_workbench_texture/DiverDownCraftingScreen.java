package net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;

public class DiverDownCraftingScreen extends CraftingScreen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/gui/diver_down/workbench_ui/crafting_table.png");

    public DiverDownCraftingScreen(CraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void init() {
        super.init();

        this.children().stream()
                .filter(child -> child instanceof ImageButton)
                .findFirst()
                .ifPresent(this::removeWidget);

        this.addRenderableWidget(new ImageButton(
                this.leftPos + 5,
                this.height / 2 - 49, 20, 18, 0, 168, 19, TEXTURE,
                (button) -> {
                    this.getRecipeBookComponent().toggleVisibility();
                    this.leftPos = this.getRecipeBookComponent().updateScreenPosition(this.width, this.imageWidth);
                    button.setPosition(this.leftPos + 5, this.height / 2 - 49);
                }
        ){
            @Override
            public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                // for the hovered version.
                int v = this.isHoveredOrFocused() ? 187 : 168;
                graphics.blit(TEXTURE, this.getX(), this.getY(), 0, v, this.width, this.height);
            }
        });
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}
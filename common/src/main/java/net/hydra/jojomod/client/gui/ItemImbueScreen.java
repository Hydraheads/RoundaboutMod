package net.hydra.jojomod.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ItemImbueScreen extends Screen {
    private static final ResourceLocation MENU_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/ge_item_imbue.png");

    private final int imageWidth = 195;
    private final int imageHeight = 136;
    private int leftPos;
    private int topPos;

    private float scrollOffs = 0.0F;
    private boolean isScrolling = false;

    private final int totalColumns = 9;
    private final int visibleRows = 5;
    //PLACEHOLDER
    private int totalItems = 100;

    public ItemImbueScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(MENU_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int scrollBarX = this.leftPos + 175;
        int scrollBarYMin = this.topPos + 18;
        int scrollBarYMax = this.topPos + 108;
        int scrollRange = scrollBarYMax - scrollBarYMin - 15;
        int currentScrollY = scrollBarYMin + (int) (this.scrollOffs * scrollRange);

        guiGraphics.blit(MENU_TEXTURE, scrollBarX, currentScrollY, 195, 0, 12, 15);

        int startRow = getScrollRow();
        for (int i = 0; i < visibleRows * totalColumns; i++) {
            int itemIndex = (startRow * totalColumns) + i;
            if (itemIndex >= this.totalItems) break;

            int col = i % totalColumns;
            int row = i / totalColumns;

            int slotX = this.leftPos + 8 + (col * 18);
            int slotY = this.topPos + 18 + (row * 18);

            //TODO: replace this with the items and stuff
            guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80FFAA00);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.canScroll()) {
            int totalRows = Mth.ceil((float) this.totalItems / (float) this.totalColumns);
            int hiddenRows = totalRows - this.visibleRows;

            this.scrollOffs = (float) ((double) this.scrollOffs - delta / (double) hiddenRows);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.isHoveringScrollbar(mouseX, mouseY)) {
                this.isScrolling = this.canScroll();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isScrolling) {
            int scrollBarYMin = this.topPos + 18;
            int scrollBarYMax = this.topPos + 108;
            int scrollRange = scrollBarYMax - scrollBarYMin - 15;

            this.scrollOffs = ((float) mouseY - (float) scrollBarYMin - 7.5F) / (float) scrollRange;
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isScrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isHoveringScrollbar(double mouseX, double mouseY) {
        int scrollXMin = this.leftPos + 175;
        int scrollXMax = scrollXMin + 12;
        int scrollYMin = this.topPos + 18;
        int scrollYMax = this.topPos + 108;
        return mouseX >= scrollXMin && mouseX <= scrollXMax && mouseY >= scrollYMin && mouseY <= scrollYMax;
    }

    private boolean canScroll() {
        return this.totalItems > this.visibleRows * this.totalColumns;
    }

    private int getScrollRow() {
        int totalRows = Mth.ceil((float) this.totalItems / (float) this.totalColumns);
        int hiddenRows = Math.max(0, totalRows - this.visibleRows);
        return (int) ((double) (this.scrollOffs * (float) hiddenRows) + 0.5D);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
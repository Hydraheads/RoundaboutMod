package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.menu.GamblingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

public class GamblingTableScreen extends AbstractContainerScreen<GamblingTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/gui/gambling_table.png");

    // Change these if the texture also gets changed
    private static final int BTN_WIDTH = 14;
    private static final int BTN_HEIGHT = 14;
    private static final int ACCEPT_X = 132;
    private static final int ACCEPT_Y = 35;
    private static final int DENY_X = 155;
    private static final int DENY_Y = 35;

    // Reminder: x and y represent their positions on the player's screens
    // u and v refer to the texture's position in the sprite sheet.
    private static final int ACCEPT_IDLE_U = 0;
    private static final int ACCEPT_IDLE_V = 194;
    private static final int ACCEPT_CLICKED_U = 14;
    private static final int ACCEPT_CLICKED_V = 194;

    private static final int DENY_IDLE_U = 0;
    private static final int DENY_IDLE_V = 166;
    private static final int DENY_CLICKED_U = 14;
    private static final int DENY_CLICKED_V = 166;

    private boolean acceptPressed = false;
    private boolean denyPressed = false;

    public GamblingTableScreen(GamblingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.menu.isHost(this.minecraft.player) && !this.menu.hasGameStarted()) {
            this.minecraft.setScreen(new GamblingGameSelectionScreen(this.menu));
        }
    }

    protected boolean isHovering(int btnRelX, int btnRelY, int width, int height, double mouseX, double mouseY) {
        int screenBtnX = this.leftPos + btnRelX;
        int screenBtnY = this.topPos + btnRelY;
        return mouseX >= screenBtnX && mouseX < screenBtnX + width &&
                mouseY >= screenBtnY && mouseY < screenBtnY + height;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Accept Button
        boolean acceptHovered = isHovering(ACCEPT_X, ACCEPT_Y, BTN_WIDTH, BTN_HEIGHT, mouseX, mouseY);
        if (this.acceptPressed && acceptHovered) {
            graphics.blit(TEXTURE, x + ACCEPT_X, y + ACCEPT_Y, ACCEPT_CLICKED_U, ACCEPT_CLICKED_V, BTN_WIDTH, BTN_HEIGHT);
        } else if (!acceptHovered) {
            graphics.blit(TEXTURE, x + ACCEPT_X, y + ACCEPT_Y, ACCEPT_IDLE_U, ACCEPT_IDLE_V, BTN_WIDTH, BTN_HEIGHT);
        }

        // Deny Button
        boolean denyHovered = isHovering(DENY_X, DENY_Y, BTN_WIDTH, BTN_HEIGHT, mouseX, mouseY);
        if (this.denyPressed && denyHovered) {
            graphics.blit(TEXTURE, x + DENY_X, y + DENY_Y, DENY_CLICKED_U, DENY_CLICKED_V, BTN_WIDTH, BTN_HEIGHT);
        } else if (!denyHovered) {
            graphics.blit(TEXTURE, x + DENY_X, y + DENY_Y, DENY_IDLE_U, DENY_IDLE_V, BTN_WIDTH, BTN_HEIGHT);
        }
    }

    // Puts the game name under the host slots so the players know what game was selected
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        Component gameTitle = this.menu.getGameName();
        graphics.drawString(this.font, gameTitle, 8, 38, 0x404040, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // button 0 means left click
        if (button == 0) {
            if (isHovering(ACCEPT_X, ACCEPT_Y, BTN_WIDTH, BTN_HEIGHT, mouseX, mouseY)) {
                this.acceptPressed = true;
                playClickSound();
                return true;
            }
            if (isHovering(DENY_X, DENY_Y, BTN_WIDTH, BTN_HEIGHT, mouseX, mouseY)) {
                this.denyPressed = true;
                playClickSound();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.acceptPressed) {
                this.acceptPressed = false;
                if (isHovering(ACCEPT_X, ACCEPT_Y, BTN_WIDTH, BTN_HEIGHT, mouseX, mouseY)) {
                    onAcceptClicked();
                    return true;
                }
            }
            if (this.denyPressed) {
                this.denyPressed = false;
                if (isHovering(DENY_X, DENY_Y, BTN_WIDTH, BTN_HEIGHT, mouseX, mouseY)) {
                    onDenyClicked();
                    return true;
                }
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
        );
    }

    // handleInventoryButtonClick syncs the server with the client

    private void onAcceptClicked() {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, GamblingTableMenu.BUTTON_ACCEPT);
        }
    }

    private void onDenyClicked() {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, GamblingTableMenu.BUTTON_DENY);
        }
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
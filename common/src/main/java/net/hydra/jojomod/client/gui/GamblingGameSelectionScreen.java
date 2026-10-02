package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.menu.GamblingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import java.util.List;

public class GamblingGameSelectionScreen extends Screen {
    private final GamblingTableMenu menu;
    private int currentPage = 0;
    private static final int GAMES_PER_PAGE = 4;

    // Registry of available games (gameId, translationKey)
    public record GameEntry(int id, Component name) {}
    private final List<GameEntry> games = List.of(
            new GameEntry(1, Component.translatable("gui.roundabout.gambling_table.game.coin_drop"))
    );

    public GamblingGameSelectionScreen(GamblingTableMenu menu) {
        super(Component.translatable("gui.roundabout.gambling_table.select_game"));
        this.menu = menu;
    }

    private int getMaxPages() {
        return (int) Math.ceil((double) games.size() / GAMES_PER_PAGE);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;

        // Title
        graphics.drawCenteredString(this.font, this.title, centerX, startY - 20, 0xFFFFFF);

        // Render Games on Current Page
        int startIndex = currentPage * GAMES_PER_PAGE;
        int endIndex = Math.min(startIndex + GAMES_PER_PAGE, games.size());

        for (int i = startIndex; i < endIndex; i++) {
            int slotY = startY + (i - startIndex) * 24;
            boolean hovered = mouseX >= centerX - 80 && mouseX <= centerX + 80 &&
                    mouseY >= slotY && mouseY <= slotY + 20;

            // Box background
            int color = hovered ? 0x805555FF : 0x80000000;
            graphics.fill(centerX - 80, slotY, centerX + 80, slotY + 20, color);
            graphics.renderOutline(centerX - 80, slotY, 160, 20, hovered ? 0xFFFFFFFF : 0xFFAAAAAA);

            // Game Name
            graphics.drawCenteredString(this.font, games.get(i).name(), centerX, slotY + 6, hovered ? 0xFFFF55 : 0xFFFFFF);
        }

        int arrowY = startY + (GAMES_PER_PAGE * 24) + 10;

        // Left Arrow
        if (currentPage > 0) {
            boolean leftHovered = mouseX >= centerX - 80 && mouseX <= centerX - 60 && mouseY >= arrowY && mouseY <= arrowY + 16;
            graphics.drawCenteredString(this.font, "<", centerX - 70, arrowY + 4, leftHovered ? 0xFFFF55 : 0xFFFFFF);
        }

        // Page Number
        String pageStr = (currentPage + 1) + " / " + Math.max(1, getMaxPages());
        graphics.drawCenteredString(this.font, pageStr, centerX, arrowY + 4, 0xAAAAAA);

        // Right Arrow
        if (currentPage < getMaxPages() - 1) {
            boolean rightHovered = mouseX >= centerX + 60 && mouseX <= centerX + 80 && mouseY >= arrowY && mouseY <= arrowY + 16;
            graphics.drawCenteredString(this.font, ">", centerX + 70, arrowY + 4, rightHovered ? 0xFFFF55 : 0xFFFFFF);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int centerX = this.width / 2;
            int startY = this.height / 2 - 60;
            int startIndex = currentPage * GAMES_PER_PAGE;
            int endIndex = Math.min(startIndex + GAMES_PER_PAGE, games.size());

            // Check if clicked on game
            for (int i = startIndex; i < endIndex; i++) {
                int slotY = startY + (i - startIndex) * 24;
                if (mouseX >= centerX - 80 && mouseX <= centerX + 80 &&
                        mouseY >= slotY && mouseY <= slotY + 20) {

                    playClickSound();
                    selectGame(games.get(i).id());
                    return true;
                }
            }

            // For clicking on arrows
            int arrowY = startY + (GAMES_PER_PAGE * 24) + 10;
            if (currentPage > 0 && mouseX >= centerX - 80 && mouseX <= centerX - 60 && mouseY >= arrowY && mouseY <= arrowY + 16) {
                currentPage--;
                playClickSound();
                return true;
            }
            if (currentPage < getMaxPages() - 1 && mouseX >= centerX + 60 && mouseX <= centerX + 80 && mouseY >= arrowY && mouseY <= arrowY + 16) {
                currentPage++;
                playClickSound();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void selectGame(int gameId) {
        if (this.minecraft != null && this.minecraft.gameMode != null && this.menu != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 10 + gameId);
            this.menu.setSelectedGameClient(gameId);
            if (this.minecraft.player != null) {
                this.minecraft.setScreen(new GamblingTableScreen(
                        this.menu, this.minecraft.player.getInventory(), Component.translatable("container.roundabout.gambling_table")));
            }
        }
    }

    private void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
        );
    }
}
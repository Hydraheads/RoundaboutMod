package net.hydra.jojomod.client.gui.gambling_table;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.menu.GamblingTableMenu;
import net.hydra.jojomod.menu.gambling_games.CoinDropGame;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class CoinDropScreen extends Screen {
    private static final ResourceLocation TEXTURE_1 =
            new ResourceLocation(Roundabout.MOD_ID, "textures/gui/gambling_table/coin_drop/coin_drop_1.png");
    private static final ResourceLocation TEXTURE_2 =
            new ResourceLocation(Roundabout.MOD_ID, "textures/gui/gambling_table/coin_drop/coin_drop_2.png");

    private static final int MAIN_SCREEN_U = 4, MAIN_SCREEN_V = 2;
    private static final int MAIN_SCREEN_WIDTH = 194, MAIN_SCREEN_HEIGHT = 181;
    private static final int FULL_CUP_U = 45, FULL_CUP_V = 192;
    private static final int FULL_CUP_WIDTH = 111, FULL_CUP_HEIGHT = 61;
    private static final int METER_EMPTY_U = 202, METER_EMPTY_V = 1;
    private static final int METER_EMPTY_WIDTH = 31, METER_EMPTY_HEIGHT = 193;
    private static final int METER_FULL_U = 237, METER_FULL_V = 7;
    private static final int METER_FULL_WIDTH = 15, METER_FULL_HEIGHT = 181;

    private static final int METER_OFFSET = 6;

    private final CoinDropGame game;
    private boolean isHoldingSpace = false;
    private int chargeTicks = 0;
    private float chargeProgress = 0.0f;

    public CoinDropScreen(CoinDropGame game) {
        super(CommonComponents.EMPTY);
        this.game = game;
    }

    @Override
    public void tick() {
        super.tick();

        // If the game ends, close the minigame screen
        if (this.game.isGameOver()) {
            this.onClose();
            return;
        }

        // charge bar logic
        if (this.isHoldingSpace && isMyTurn()) {
            this.chargeTicks++;
            if (this.chargeTicks < 10) {
                this.chargeProgress = this.chargeTicks * 0.1f;
            } else {
                this.chargeProgress = 0.8f + (2.0f / (float) (this.chargeTicks - 9)) * 0.1f;
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        //normal cup
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int x = centerX - (MAIN_SCREEN_WIDTH / 2);
        int y = centerY - (MAIN_SCREEN_HEIGHT / 2);

        graphics.blit(TEXTURE_1, x, y, MAIN_SCREEN_U, MAIN_SCREEN_V, MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);

        // filled cup
        float tensionRatio = 0.0f;
        if (this.game != null) {
            tensionRatio = Math.max(0.0f, Math.min(1.0f, this.game.getTension() / 100.0f));
        }

        int fillHeight = (int) (tensionRatio * FULL_CUP_HEIGHT);

        if (fillHeight > 0) {
            int xOffset = x + 41;
            int yOffset = y + 95;
            // scissors is pretty self-explanatory, you just "cut" the image where you want to.
            int scissorTop = yOffset + (FULL_CUP_HEIGHT - fillHeight);
            graphics.enableScissor(xOffset, scissorTop, xOffset + FULL_CUP_WIDTH, yOffset + FULL_CUP_HEIGHT);

            graphics.blit(TEXTURE_1, xOffset, yOffset, FULL_CUP_U, FULL_CUP_V, FULL_CUP_WIDTH, FULL_CUP_HEIGHT);

            graphics.disableScissor();
        }

        //charge meter
        if (isMyTurn()) {
            // put this next to the main hud
            int meterX = x + MAIN_SCREEN_WIDTH + METER_OFFSET;
            int meterY = y + (MAIN_SCREEN_HEIGHT - METER_EMPTY_HEIGHT) / 2;

            // empty meter
            graphics.blit(TEXTURE_1, meterX, meterY, METER_EMPTY_U, METER_EMPTY_V, METER_EMPTY_WIDTH, METER_EMPTY_HEIGHT);

            // full
            int xMeterOffset = meterX + 8;
            int yMeterOffset = meterY + 6;
            int meterFillHeight = (int) (this.chargeProgress * METER_FULL_HEIGHT);
            if (meterFillHeight > 0) {
                int meterScissorTop = yMeterOffset + (METER_FULL_HEIGHT - meterFillHeight);
                graphics.enableScissor(xMeterOffset, meterScissorTop, xMeterOffset + METER_FULL_WIDTH, yMeterOffset + METER_FULL_HEIGHT);
                graphics.blit(TEXTURE_1, xMeterOffset, yMeterOffset, METER_FULL_U, METER_FULL_V, METER_FULL_WIDTH, METER_FULL_HEIGHT);
                graphics.disableScissor();
            }
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    // both methods below are used to track the user input for holding space.
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean isJumpKey = this.minecraft != null && this.minecraft.options.keyJump.matches(keyCode, scanCode);
        if (isJumpKey && isMyTurn() && !this.isHoldingSpace) {
            this.isHoldingSpace = true;
            this.chargeTicks = 0;
            this.chargeProgress = 0.0f;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        boolean isJumpKey = this.minecraft != null && this.minecraft.options.keyJump.matches(keyCode, scanCode);
        if (isJumpKey && this.isHoldingSpace) {
            this.isHoldingSpace = false;
            this.game.dropCoin(this.chargeProgress);
            this.chargeTicks = 0;
            this.chargeProgress = 0.0f;
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private boolean isMyTurn() {
        if (this.minecraft == null || this.minecraft.player == null) return false;

        if (this.minecraft.player.containerMenu instanceof GamblingTableMenu menu) {
            boolean iAmHost = menu.isHost(this.minecraft.player);
            boolean isHostsTurn = menu.isHostsTurn();
            return (iAmHost && isHostsTurn) || (!iAmHost && !isHostsTurn);
        }

        return this.game.isPlayerTurn(this.minecraft.player);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
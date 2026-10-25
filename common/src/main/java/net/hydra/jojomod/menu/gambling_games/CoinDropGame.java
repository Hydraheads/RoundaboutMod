package net.hydra.jojomod.menu.gambling_games;

import net.hydra.jojomod.block.GamblingTableBlockEntity;
import net.hydra.jojomod.menu.GamblingGame;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CoinDropGame extends GamblingGame {
    public static final int ACTION_DROP_COIN = 1;

    // Spillage / Tension: 0.0 to 100.0 (%)
    private float currentTension = 0.0f;
    private float lastSpillageAdded = 0.0f;
    private int lastDropQuality = 0; // 1: Splash, 2: Good, 3: Great, 4: Perfect

    public CoinDropGame(GamblingTableBlockEntity table) {
        super(table);
    }

    // Checks if it's this specific player's turn
    public boolean isPlayerTurn(Player player) {
        if (player == null || this.table == null) return false;
        boolean isHost = this.table.getPlayerRole(player) == GamblingTableBlockEntity.Role.HOST;
        return isHost == this.isHostTurn;
    }

    // Client-side call to perform the drop
    public void dropCoin(float charge) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode != null && mc.player != null) {
            int chargeEncoded = (int) (Math.max(0.0f, Math.min(1.0f, charge)) * 100);
            // Uses the player's active open container ID without coupling to GamblingTableMenu
            mc.gameMode.handleInventoryButtonClick(mc.player.containerMenu.containerId, 1000 + chargeEncoded);
        }
    }

    @Override
    public byte getGameId() {
        return GamblingTableBlockEntity.COIN_DROP;
    }

    @Override
    public void handleAction(Player player, int actionId, int extraData) {
        if (gameOver || table.getLevel() == null || table.getLevel().isClientSide()) return;

        // Verify it is this player's turn
        boolean isHost = table.getPlayerRole(player) == GamblingTableBlockEntity.Role.HOST;
        if (isHost != isHostTurn) return;

        if (actionId == ACTION_DROP_COIN) {
            // extraData is encoded charge: 0 to 100 (0.00 to 1.00)
            float chargeProgress = Math.max(0.0f, Math.min(1.0f, extraData / 100.0f));

            // Calculate spillage: 1.0 = 5% rise, 0.0 = 30% rise
            float penaltyFactor = 1.0f - chargeProgress;
            this.lastSpillageAdded = 5.0f + (penaltyFactor * 25.0f);
            this.currentTension += this.lastSpillageAdded;

            // Determine drop quality for feedback
            if (chargeProgress <= 0.33f) {
                this.lastDropQuality = 1;
            } else if (chargeProgress <= 0.611f) {
                this.lastDropQuality = 2;
            } else if (chargeProgress <= 0.833f) {
                this.lastDropQuality = 3;
            } else {
                this.lastDropQuality = 4;
            }

            // Check for game over (spill over 100%)
            if (this.currentTension >= 100.0f) {
                // The current player caused the spill, so the OTHER player wins!
                broadcastMessage(player.getScoreboardName() + " spilled the glass!");
                endGame(!isHostTurn);
            } else {
                switchTurn();
            }
            this.table.setChanged();
        }
    }

    public float getTension() { return currentTension; }
    public float getLastSpillageAdded() { return lastSpillageAdded; }
    public int getLastDropQuality() { return lastDropQuality; }

    private void broadcastMessage(String message) {
        if (table.getLevel() != null && table.getLevel().getServer() != null) {
            table.getLevel().getServer().getPlayerList().broadcastSystemMessage(Component.literal(message), false);
        }
    }

    @Override
    public void onPlayerLeave(Player player) {
        boolean isHost = table.getPlayerRole(player) == GamblingTableBlockEntity.Role.HOST;
        endGame(!isHost);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putBoolean("IsHostTurn", isHostTurn);
        tag.putFloat("Tension", currentTension);
        tag.putFloat("LastSpillage", lastSpillageAdded);
        tag.putInt("LastQuality", lastDropQuality);
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        this.isHostTurn = tag.getBoolean("IsHostTurn");
        this.currentTension = tag.getFloat("Tension");
        this.lastSpillageAdded = tag.getFloat("LastSpillage");
        this.lastDropQuality = tag.getInt("LastQuality");
    }
}
package net.hydra.jojomod.menu;

import net.hydra.jojomod.block.GamblingTableBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public abstract class GamblingGame {
    protected final GamblingTableBlockEntity table;
    protected boolean isHostTurn = true;
    protected boolean gameOver = false;

    public GamblingGame(GamblingTableBlockEntity table) {
        this.table = table;
    }

    // abstract getters and setters
    // you NEED to override these, even if you don't need to use them
    public abstract byte getGameId();
    public abstract void handleAction(Player player, int actionId, int extraData);
    public abstract void onPlayerLeave(Player player);
    // games can be saved if someone leaves, so that needs to be saved
    public abstract void saveAdditional(CompoundTag tag);
    public abstract void loadAdditional(CompoundTag tag);

    // checks to see if it's the host's turn or not
    public boolean isHostTurn() {
        return isHostTurn;
    }

    // is it game over?
    public boolean isGameOver() {
        return gameOver;
    }

    // Swaps the current turn between Host and Challenger
    protected void switchTurn() {
        this.isHostTurn = !this.isHostTurn;
        this.table.setChanged();
    }

    // Ends the game and distributes bets to the winner
    protected void endGame(boolean hostWon) {
        this.gameOver = true;
        this.table.setGameInProgress(false);

        // call on the gambling table to distribute bets
        if (!this.table.getLevel().isClientSide()) {
            this.table.awardBetsToWinner(hostWon);
        }

        this.table.setChanged();
    }
}
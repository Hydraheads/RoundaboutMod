package net.hydra.jojomod.menu.gambling_games;

import net.hydra.jojomod.block.GamblingTableBlockEntity;
import net.hydra.jojomod.menu.GamblingGame;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import java.util.Random;

public class CoinDropGame extends GamblingGame {
    public static final int ACTION_FLIP_COIN = 0;
    private final Random random = new Random();

    public CoinDropGame(GamblingTableBlockEntity table) {
        super(table);
        executeCoinFlip();
    }

    @Override
    public byte getGameId() {
        return GamblingTableBlockEntity.COIN_DROP;
    }

    @Override
    public void handleAction(Player player, int actionId, int extraData) {
    }

    public void executeCoinFlip() {
        if (gameOver || table.getLevel() == null || table.getLevel().isClientSide()) return;

        boolean hostWon = random.nextBoolean();

        if (hostWon) {
            Player host = table.getHostUUID() != null ? table.getLevel().getPlayerByUUID(table.getHostUUID()) : null;
            String hostName = host != null ? host.getScoreboardName() : "Unknown Host";
            String hostUUIDStr = table.getHostUUID() != null ? table.getHostUUID().toString() : "N/A";

            broadcastMessage("HOST (" + hostUUIDStr + " " + hostName + ") WINS!");
        } else {
            Player challenger = table.getChallengerUUID() != null ? table.getLevel().getPlayerByUUID(table.getChallengerUUID()) : null;
            String challengerName = challenger != null ? challenger.getScoreboardName() : "Unknown Challenger";
            String challengerUUIDStr = table.getChallengerUUID() != null ? table.getChallengerUUID().toString() : "N/A";
            broadcastMessage("CHALLENGER (" + challengerUUIDStr + " " + challengerName + ") WINS!");
        }

        endGame(hostWon);
    }

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
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        this.isHostTurn = tag.getBoolean("IsHostTurn");
    }
}
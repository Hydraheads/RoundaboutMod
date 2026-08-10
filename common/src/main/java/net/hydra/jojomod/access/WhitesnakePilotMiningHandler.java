package net.hydra.jojomod.access;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;

public interface WhitesnakePilotMiningHandler {
    void roundaboutWhitesnake$handleMining(BlockPos pos, ServerboundPlayerActionPacket.Action action,
                                           Direction direction, int buildHeight, int sequence);
}

package net.hydra.jojomod.mixin.whitesnake;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerGameMode.class)
public interface ServerPlayerGameModeAccessor {
    @Accessor("player")
    ServerPlayer roundabout$getPlayer();
}

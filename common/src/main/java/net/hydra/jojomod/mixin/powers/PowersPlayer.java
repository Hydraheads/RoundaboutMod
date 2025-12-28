package net.hydra.jojomod.mixin.powers;

import net.hydra.jojomod.access.IPowersPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PowersPlayer extends LivingEntity implements IPowersPlayer {
    protected PowersPlayer(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
}

package net.hydra.jojomod.entity;

import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class KingCrimsonCloneEntity extends CloneEntity {

    public int timer = 0;
    public KingCrimsonCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public float getSpeed() {
        if (this.getPlayer() != null){
            return this.getPlayer().getSpeed();
        }
        return super.getSpeed();
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if ($$0.is(ModDamageTypes.GO_BEYOND)){
            if (this.getPlayer() != null){
                this.getPlayer().hurt($$0,$$1);
            }
        }
        return super.hurt($$0,$$1);
    }

    @Override
    public void tick() {
        super.tick();
    }

}

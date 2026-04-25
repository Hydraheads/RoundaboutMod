package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;

import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import java.util.UUID;

public class BlockBombEntity extends StandEntity {
	
	private BlockPos bombPos;
	
	
	
	public BlockBombEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		super($$0, $$1);
	}
	
	public void setBlockPos(BlockPos pos) {
		if (this.bombPos != null) {
			this.bombPos = pos;
		}
	}
	
	public BlockPos getBlockPos() {
		return this.bombPos;
	}
	
	@Override
    public void tick() {
        this.setBlockPos(this.bombPos);
    }
	
	protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(BlockBombEntity .class,
            EntityDataSerializers.INT);
	
	
	@Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity $$0) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
	
}
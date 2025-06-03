package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SoftAndWetExplosiveBubbleEntity extends SoftAndWetBubbleEntity{
    public SoftAndWetExplosiveBubbleEntity(EntityType<? extends SoftAndWetExplosiveBubbleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public void tick(){
        if (!this.level().isClientSide()) {
            lifeSpan--;
            if (lifeSpan <= 0 || (this.standUser == null || !(((StandUser) this.standUser).roundabout$getStandPowers() instanceof PowersSoftAndWet))) {
                popBubble();
                return;
            }
        }
        super.tick();
        if (!this.level().isClientSide()){
            ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_TRAIL,
                    this.getX(), this.getY() + this.getBbHeight()/2, this.getZ(),
                    0, 0, 0,0, 0.015);
        }
    }

    public SoftAndWetExplosiveBubbleEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.EXPLOSIVE_BUBBLE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public void popBubble(){

        this.level().playSound(null, this.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_POP_EVENT,
                SoundSource.PLAYERS, 2F, (float)(0.98+(Math.random()*0.04)));
        if (!this.level().isClientSide()){
            ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                    this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                    1, 0, 0,0, 0.015);
        }
        this.discard();
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState($$0.getBlockPos())),
                    $$0.getLocation().x, $$0.getLocation().y, $$0.getLocation().z,
                    30, 0.2, 0.05, 0.2, 0.3);
        }
        popBubble();
    }
    public int lifeSpan = 0;
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide()) {
            if (!($$0.getEntity() instanceof SoftAndWetBubbleEntity)) {
                if (!(MainUtil.isMobOrItsMounts($$0.getEntity(),getOwner())) && !MainUtil.isCreativeOrInvincible($$0.getEntity())){

                }
            }
        }
    }
}

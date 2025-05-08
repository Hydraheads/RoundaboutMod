package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.UnburnableProjectile;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SoftAndWetBubbleEntity extends AbstractHurtingProjectile implements UnburnableProjectile {
    public SoftAndWetBubbleEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected SoftAndWetBubbleEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }
    @Override
    protected float getInertia() {
        return 1F;
    }
    @Override
    public boolean isPickable() {
        return true;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

}

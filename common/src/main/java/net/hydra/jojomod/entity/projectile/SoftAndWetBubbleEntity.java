package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.UnburnableProjectile;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SoftAndWetBubbleEntity extends AbstractHurtingProjectile implements UnburnableProjectile {
    public SoftAndWetBubbleEntity(EntityType<? extends SoftAndWetBubbleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected SoftAndWetBubbleEntity(EntityType<? extends SoftAndWetBubbleEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    @Override
    public float getPickRadius() {
        return 0.0F;
    }
    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }
    public static float eWidth=0.7f;
    public static float eHeight=0.7f;
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(eWidth, eHeight); // Width, Height
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

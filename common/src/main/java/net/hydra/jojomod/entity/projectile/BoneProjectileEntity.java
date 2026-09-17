package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BoneProjectileEntity extends AbstractArrow {

    public BoneProjectileEntity(EntityType<? extends BoneProjectileEntity> type, Level level) {
        super(type, level);
    }

    public BoneProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BONE_PROJECTILE, shooter, level);
    }

    public BoneProjectileEntity(Level level, double x, double y, double z) {
        super(ModEntities.BONE_PROJECTILE, x, y, z, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Shatters upon hitting a block without sticking into it
        shatter(result.getLocation());
    }

    private void shatter(Vec3 pos) {
        if (!this.level().isClientSide()) {
            //remove the entity
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide() && result.getEntity() instanceof LivingEntity target) {
            // Stand damage
            DamageHandler.StandDamageEntity(target, 1.5F, this.getOwner() != null ? this.getOwner() : this);
            this.playSound(SoundEvents.SKELETON_HURT, 1.0F, 1.2F);
            this.discard(); // Bone shatters on impact
        }
    }
}
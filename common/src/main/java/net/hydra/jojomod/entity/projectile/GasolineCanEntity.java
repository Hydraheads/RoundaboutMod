package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Set;

public class GasolineCanEntity extends ThrowableItemProjectile {

    public float spinningCanX = 0;
    public float spinningCanXo = 0;
    public boolean done;
    public float bounces = 3;
    public GasolineCanEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GasolineCanEntity(LivingEntity living, Level $$1) {
        super(ModEntities.GASOLINE_CAN, living, $$1);
    }

    @Override
    public void tick(){
        int spincount = 0;
        if (bounces <= 3){spincount = -15;}
        spinningCanX = Mth.wrapDegrees(spinningCanX+=spincount);
        super.tick();
    }
    @Override
    protected Item getDefaultItem() {
        return ModItems.GASOLINE_CAN;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        bounces--;
        if (bounces < 0 || !$$0.getDirection().equals(Direction.UP)) {
            super.onHitBlock($$0);

            if (!done){
                SoundEvent $$6 = ModSounds.CAN_BOUNCE_END_EVENT;
                this.playSound($$6, 1F, 1F);

                scatterGoo($$0.getBlockPos());
            }

            this.discard();
        } else {
            SoundEvent $$6 = ModSounds.CAN_BOUNCE_EVENT;
            float volume = 1F;
            float pitch = 1F;
            if (bounces < 1){
                done=true;
                $$6 = ModSounds.CAN_BOUNCE_END_EVENT;
            } else {
                if (bounces == 1){
                    volume = 0.9F;
                    pitch = 0.9F;
                }
            }
            scatterGoo($$0.getBlockPos());
            this.playSound($$6, volume, pitch);
            this.setDeltaMovement(this.getDeltaMovement().x*0.9, 0.18+(0.04*bounces), this.getDeltaMovement().z*0.9);

        }
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        float $$2 = 2.0f;

        Entity $$4 = this.getOwner();

        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.MATCH, $$4);

        SoundEvent $$6 = SoundEvents.FIRE_EXTINGUISH;
        Vec3 DM = $$1.getDeltaMovement();

        if ($$1.getType() == EntityType.TNT_MINECART) {
            DamageSource DS = $$1.damageSources().explosion($$1, $$0.getEntity());
            ((IMinecartTNT)$$1).roundabout$explode(DS, this.getDeltaMovement().lengthSqr());
            this.discard();
        } else if ($$1.hurt($$5, $$2)) {
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if ($$1 instanceof LivingEntity $$7) {
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
            }
            this.playSound($$6, 0.8F, 1.6F);
            scatterGoo($$0.getEntity().getOnPos());
            this.discard();
        }

    }


    public void scatterGoo(BlockPos pos){
        if (!this.level().isClientSide) {
            if (bounces == 2 || bounces == 1) {
                setGoo(pos, 0, 0, 0);

                setGoo(pos, 1, 0, 1);
                setGoo(pos, -1, 0, 1);
                setGoo(pos, 0, 1, 1);
                setGoo(pos, 0, -1, 1);

                setGoo(pos, 2, 0, 2);
                setGoo(pos, -2, 0, 2);
                setGoo(pos, 0, 2, 2);
                setGoo(pos, 0, -2, 2);

                setGoo(pos, 1, 1, 2);
                setGoo(pos, -1, 1, 2);
                setGoo(pos, 1, -1, 2);
                setGoo(pos, -1, -1, 2);
            } else if (bounces == 0) {
                setGoo(pos, 0, 0, 1);

                setGoo(pos, 1, 0, 2);
                setGoo(pos, -1, 0, 2);
                setGoo(pos, 0, 1, 2);
                setGoo(pos, 0, -1, 2);
            }
        }
    }

    public void setGoo(BlockPos pos, int offsetX, int offsetZ, int level){
        BlockPos blockPos = null;
        if (canPlaceGoo(pos, offsetX, +1, offsetZ)) {
            blockPos = new BlockPos(pos.getX() + offsetX, pos.getY() + 1, pos.getZ() + offsetZ);
        } else if (canPlaceGoo(pos, offsetX, +2, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY() + 2,pos.getZ()+offsetZ);
        } else if (canPlaceGoo(pos, offsetX, 0, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY(),pos.getZ()+offsetZ);
        } else if (canPlaceGoo(pos, offsetX, -1, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY()-1,pos.getZ()+offsetZ);
        }
        //if (this.level().getBlockState(pos).getBlock())
        if (blockPos != null) {
            this.level().setBlockAndUpdate(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), ModBlocks.GASOLINE_SPLATTER.defaultBlockState().setValue(ModBlocks.GAS_CAN_LEVEL, Integer.valueOf(level)));
        }
    }
    //this.level().setBlock(new BlockPos(pos.getX() + offsetX, pos.getY(), pos.getZ() + offsetZ), ModBlocks.GASOLINE_SPLATTER.defaultBlockState(), 1);

    public boolean canPlaceGoo(BlockPos pos, int offsetX, int offsetY, int offsetZ){
        BlockPos blk =  new BlockPos(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

        if (this.level().isEmptyBlock(blk)) {
            BlockPos $$8 = blk.below();
            if (this.level().getBlockState($$8).isFaceSturdy(this.level(), $$8, Direction.UP)) {
                return true;
            }
        }

        return false;
    }


    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }


    protected float getGravity() {
        return 0.04F;
    }

}


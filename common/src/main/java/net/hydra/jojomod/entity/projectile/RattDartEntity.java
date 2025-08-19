package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class RattDartEntity extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.BOOLEAN);
    public static final Vec3 ShootOffset = new Vec3(0.45,-0.65,-0.45);
    private int superThrowTicks = -1;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            this.getEntityData().define(ROUNDABOUT$SUPER_THROWN, false);
        }
    }

    int charged = 0;

    public RattDartEntity(EntityType<? extends RattDartEntity> entity,  Level world) {
        super(entity, world);
    }

    public RattDartEntity(Level world, LivingEntity player,int i) {
        super(ModEntities.RATT_DART, player, world);
        if ( ((StandUser) player).roundabout$getStandPowers() instanceof PowersRatt PR) {
            if (PR.getStandEntity(player) instanceof RattEntity RE) {
                Vec2 v = new Vec2((float) (-1*Math.cos(RE.getStandRotationY())),
                        (float) (-1*Math.sin(RE.getStandRotationY())) );
                this.setPos(RE.getEyePosition(0).add(
                        ShootOffset.x*v.y,
                        ShootOffset.y,
                        ShootOffset.z*v.x
                ));
            }
        }
        charged = i;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        this.DisableSuperThrow();
        super.onHitBlock($$0);
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
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void EnableSuperThrow() {
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        int ticks = 0;
        for (int b=PowersRatt.ShotThresholds.length-1;b>=0;b--) {
            if (this.charged >= PowersRatt.ShotThresholds[b]) {
                ticks = PowersRatt.ShotSuperthrowTicks[b];
                break;
            }
        }
        superThrowTicks = ticks;
    }
    public void DisableSuperThrow() {
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
        superThrowTicks = 0;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.RATT_DART_THUNK_EVENT;
    }
    public void applyEffect(LivingEntity $$1) {
        int stack = 0;
        if ( ((LivingEntity)$$1).getEffect(ModEffects.MELTING) != null) {
            stack = ((LivingEntity) $$1).getEffect(ModEffects.MELTING).getAmplifier() + 1;
        }
        stack += charged >= PowersRatt.ShotThresholds[1] ? 1 : 0;
        ((LivingEntity)$$1).addEffect(new MobEffectInstance(ModEffects.MELTING, 900, stack),this);
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        float $$2 = 2.29F;

        if ($$1 instanceof LivingEntity $$3) {
            StandPowers entityPowers = ((StandUser) $$3).roundabout$getStandPowers();
            if (entityPowers != null ) {
                if (entityPowers.dealWithProjectile(this, $$0)) {
                    this.discard();
                    return;
                }
            }

            int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, $$3);
            float pow = 0;
            for (int b=PowersRatt.ShotDamageTicks.length-1;b>=0;b--) {
                if (this.charged >= PowersRatt.ShotDamageTicks[b]) {
                    pow = PowersRatt.ShotDamageTicks[b];
                    break;
                }
            }
            $$2 = (float) ($$2 * (pow-(f*0.03)));


        }

        Entity $$4 = this.getOwner();
        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.MELTING, $$4);
        SoundEvent $$6 = ModSounds.RATT_DART_IMPACT_EVENT;
        if ($$1.hurt($$5, $$2)) {


            if ($$4 instanceof LivingEntity LE) {
                LE.setLastHurtMob($$1);
            }

            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }


            if ($$1 instanceof LivingEntity $$7) {
                applyEffect($$7);
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
                if ($$4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects($$7, $$4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) $$4, $$7);
                }

                this.doPostHurtEffects($$7);
            }
            this.playSound($$6, 1.0F, (this.random.nextFloat() * 0.2F + 0.9F));
        }
        this.discard();

    }

    @Override
    public void tick(){
        Vec3 delta = this.getDeltaMovement();
        if (inGroundTime >= 160) {
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
        if (this.getEntityData().get(ROUNDABOUT$SUPER_THROWN)) {
            this.setDeltaMovement(delta);
        }
        if (!this.level().isClientSide()) {
            if (superThrowTicks > -1) {
                superThrowTicks--;
                if (superThrowTicks <= -1) {
                    this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
                } else {
                    if ((this.tickCount+2) == 0){
                        ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                this.getX(), this.getY(), this.getZ(),
                                0, 0, 0, 0, 0);
                    }
                }
            }
        }
    }





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
            this.level().setBlockAndUpdate(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), ModBlocks.FLESH_BLOCK.defaultBlockState().setValue(ModBlocks.FLESH_LAYER, Integer.valueOf(level)));
        }
    }



    public void placeFlesh(BlockPos pos, int amount) {
        int[][] array = {
                {0,0,0},
                {0,1,0},
                {0,0,0}
        };
        for (int i=0;i<amount;i++) {array[(int) (Math.random()*3) ][(int) (Math.random()*3)] += 1;}

        for (int x=0;x<array.length;x++) {
            for (int y=0;y<array[0].length;y++) {
                if (array[x][y] > 0) {
                    setGoo(pos, x, y, array[x][y]);
                }
            }
        }
    }

}

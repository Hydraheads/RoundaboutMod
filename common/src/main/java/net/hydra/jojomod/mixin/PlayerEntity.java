package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntity extends LivingEntity implements IPlayerEntity{


    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT_POS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Byte> DATA_KNIFE_COUNT_ID = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Shadow
    @Final
    private Inventory inventory;

    protected PlayerEntity(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public Inventory roundaboutGetInventory(){
        return inventory;
    }

    /**Keep track of unique player animations like floating*/
    public void roundaboutSetPos(byte Pos){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT_POS, Pos);
    }
    public byte roundaboutGetPos(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT_POS);
    }

    /**if your stand guard is broken, disable shields. Also, does not run takeshieldhit code if stand guarding.*/
    @Inject(method = "blockUsingShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutTakeShieldHit(LivingEntity $$0, CallbackInfo ci) {
        if (((StandUser) this).isGuarding()) {
            if (((StandUser) this).getGuardBroken()){

                ItemStack itemStack = ((LivingEntity) (Object) this).getUseItem();
                Item item = itemStack.getItem();
                if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                    ((LivingEntity) (Object) this).releaseUsingItem();
                    ((Player) (Object) this).stopUsingItem();
                }
                ((Player) (Object) this).getCooldowns().addCooldown(Items.SHIELD, 100);
                ((Player) (Object) this).level().broadcastEntityEvent(((Player) (Object) this), EntityEvent.SHIELD_DISABLED);
            }
            ci.cancel();
        } else if (((StandUser) $$0).getMainhandOverride()){
            ci.cancel();
        }
    }

    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "hurtCurrentlyUsedShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutDamageShield(float $$0, CallbackInfo ci) {
        if (((StandUser) this).isGuarding()) {
            ci.cancel();
        }
    }
    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutJump(CallbackInfo ci) {
        if (((StandUser) this).isClashing()) {
            ci.cancel();
        }
    }


    /**If you are in a barrage, does not play the hurt sound*/
    @Inject(method = "getHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutGetHurtSound(DamageSource $$0, CallbackInfoReturnable<SoundEvent> ci) {
        if (((StandUser) this).isDazed()) {
            ci.setReturnValue(null);
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty()) {
            if (((TimeStop) ((Player) (Object) this).level()).CanTimeStopEntity(((Player) (Object) this))) {
                ci.cancel();
            } else if ((((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this)))) {
                ((StandUser) this).setRoundaboutIdleTime(-1);
            }
        }

    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void RoundaboutTick2(CallbackInfo ci) {
        if (((StandUser)this).getAttackTimeDuring() > -1 || this.isUsingItem()) {
            ((StandUser) this).setRoundaboutIdleTime(-1);
        } else if (!new Vec3(this.getX(), this.getY(), this.getZ()).equals(new Vec3(this.xOld, this.yOld, this.zOld))) {
            ((StandUser) this).setRoundaboutIdleTime(-1);
        } else {
            ((StandUser) this).setRoundaboutIdleTime(((StandUser) this).getRoundaboutIdleTime() + 1);
        }
    }

    @Override
    @Unique
    public final int roundabout$getKnifeCount() {
        return this.entityData.get(DATA_KNIFE_COUNT_ID);
    }
    @Override
    @Unique
    public void roundabout$addKnife() {
        byte knifeCount = this.entityData.get(DATA_KNIFE_COUNT_ID);

        knifeCount++;
        if (knifeCount <= 12){
            ((LivingEntity) (Object) this).getEntityData().set(DATA_KNIFE_COUNT_ID, knifeCount);
        }
    }
    @Override
    @Unique
    public void roundabout$setKnife(byte knives) {
        ((LivingEntity) (Object) this).getEntityData().set(DATA_KNIFE_COUNT_ID, knives);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT_POS, PlayerPosIndex.NONE);
        ((LivingEntity)(Object)this).getEntityData().define(DATA_KNIFE_COUNT_ID, (byte)0);
    }

    @Shadow
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Shadow
    public ItemStack getItemBySlot(EquipmentSlot var1) {
        return null;
    }

    @Shadow
    public void setItemSlot(EquipmentSlot var1, ItemStack var2) {

    }

    @Shadow
    public HumanoidArm getMainArm() {
        return null;
    }
}

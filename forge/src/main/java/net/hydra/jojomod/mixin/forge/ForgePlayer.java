package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class ForgePlayer extends LivingEntity {



    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract float getDestroySpeed(BlockState $$0);
    @Shadow
    @Final
    private Inventory inventory;

    protected ForgePlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /**Block Breaking Speed Decreases when your hand is stone*/
    @Inject(method = "getDigSpeed", at = @At(value = "HEAD"), cancellable = true, remap = false)
    protected void roundabout$getForgeDestroySpeed(BlockState $$0, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        StandPowers powers = ((StandUser) this).roundabout$getStandPowers();
        GeneralPowers gp = ((IPowersPlayer)this).rdbt$getPowers();
        if (PowerTypes.hasStandActive(this) &&
                (powers.canUseMiningStand()) || powers.isMiningRegardless()|| PowerTypes.isBrawling(this)) {
            cir.setReturnValue(((IPlayerEntity)this).rdbt$mutualMiningSpeedFunction($$0,powers));
            return;
        }
        if (PowerTypes.isUsingPower(this) && ((IPowersPlayer)this).rdbt$getPowers().isMining()
                || PowerTypes.isBrawling(this)){
            cir.setReturnValue(((IPlayerEntity)this).rdbt$mutualMiningSpeedFunction2($$0,gp));
            return;
        }

        byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
        float f = this.inventory.getDestroySpeed($$0);
        boolean overwrite = false;
            if (curse > -1) {
                if ((curse == LocacacaCurseIndex.RIGHT_HAND && this.getMainArm() == HumanoidArm.RIGHT)
                        || (curse == LocacacaCurseIndex.LEFT_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                    if (f > 1.0F) {
                        overwrite = true;
                        int i = EnchantmentHelper.getBlockEfficiency(this);
                        ItemStack itemstack = this.getMainHandItem();
                        if (i > 0 && !itemstack.isEmpty()) {
                            f += (float) (i * i + 1);
                            f*= 0.6F;
                        }
                    }
                }
            }



        if (HeatUtil.isArmsFrozen(this)){
            f*=0.25f;
        }


        boolean active = ((StandUser) this).roundabout$getActive();
        if (active && PowerTypes.hasStandActivelyEquipped(this)){
            float bpow = ((StandUser)this).roundabout$getStandPowers().getBonusPassiveMiningSpeed();
                    if (bpow != 1){
                        f*= bpow;
                        overwrite = true;
                    }
        } else if (active){
            float bpow = ((IPowersPlayer)this).rdbt$getPowers().getBonusPassiveMiningSpeed();
            if (bpow != 1){
                f*= bpow;
                overwrite = true;
            }
        }

        if (!(PowerTypes.hasStandActive(this) &&
                ((((StandUser) this).roundabout$getStandPowers().canUseMiningStand())))) {
            float bpow = ((IFatePlayer) this).rdbt$getFatePowers().getBonusPassiveMiningSpeed();
            if (bpow != 1) {
                f *= bpow;
                overwrite = true;
            }
        }

        if (overwrite){
            if (f > 1.0F) {
                int i = EnchantmentHelper.getBlockEfficiency(this);
                ItemStack itemstack = this.getMainHandItem();
                if (i > 0 && !itemstack.isEmpty()) {
                    f += (float)(i * i + 1);
                }
            }
            if (MobEffectUtil.hasDigSpeed(this)) {
                f *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
            }

            if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                float f1;
                switch (this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                    case 0:
                        f1 = 0.3F;
                        break;
                    case 1:
                        f1 = 0.09F;
                        break;
                    case 2:
                        f1 = 0.0027F;
                        break;
                    case 3:
                    default:
                        f1 = 8.1E-4F;
                }

                f *= f1;
            }

            if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
                f /= 5.0F;
            }

            if (!this.onGround()) {
                f /= 5.0F;
            }

            if ($$0.is(Blocks.COBWEB)){
                f *= 5.0F;
            }

            f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(((Player)(Object)this), $$0, f, pos);


            cir.setReturnValue((float)(f));
        }
    }

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$playSound(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        Entity thrs = ((Entity) (Object)this);
        if(((ILevelAccess)this.level()).roundabout$isSoundPlunderedEntity(thrs)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).roundabout$getSoundPlunderedBubbleEntity(((Entity) (Object)this));
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds(soundEvent, this.getSoundSource(), f, g);
            }
            ci.cancel();
            return;
        }
        if (level().isClientSide()){
            if (PowerTypes.isInADifferentExistenceNoTE(thrs, ClientUtil.getPlayer())){
                ci.cancel();
                return;
            } else if (PowerTypes.isExistentiallyElsewhere(thrs)){
                ClientUtil.playSoundWithInfo(thrs.level(),
                        thrs.getX(),
                        thrs.getY(),
                        thrs.getZ(),
                        soundEvent,
                        this.getSoundSource(),
                        f,
                        g);
            }
        } else {
            if (PowerTypes.isExistentiallyElsewhere(thrs)){
                if (thrs.level() instanceof ServerLevel sl) {
                    if (soundEvent != null) {
                        ResourceLocation soundId = BuiltInRegistries.SOUND_EVENT.getKey(soundEvent);
                        String str = this.getSoundSource().name();
                        for (ServerPlayer playerInList :
                                sl.getServer().getPlayerList().getPlayers()) {

                            double range = soundEvent.getRange(g);
                            double rangeSqr = range * range;
                            if (playerInList.distanceToSqr(thrs) > rangeSqr) {
                                continue;
                            }

                            if (PowerTypes.isInADifferentExistenceNoTE(
                                    thrs,
                                    playerInList)) {
                                continue;
                            }

                            S2CPacketUtil.sendSafeSound(
                                    playerInList,
                                    thrs.getX(),
                                    thrs.getY(),
                                    thrs.getZ(),
                                    soundId.toString(),
                                    str,
                                    f,
                                    g
                            );
                        }
                    }
                }
                ci.cancel();
                return;
            }
        }
    }
    /**stand mining intercepts mining speed as well*/
    @Inject(method = "getDigSpeed", at = @At(value = "HEAD"), cancellable = true, remap = false)
    protected void roundabout$getForgeDestroySpeed2(BlockState $$0, BlockPos pos, CallbackInfoReturnable<Float> cir) {

    }

    @Shadow
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Shadow
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return null;
    }

    @Shadow
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Shadow
    public HumanoidArm getMainArm() {
        return null;
    }
}

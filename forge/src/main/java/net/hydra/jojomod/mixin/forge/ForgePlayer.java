package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
            byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
            if (curse > -1) {
                if ((curse == LocacacaCurseIndex.MAIN_HAND && this.getMainArm() == HumanoidArm.RIGHT)
                        || (curse == LocacacaCurseIndex.OFF_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                    float f = this.inventory.getDestroySpeed($$0);
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


                    boolean standActive = ((StandUser) this).roundabout$getActive();
                    if (standActive){
                        f*= ((StandUser)this).roundabout$getStandPowers().getBonusPassiveMiningSpeed();
                    }

                    cir.setReturnValue((float)(f*0.6));
                }
            }
    }

    /**stand mining intercepts mining speed as well*/
    @Inject(method = "getDigSpeed", at = @At(value = "HEAD"), cancellable = true, remap = false)
    protected void roundabout$getForgeDestroySpeed2(BlockState $$0, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        boolean standActive = ((StandUser) this).roundabout$getActive();
        StandPowers powers = ((StandUser) this).roundabout$getStandPowers();
        if (standActive) {
            if (((StandUser) this).roundabout$getStandPowers().canUseMiningStand()) {
                float mspeed;
                if (!$$0.is(BlockTags.MINEABLE_WITH_PICKAXE)){
                    if ($$0.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        mspeed = powers.getShovelMiningSpeed() / 2;

                    } else if ($$0.is(BlockTags.MINEABLE_WITH_AXE)){
                            mspeed= powers.getAxeMiningSpeed()/2;
                    } else {
                        mspeed= powers.getSwordMiningSpeed()/4;
                    }
                } else {
                    mspeed= powers.getPickMiningSpeed()*3;
                }


                if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
                    mspeed /= 5.0F;
                }

                if (!this.onGround()) {
                    mspeed /= 5.0F;
                }

                if (this.isCrouching() && $$0.getBlock() instanceof DropExperienceBlock) {
                    mspeed = 0.0F;
                }
                cir.setReturnValue(mspeed);
            }
        }
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

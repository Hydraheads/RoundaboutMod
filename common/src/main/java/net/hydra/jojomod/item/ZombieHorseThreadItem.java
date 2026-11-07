package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ZombieHorseThreadItem extends Item {
    public ZombieHorseThreadItem(Properties $$0) {
        super($$0);
    }

    private void stitchSelf(LivingEntity entity){
        if (!entity.hasEffect(ModEffects.BLEED)){
            if (!entity.hasEffect(ModEffects.STITCHED)){
                entity.heal(4);
            }else{
                entity.heal(2);
            }
        }else{
            MobEffectInstance bleed = entity.getEffect(ModEffects.BLEED);
            if (bleed != null) {
                int amp = bleed.getAmplifier();
                int dur = bleed.getDuration();
                entity.removeEffect(bleed.getEffect());
                entity.addEffect(new MobEffectInstance(ModEffects.STITCHED, dur, amp));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!($$3.getDamageValue() >= $$3.getMaxDamage())) {
            int getTrueHealth = Math.round($$1.getHealth());
            if (getTrueHealth < $$1.getMaxHealth() || $$1.hasEffect(ModEffects.BLEED)) {
                $$1.startUsingItem($$2);
                return InteractionResultHolder.consume($$3);
            }
        }else{
            return InteractionResultHolder.fail($$3);
        }
        return InteractionResultHolder.fail($$3);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemstack, Level level, LivingEntity entity) {
        ItemStack $$3 = super.finishUsingItem(itemstack, level, entity);
        if (!level.isClientSide && entity.isAlive()) {
            if (!(itemstack.getDamageValue() >= itemstack.getMaxDamage())) {
                if (entity.getHealth() < entity.getMaxHealth() || entity.hasEffect(ModEffects.BLEED)) {
                    stitchSelf(entity);
                    if (entity instanceof Player PE) {
                        PE.getCooldowns().addCooldown(this, 60);
                    }
                    if (entity instanceof Player PE){
                        if (!PE.isCreative()){
                        itemstack.hurt(1,PE.level().getRandom(),(ServerPlayer) PE);
                        }
                    }

                    SoundEvent $$6 = ModSounds.GREEN_DAY_STITCH_EVENT;
                    level.playSound(null, entity, $$6, SoundSource.PLAYERS, 1.0F, 1F);
                }
            }
        }
        return $$3;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
}

package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
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
            entity.heal(3);
        }else{
            MobEffectInstance bleed = entity.getEffect(ModEffects.BLEED);
            if (bleed != null) {
                if (bleed.getAmplifier() > 0) {
                    int amp = bleed.getAmplifier() - 1;
                    entity.removeEffect(bleed.getEffect());
                    entity.addEffect(new MobEffectInstance(ModEffects.BLEED, bleed.getDuration(), amp));
                } else {
                    entity.removeEffect(bleed.getEffect());
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!($$3.getDamageValue() >= $$3.getMaxDamage())) {
            if ((int) $$1.getHealth() < (int) $$1.getMaxHealth() || $$1.hasEffect(ModEffects.BLEED)) {
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
                        itemstack.hurt(1,PE.level().getRandom(),(ServerPlayer) PE);
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

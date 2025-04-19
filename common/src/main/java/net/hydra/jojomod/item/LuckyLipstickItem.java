package net.hydra.jojomod.item;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

public class LuckyLipstickItem extends Item implements Vanishable {
    public LuckyLipstickItem(Item.Properties $$0) {
        super($$0.defaultDurability(6));
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$0.playSound((Player)null, $$1.getX(), $$1.getY(), $$1.getZ(), ModSounds.CINDERELLA_SPARKLE_EVENT, SoundSource.NEUTRAL, 1F, 1F);
        $$1.getCooldowns().addCooldown(this, 50);

        if (!$$0.isClientSide) {
            if (!$$1.hasEffect(ModEffects.FACELESS)) {
                ((StandUser) $$1).roundabout$setGlow((byte) 2);
                ((LivingEntity) $$1).addEffect(new MobEffectInstance(ModEffects.CAPTURING_LOVE, 3600, 0, false, true), null);
            }
        }
        $$1.awardStat(Stats.ITEM_USED.get(this));
        if (!$$1.getAbilities().instabuild) {
            if (!$$0.isClientSide) {
                if (!$$1.hasEffect(ModEffects.FACELESS)) {
                    $$3.hurtAndBreak(1, $$1, ($$1x) -> {
                        $$1x.broadcastBreakEvent($$2);
                    });
                }
            }
        }

        return InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide());
    }
}

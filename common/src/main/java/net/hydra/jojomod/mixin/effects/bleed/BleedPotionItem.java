package net.hydra.jojomod.mixin.effects.bleed;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class BleedPotionItem {

    @Inject(method = "finishUsingItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$finishUsingItem(ItemStack $$0, Level $$1, LivingEntity $$2, CallbackInfoReturnable<ItemStack> cir) {
        if (!$$1.isClientSide() && $$2 != null && $$1 instanceof ServerLevel sl){
            if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.hexTwoSealsPotions) {
                MobEffectInstance mi = $$2.getEffect(ModEffects.BANISH);
                if (mi != null) {

                    Player $$3 = $$2 instanceof Player ? (Player) $$2 : null;
                    if ($$3 instanceof ServerPlayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) $$3, $$0);
                    }

                    if (!$$1.isClientSide) {
                        $$1.playSound(null, $$2.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1F, (float) (1 + (Math.random() * 0.04)));
                        sl.sendParticles(ParticleTypes.SMOKE, $$2.getEyePosition().x,
                                $$2.getEyePosition().y, $$2.getEyePosition().z,
                                6,
                                0.35,
                                0.35,
                                0.35,
                                0.01);
                    }

                    if ($$3 != null) {
                        $$3.awardStat(Stats.ITEM_USED.get((PotionItem) (Object) this));
                        if (!$$3.getAbilities().instabuild) {
                            $$0.shrink(1);
                        }
                    }

                    if ($$3 == null || !$$3.getAbilities().instabuild) {
                        if ($$0.isEmpty()) {
                            cir.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
                            return;
                        }

                        if ($$3 != null) {
                            $$3.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                        }
                    }

                    $$2.gameEvent(GameEvent.DRINK);
                    cir.setReturnValue($$0);
                }
            }
        }
    }
}

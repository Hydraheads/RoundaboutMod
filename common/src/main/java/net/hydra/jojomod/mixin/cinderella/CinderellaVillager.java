package net.hydra.jojomod.mixin.cinderella;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= Villager.class)
public abstract class CinderellaVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
    /**Makes lucky lipstick and faceless influence trading*/

    @Inject(method = "updateSpecialPrices(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "TAIL"),cancellable = true)
    private void roundabout$updateSpecialPrices(Player $$0, CallbackInfo ci) {
        if (!$$0.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            if ($$0.hasEffect(ModEffects.FACELESS)) {
                MobEffectInstance mi = $$0.getEffect(ModEffects.FACELESS);
                if (mi != null) {
                    int amp = mi.getAmplifier();
                    for (MerchantOffer $$5 : this.getOffers()) {
                        double $$6 = 0.14 + 0.1 * (double) amp;
                        int $$7 = (int) Math.floor($$6 * (double) $$5.getBaseCostA().getCount());
                        $$5.addToSpecialPriceDiff(Math.max($$7, 1));
                    }
                }
            } else if ($$0.hasEffect(ModEffects.CAPTURING_LOVE)) {
                MobEffectInstance mi = $$0.getEffect(ModEffects.CAPTURING_LOVE);
                if (mi != null) {
                    int amp = mi.getAmplifier();
                    for (MerchantOffer $$5 : this.getOffers()) {
                        double $$6 = 0.14 + 0.1 * (double) amp;
                        int $$7 = (int) Math.floor($$6 * (double) $$5.getBaseCostA().getCount());
                        $$5.addToSpecialPriceDiff(-Math.max($$7, 1));
                    }
                }
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    public CinderellaVillager(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow @Final private GossipContainer gossips;
}

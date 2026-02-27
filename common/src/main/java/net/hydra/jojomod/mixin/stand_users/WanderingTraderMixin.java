package net.hydra.jojomod.mixin.stand_users;

import net.hydra.jojomod.util.loot.WanderingTrades;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Pair;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin
        extends AbstractVillager {

    @Shadow
    protected abstract void updateTrades();

    @Unique
    private float pick = -1;

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> $$0, Level $$1) {super($$0, $$1);}


    @Inject(method = "<init>",at = @At(value = "RETURN"))
    private void roundabout$updateTrades(EntityType $$0, Level $$1, CallbackInfo ci) {
        WanderingTrades.updateTrades(Config.getServerInstance());
    }

    @Inject(method = "aiStep",at = @At(value = "HEAD"))
    public void roundabout$makeTraderStand(CallbackInfo ci) {



        /// does the same as the code below, but only check if it's a broken arrow trade
        /// also generates the pick for the code below, this is done to give the trader a stand
        /// the main thing is that the trades don't generate until you first click on them
        WanderingTrader This = (WanderingTrader) (Object) this;
        if (!This.level().isClientSide() && (offers == null) ) {
            float WTS = VillagerTrades.WANDERING_TRADER_TRADES.get(2).length;
            float maxChance = WTS;
            for (Pair<Float, MerchantOffer> f : WanderingTrades.TRADES) {maxChance += f.getA();}
            pick = This.getRandom().nextFloat() * maxChance;
          //  Roundabout.LOGGER.info("Pick: " + pick);
         //   Roundabout.LOGGER.info("WTS: " + WTS);


            if (pick > WTS) {
                for(int i=0;i<WanderingTrades.TRADES.size();i++) {
                    Pair<Float,MerchantOffer> pair = WanderingTrades.TRADES.get(i);
                    if (pick < pair.getA()) {
                        if (pair.getB().equals(WanderingTrades.BROKEN_ARROW_TRADE) && ConfigManager.getConfig().wanderingTraderSettings.brokenArrowsHaveStands ) {
                            StandArrowItem.grantStand(StandArrowItem.randomizeStand().getDefaultInstance(),This);
                            ((StandUser)This).roundabout$setActive(false);
                        }
                        break;
                    } else {
                        pick -= pair.getA();
                    }
                }
            }
            /// this has the potential to cause an issue by running getOffers before a player is to interact with them, but I doubt it will matter
            this.getOffers();

        }

    }


    @Inject(method = "updateTrades",at = @At(value = "TAIL"))
    public void roundabout$addWanderingTrades(CallbackInfo ci) {
        WanderingTrader This = (WanderingTrader) (Object) this;
        if (This.level().isClientSide()) {return;}
        MerchantOffers offers = This.getOffers();
        if (offers == null) {return;}
        Config cf = ConfigManager.getConfig();


        /// effectively what this does is make a number that correlates to the number of trades
        ///  since I'm not controlling the vanilla trades it only activates if it selects a modded trade
        ///  by changing the value it makes it more likely ex. changing it from 1 -> 2 correlates to 1/8 -> 2/9

        if (cf != null && !VillagerTrades.TRADES.isEmpty()) {
            float WTS = VillagerTrades.WANDERING_TRADER_TRADES.get(2).length;
            if (pick > WTS) {
                MerchantOffer offer = null;
                pick -= WTS;
                for (int i = 0; i< WanderingTrades.TRADES.size(); i++) {
                    Pair<Float,MerchantOffer> pair = WanderingTrades.TRADES.get(i);
                    if (pick < pair.getA()) {
                        offer = pair.getB();
                        break;
                    } else {
                        pick -= pair.getA();
                        //Roundabout.LOGGER.info("attempt" + i + ": " + pick);
                    }
                }

                if (offer != null) {
                    if (offer.equals(WanderingTrades.BROKEN_ARROW_TRADE)) {
                        ItemStack arrow = new ItemStack(ModItems.STAND_BEETLE_ARROW);
                        arrow.setDamageValue((int)(2+Math.random()*2));
                        offer = new MerchantOffer(offer.getBaseCostA(),arrow,1,1,1);
                    }
                 //   Roundabout.LOGGER.info("offer:" + offer.toString());
                    offers.set(offers.size()-1,offer);
                }

            }


        }
        pick = -1;
    }




}

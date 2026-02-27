package net.hydra.jojomod.util.loot;

import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.config.Config;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class WanderingTrades {

    public static List<Pair<Float, MerchantOffer>> TRADES = new ArrayList<>();
    public static MerchantOffer BEETLE_ARROW_TRADE;
    public static MerchantOffer BROKEN_ARROW_TRADE;
    public static MerchantOffer ANUBIS_TRADE;

    public static void updateTrades(Config cf) {
            TRADES = new ArrayList<>();
            BEETLE_ARROW_TRADE = new MerchantOffer(
                    new ItemStack(Items.EMERALD,cf.wanderingTraderSettings.beetleArrowCost),
                    new ItemStack(ModItems.STAND_ARROW)
                    ,1,1,1);
            BROKEN_ARROW_TRADE = new MerchantOffer(
                    new ItemStack(Items.EMERALD,cf.wanderingTraderSettings.brokenArrowCost),
                    new ItemStack(ModItems.STAND_BEETLE_ARROW)
                    ,1,1,1);
            ANUBIS_TRADE = new MerchantOffer(
                new ItemStack(Items.EMERALD,cf.wanderingTraderSettings.anubisTradeCost),
                new ItemStack(ModItems.ANUBIS_ITEM)
                ,1,1,1);

            TRADES.add(new Pair<>(cf.wanderingTraderSettings.beetleArrowTradeChance, BEETLE_ARROW_TRADE) );
            TRADES.add(new Pair<>(cf.wanderingTraderSettings.brokenArrowTradeChance, BROKEN_ARROW_TRADE) );
            TRADES.add(new Pair<>(cf.wanderingTraderSettings.anubisTradeChance, ANUBIS_TRADE) );
    }
}

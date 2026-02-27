package net.hydra.jojomod.Utils;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.registry.ForgeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GlobalLootModProvider extends GlobalLootModifierProvider {
    public GlobalLootModProvider(PackOutput output) {
        super(output, Roundabout.MOD_ID);
    }

    @Override
    protected void start(){
  /*      add("stand_arrow_from_pyramid", new ForgeSusSandItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build()
        }, ForgeItems.STAND_ARROW.get()));

        add("stand_arrow_from_warm_ocean", new ForgeSusSandItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/ocean_ruin_warm")).build()
        }, ForgeItems.STAND_ARROW.get()));

        add("stand_arrow_from_well", new ForgeSusSandItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_well")).build()
        }, ForgeItems.STAND_ARROW.get()));

        add("stand_arrow_from_cold_ocean", new ForgeSusGravelItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/ocean_ruin_cold")).build()
        }, ForgeItems.STAND_ARROW.get()));

        add("stand_arrow_from_trail_ruins", new ForgeSusGravelItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/trail_ruins_common")).build()
        }, ForgeItems.STAND_ARROW.get()));

        add("stand_arrow_from_trail_ruins_rare", new ForgeSusGravelItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/trail_ruins_rare")).build()
        }, ForgeItems.STAND_ARROW.get())); REMOVE THIS WITH FURTHER TESTING */

      /*  add("anubis_from_trail_ruins", new ForgeSusGravelItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/trail_ruins_common")).build()
        }, ForgeItems.ANUBIS_ITEM.get()));
        add("anubis_from_trail_ruins_rare", new ForgeSusGravelItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/trail_ruins_rare")).build()
        }, ForgeItems.ANUBIS_ITEM.get()));
        add("stand_arrow_from_pyramid", new ForgeSusSandItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build()
        }, ForgeItems.ANUBIS_ITEM.get())); */ // ADD FOR ANUBIS


        add("shipwreck_locacaca_pit", new ForgeItemModifiers(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.15F).build()
        }, ForgeItems.LOCACACA_PIT.get()));
    }
}

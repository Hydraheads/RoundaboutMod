package net.hydra.jojomod.Utils;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.registry.ForgeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GlobalLootModProvider extends GlobalLootModifierProvider {
    public GlobalLootModProvider(PackOutput output) {
        super(output, Roundabout.MOD_ID);
    }

    @Override
    protected void start(){
        add("stand_arrow_from_pyramid", new ForgeSusSandItemModifier(new LootItemCondition[]{
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
        }, ForgeItems.STAND_ARROW.get()));
    }
}

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



        add("shipwreck_locacaca_pit", new ForgeItemModifiers(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.15F).build()
        }, ForgeItems.LOCACACA_PIT.get()));
    }
}

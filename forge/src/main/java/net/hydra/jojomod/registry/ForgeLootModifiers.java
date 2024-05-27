package net.hydra.jojomod.registry;

import com.mojang.serialization.Codec;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.Utils.ForgeItemModifiers;
import net.hydra.jojomod.Utils.ForgeSusGravelItemModifier;
import net.hydra.jojomod.Utils.ForgeSusSandItemModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Roundabout.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_SUS_SAND_ITEM =
            LOOT_MODIFIERS.register("add_sus_sand_item", ForgeSusSandItemModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_SUS_GRAVEL_ITEM =
            LOOT_MODIFIERS.register("add_sus_gravel_item", ForgeSusGravelItemModifier.CODEC);
}

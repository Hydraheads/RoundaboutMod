package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class FabricEffects extends MobEffect{

    public static final MobEffect BLEED = register("bleed", new FabricEffects(MobEffectCategory.HARMFUL, 11994666));
    public static final MobEffect HEX = register("hex", new FabricEffects(MobEffectCategory.HARMFUL, 11606258)); //old color 16762706

    protected FabricEffects(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    private static MobEffect register(String $$1, MobEffect $$2) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Roundabout.MOD_ID, $$1), $$2);
    }


    public static void register(){
        ModEffects.BLEED = BLEED;
        ModEffects.HEX = HEX;
    }
}

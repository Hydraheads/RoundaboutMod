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
    public static final MobEffect STAND_VIRUS = register("stand_virus", new FabricEffects(MobEffectCategory.HARMFUL, 9979490));
    public static final MobEffect CAPTURING_LOVE = register("capturing_love", new FabricEffects(MobEffectCategory.BENEFICIAL, 16772988));
    public static final MobEffect FACELESS = register("faceless", new FabricEffects(MobEffectCategory.HARMFUL, 10329495));

    protected FabricEffects(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    private static MobEffect register(String $$1, MobEffect $$2) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Roundabout.MOD_ID, $$1), $$2);
    }


    public static void register(){
        ModEffects.BLEED = BLEED;
        ModEffects.HEX = HEX;
        ModEffects.STAND_VIRUS = STAND_VIRUS;
        ModEffects.CAPTURING_LOVE = CAPTURING_LOVE;
        ModEffects.FACELESS = FACELESS;
    }
}

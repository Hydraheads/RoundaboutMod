package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeEffects{

    public static final DeferredRegister<MobEffect> POTION_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Roundabout.MOD_ID);
    public static final RegistryObject<MobEffect> BLEED =
            POTION_EFFECTS.register("bleed", () ->
                    new Effect(MobEffectCategory.HARMFUL, 11994666)
            );
    public static final RegistryObject<MobEffect> HEX =
            POTION_EFFECTS.register("hex", () ->
                    new Effect(MobEffectCategory.HARMFUL, 11606258)
            );
    public static final RegistryObject<MobEffect> STAND_VIRUS =
            POTION_EFFECTS.register("stand_virus", () ->
                    new Effect(MobEffectCategory.HARMFUL, 9979490)
            );
    public static final RegistryObject<MobEffect> CAPTURING_LOVE =
            POTION_EFFECTS.register("capturing_love", () ->
                    new Effect(MobEffectCategory.BENEFICIAL, 16772988)
            );
    public static final RegistryObject<MobEffect> FACELESS =
            POTION_EFFECTS.register("faceless", () ->
                    new Effect(MobEffectCategory.HARMFUL, 10329495)
            );
    public static final RegistryObject<MobEffect> MELTING =
            POTION_EFFECTS.register("melting", () ->
                    new Effect(MobEffectCategory.HARMFUL, 10329495)
                            .addAttributeModifier(Attributes.MAX_HEALTH,"6107DE5E-7CE8-4030-940E-514C1F160890",-1.1, AttributeModifier.Operation.ADDITION)
            );


    public static class Effect extends MobEffect{
        public Effect(MobEffectCategory typeIn, int liquidColorIn) {

            super(typeIn, liquidColorIn);
        }

    }

}

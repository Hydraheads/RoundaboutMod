package net.hydra.jojomod.event.powers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageTypes {
        /*
         * Store the RegistryKey of our DamageType into a new constant called CUSTOM_DAMAGE_TYPE
         * The Identifier in use here points to our JSON file we created earlier.
         */
        public static final ResourceKey<DamageType> STAND = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("roundabout", "stand"));

        public static DamageSource of(Level world, ResourceKey<DamageType> key, Entity attacker) {
            return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker, attacker);
        }
}

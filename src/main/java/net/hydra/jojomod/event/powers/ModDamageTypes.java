package net.hydra.jojomod.event.powers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {
        /*
         * Store the RegistryKey of our DamageType into a new constant called CUSTOM_DAMAGE_TYPE
         * The Identifier in use here points to our JSON file we created earlier.
         */
        public static final RegistryKey<DamageType> STAND = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("roundabout", "stand"));

        public static DamageSource of(World world, RegistryKey<DamageType> key, Entity attacker) {
            return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), attacker, attacker);
        }
}

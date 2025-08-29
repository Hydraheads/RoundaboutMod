package net.hydra.jojomod.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Minecart;

public class GEntityTags {
    /**
     * It's a whitelist for the non-living/non-projectile/non-minecart entities that can change gravity.
     * <p>
     * The main reason that it's a whitelist instead of blacklist is that,
     * the portal entities of Immersive Portals cannot change gravity.
     * Both MiniScaled and PortalGun has their own portal entity type.
     * If it's a blacklist, the maintenance of the blacklist incur more work
     * as it has to consider many different kinds of non-living mod entities.
     * It's not favorable to let every mod add blacklist entity tag for this.
     */
    public static final TagKey<EntityType<?>> ALLOWED_SPECIAL = TagKey.create(
            BuiltInRegistries.ENTITY_TYPE.key(),
            new ResourceLocation("gravity_changer", "allowed_special")
    );

    public static boolean canChangeGravity(Entity entity) {

        if (entity instanceof LivingEntity ||
                entity instanceof Projectile ||
                entity instanceof Minecart
        ) {
            if (entity instanceof Shulker)
                return false;

            return true;
        }

        return entity.getType().builtInRegistryHolder().is(ALLOWED_SPECIAL);
    }

    public static boolean allowGravityTransformationInRendering(Entity entity) {
        return true;
    }
}

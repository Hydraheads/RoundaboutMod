package net.hydra.jojomod.mixin;

import com.google.common.collect.Maps;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
@Mixin(SpawnPlacements.class)
public class RegisterSpawnPlacements {

    @Shadow()
    @Final
    private static <T extends Mob> void register(EntityType<T> $$0, SpawnPlacements.Type $$1, Heightmap.Types $$2, SpawnPlacements.SpawnPredicate<T> $$3) {
    }

    static {
        register(ModEntities.TERRIER_DOG, SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TerrierEntity::canSpawn);
    }
}

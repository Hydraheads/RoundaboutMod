package com.example.jjf.mixin;

import com.example.jjf.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = Mob.class,
        priority = 1500
)
public class JJFMobStandPoolMixin {

    @Inject(
            method = "finalizeSpawn",
            at = @At("HEAD")
    )
    private void jjf$addJumpinJackFlashToMobPools(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            SpawnGroupData spawnData,
            CompoundTag tag,
            CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        if (ModItems.JJF_STAND_DISC == null) {
            return;
        }

        if (!net.hydra.jojomod.item.ModItems
                .STAND_ARROW_POOL_FOR_MOBS
                .contains(
                        ModItems.JJF_STAND_DISC
                )) {

            net.hydra.jojomod.item.ModItems
                    .STAND_ARROW_POOL_FOR_MOBS
                    .add(
                            ModItems.JJF_STAND_DISC
                    );
        }

        if (!net.hydra.jojomod.item.ModItems
                .STAND_ARROW_POOL_FOR_HUMANOID_MOBS
                .contains(
                        ModItems.JJF_STAND_DISC
                )) {

            net.hydra.jojomod.item.ModItems
                    .STAND_ARROW_POOL_FOR_HUMANOID_MOBS
                    .add(
                            ModItems.JJF_STAND_DISC
                    );
        }
    }
}
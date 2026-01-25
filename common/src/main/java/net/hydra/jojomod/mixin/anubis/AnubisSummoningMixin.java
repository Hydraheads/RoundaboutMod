package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class AnubisSummoningMixin {

    @Unique
    public int roundabout$inStructureTicks = 0;

    @Inject(method = "tick",at = @At(value = "TAIL"))
    private void roundabout$summonAnubis(CallbackInfo ci) {

        LivingEntity This = ((LivingEntity)(Object)this);

        if (This instanceof Player P) {
            if (This.level() instanceof ServerLevel SL) {
                if (This.hasEffect(MobEffects.BAD_OMEN)) {
                    Vec3 pos = P.getPosition(0F);
                    boolean inStructure = LocationPredicate.inStructure(BuiltinStructures.DESERT_PYRAMID).matches(SL, pos.x, pos.y, pos.z);
                    if (inStructure) {
                        this.roundabout$inStructureTicks += 1;
                        if (this.roundabout$inStructureTicks > 160) {
                            if (roundabout$spawn(SL,(ServerPlayer)P )) {
                                This.removeEffect(MobEffects.BAD_OMEN);
                                SL.playSound(null, This.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.AMBIENT, 1F, 1F);
                                this.roundabout$inStructureTicks = 0;
                            }
                        }

                    } else {
                        this.roundabout$inStructureTicks = 0;
                    }
                }
            }
        }
    }

    @Unique
    private boolean roundabout$spawn(ServerLevel serverLevel, ServerPlayer player) {


        BlockPos blockPos2 = player.blockPosition();
        int i = 48;
        PoiManager poiManager = serverLevel.getPoiManager();
        Optional<BlockPos> optional = poiManager.find(holder -> holder.is(PoiTypes.MEETING), blockPos -> true, blockPos2, 48, PoiManager.Occupancy.ANY);
        BlockPos blockPos22 = optional.orElse(blockPos2);
        BlockPos blockPos3 = this.roundabout$findSpawnPositionNear(serverLevel, blockPos22, 20);
        if (blockPos3 != null && this.roundabout$hasEnoughSpace(serverLevel, blockPos3)) {
            AnubisGuardian anubisGuardian = ModEntities.ANUBIS_GUARDIAN.spawn(serverLevel,blockPos3,MobSpawnType.TRIGGERED);
            anubisGuardian.addEffect(new MobEffectInstance(MobEffects.GLOWING,200));
            return true;
        }
        return false;
    }

    @Unique
    @Nullable
    private BlockPos roundabout$findSpawnPositionNear(LevelReader levelReader, BlockPos blockPos, int i) {
        BlockPos blockPos2 = null;
        for (int j = 0; j < 100; ++j) {
            int l;
            int m;
            int k = blockPos.getX() + ((LivingEntity)(Object)this).getRandom().nextInt(i * 2) - i;
            BlockPos blockPos3 = new BlockPos(k, m = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE, k, l = blockPos.getZ() + ((LivingEntity)(Object)this).getRandom().nextInt(i * 2) - i), l);
            if (!NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, levelReader, blockPos3, EntityType.WANDERING_TRADER)) continue;
            blockPos2 = blockPos3;
            break;
        }
        return blockPos2;
    }

    @Unique
    private boolean roundabout$hasEnoughSpace(BlockGetter blockGetter, BlockPos blockPos) {
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos, blockPos.offset(1, 2, 1))) {
            if (blockGetter.getBlockState(blockPos2).getCollisionShape(blockGetter, blockPos2).isEmpty()) continue;
            return false;
        }
        return true;
    }
}

///PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.STRONGHOLD)))
package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Raid.class)
public abstract class AnubisRaidMixin {


    @Shadow
    private BlockPos center;

    @Shadow
    public abstract Level getLevel();

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    private Optional<BlockPos> waveSpawnPos;

    @Shadow
    private int groupsSpawned;

    @Shadow
    public abstract void updateBossbar();

    @Shadow
    protected abstract void setDirty();


    @Shadow
    @Final
    private int numGroups;

    @Shadow
    public abstract int getGroupsSpawned();

    @Shadow
    public abstract void joinRaid(int i, Raider raider, @Nullable BlockPos blockPos, boolean bl);

    @Inject(method = "spawnGroup", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$replaceFinalWave(BlockPos $$0, CallbackInfo ci) {
        Roundabout.LOGGER.info(center.toString());
        Roundabout.LOGGER.info("" + !this.level.getBiome(this.center).value().hasPrecipitation());
        Roundabout.LOGGER.info(this.getGroupsSpawned() + " | " + (this.numGroups - 1));
        if (this.getGroupsSpawned() == this.numGroups - 1) {
            if (!this.level.getBiome(this.center).value().hasPrecipitation()) {
                Roundabout.LOGGER.info("SPAWN MF");

                AnubisGuardian anubisGuardian = ModEntities.ANUBIS_GUARDIAN.spawn(this.level, $$0, MobSpawnType.TRIGGERED);
                joinRaid(this.groupsSpawned + 1, anubisGuardian, $$0, false);
                anubisGuardian.addEffect(new MobEffectInstance(MobEffects.GLOWING, 400));


                this.waveSpawnPos = Optional.empty();
                ++this.groupsSpawned;
                this.updateBossbar();
                this.setDirty();
                ci.cancel();
            }
        }
    }
}

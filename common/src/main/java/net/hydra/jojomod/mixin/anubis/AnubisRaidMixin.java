package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow
    private float totalHealth;

    @Shadow
    protected abstract void updateRaiders();

    @Inject(method = "spawnGroup", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$replaceFinalWave(BlockPos $$0, CallbackInfo ci) {
        if (this.getGroupsSpawned() == 2) {
            if (!this.level.getBiome(this.center).value().hasPrecipitation()) {

                AnubisGuardian anubisGuardian = ModEntities.ANUBIS_GUARDIAN.spawn(this.level, $$0, MobSpawnType.TRIGGERED);
                joinRaid(this.groupsSpawned + 1, anubisGuardian, $$0, false);
                anubisGuardian.addEffect(new MobEffectInstance(MobEffects.GLOWING, 400));

                this.totalHealth = anubisGuardian.getMaxHealth();


                this.waveSpawnPos = Optional.empty();
                ++this.groupsSpawned;
                this.updateBossbar();
                this.setDirty();
                ci.cancel();
            }
        }
    }
}

package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieVillager.class)
public abstract class ZZombieVillager extends Zombie implements VillagerDataHolder {
    public ZZombieVillager(EntityType<? extends Zombie> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Unique
    public boolean roundabout$ceaseSound = false;

    /**Replace naturally spawned zombie villagers with Zombie Aestheticians*/
    @Inject(method = "getAmbientSound", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$playAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (roundabout$ceaseSound) {
            cir.setReturnValue(null);
        }
    }
    @Inject(method = "finalizeSpawn", at = @At(value = "HEAD"))
    protected void roundabout$finalizeSpawn(ServerLevelAccessor $$0, DifficultyInstance $$1, MobSpawnType $$2, SpawnGroupData $$3, CompoundTag $$4, CallbackInfoReturnable<SpawnGroupData> cir) {
        if ($$2.equals(MobSpawnType.NATURAL) || $$2.equals(MobSpawnType.CHUNK_GENERATION)) {
            float rand = this.level().random.nextFloat();
            if (rand < (ClientNetworking.getAppropriateConfig().percentOfZombieVillagersThatBecomeZombieAestheticians * 0.01)) {
                ZombieAesthetician $$7 = ModEntities.ZOMBIE_AESTHETICIAN.create(this.level());
                if ($$7 != null) {
                    $$7.setPos(this.getPosition(1));
                    this.level().addFreshEntity($$7);
                    this.discard();
                    roundabout$ceaseSound = true;
                }
            }
        }
    }
}

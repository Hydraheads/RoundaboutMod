package net.hydra.jojomod.mixin;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.ICreeper;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(Creeper.class)
public class ZCreeper extends Monster implements ICreeper {
    /**Minor code for stopping creepers in a barrage*/
    @Shadow
    private int oldSwell;
    @Shadow
    private int swell;
    @Unique
    private static final EntityDataAccessor<Boolean> roundabout$IS_TRANSFORMED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);

    protected ZCreeper(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }
    @Unique
    public boolean roundabout$isTransformed(){
        return this.getEntityData().get(roundabout$IS_TRANSFORMED);
    }
    public void roundabout$setTransformed(boolean transformed){
        this.getEntityData().set(roundabout$IS_TRANSFORMED, transformed);
    }

    @ModifyVariable(method = "spawnLingeringCloud()V", at = @At("STORE"), ordinal = 0)
    protected Collection<MobEffectInstance> roundabout$SpawnLingeringCloud(Collection<MobEffectInstance> col) {
        if (!col.isEmpty()) {
            Collection<MobEffectInstance> col2 = Lists.newArrayList();
            for (MobEffectInstance $$2 : col) {
                if (!$$2.getEffect().equals(ModEffects.BLEED)){
                    col2.add($$2);
                }
            }
            return col2;
        }
        return col;
    }
    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    protected void roundabout$DefineSyncedData(CallbackInfo ci) {
        this.entityData.define(roundabout$IS_TRANSFORMED, false);

    }
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {
        if (((StandUser)this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack())) {
            if (((Creeper)(Object)this).isAlive()) {
                oldSwell = swell;
            }

            this.swell -= 1;
            if (this.swell < 0) {
                this.swell = 0;
            }
            super.tick();
            ci.cancel();
        }
    }
}

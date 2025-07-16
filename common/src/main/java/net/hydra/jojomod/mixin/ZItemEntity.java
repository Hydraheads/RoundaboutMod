package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.FogBlockItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ZItemEntity extends Entity implements IItemEntityAccess {
    @Shadow
    private int pickupDelay;

    @Shadow
    private int age;

    @Unique
    public boolean roundabout$raidSparkle = false;

    @Shadow
    public ItemStack getItem() {
        return null;
    }

    @Shadow public abstract int getAge();

    @Override
    @Unique
    public void roundabout$setRaidSparkle(boolean sparkle){
        roundabout$raidSparkle = sparkle;
    }


    public ZItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(method = "setThrower", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$setThrower(@Nullable UUID $$0, CallbackInfo ci) {
        if ($$0 != null) {
            Player pl =  this.level().getPlayerByUUID($$0);
            if (pl != null){
                if (MainUtil.getEntityIsTrulyInvisible(pl) && ClientNetworking.getAppropriateConfig().achtungSettings.hidesShotProjectiles){
                    ((IEntityAndData)this).roundabout$setTrueInvisibility(MainUtil.getEntityTrulyInvisibleTicks(pl));
                }
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$universalTick();
        if (((IEntityAndData)this).roundabout$getNoGravTicks() > 0){
            ((IEntityAndData)this).roundabout$setNoGravTicks(((IEntityAndData)this).roundabout$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.04, 0.0));
            }
        }
        if (roundabout$raidSparkle){
            if (!this.level().isClientSide()){
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(3),
                        this.getRandomY(), this.getRandomZ(3),
                        4, 0.0, 0.5, 0.0, 0.35);
            }
        } else if (getItem().getItem() instanceof FogBlockItem){
            if (this.age < 5940){
                this.age = 5940;
            }

            if (!this.level().isClientSide()){
                if (this.getAge() % 3 == 0) {
                    ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, this.getRandomX(3),
                            this.getRandomY(), this.getRandomZ(3),
                            0, 0.0, 0.5, 0.0, 0.35);
                }
            }
        }
        ((IEntityAndData)this).roundabout$tickQVec();
    }

    @Override
    public void roundabout$TickPickupDelay(){
        if (this.getItem().isEmpty()) {
            this.discard();
        } else {
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                this.pickupDelay--;
            }
            if (!this.level().isClientSide && this.age >= 6000) {
                this.discard();
            } else if (this.age >= 5999){
                this.age++;
            }
        }

    }
}

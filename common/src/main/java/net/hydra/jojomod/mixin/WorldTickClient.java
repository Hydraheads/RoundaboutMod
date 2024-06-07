package net.hydra.jojomod.mixin;


import net.hydra.jojomod.access.IFishingRodAccess;
import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class WorldTickClient {

    /** Called every tick on the Client. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"))
    private void roundaboutTickEntity(Entity $$0, CallbackInfo ci) {

        if (!$$0.isRemoved()) {
            this.standTickCheck($$0);
            for (Entity entity2 : $$0.getPassengers()) {
                if (!entity2.isRemoved()) {
                    this.standTickCheck(entity2);
                }
            }
        }
    }

    private void standTickCheck(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    this.tickStandIn(livingEntity, stand);
                }
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity stand) {
        if (stand == null || stand.isRemoved() || stand.getUser() != entity) {
            return;
        }
        byte ot = stand.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            stand.setOldPosAndRot();
            ++stand.tickCount;
            stand.tickStandOut();
        }
    }

    /**Time Stop Code*/
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                float delta = Minecraft.getInstance().getDeltaFrameTime();
                if ($$0 instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) $$0;

                    ((ILivingEntityAccess) livingEntity).setAnimStepO(((ILivingEntityAccess) livingEntity).getAnimStep());
                    $$0.setOldPosAndRot();
                    livingEntity.yBodyRotO = livingEntity.yBodyRot;
                    livingEntity.yHeadRotO = livingEntity.yHeadRot;
                    livingEntity.oAttackAnim = livingEntity.attackAnim;
                    //livingEntity.lastLimbDistance = livingEntity.limbDistance;

                    int LS = ((ILivingEntityAccess) livingEntity).getLerpSteps();
                    if (LS > 0) {
                        double LX = livingEntity.getX() + (((ILivingEntityAccess) livingEntity).getLerpX() - livingEntity.getX()) / (double) LS;
                        double LY = livingEntity.getY() + (((ILivingEntityAccess) livingEntity).getLerpY() - livingEntity.getY()) / (double) LS;
                        double LZ = livingEntity.getZ() + (((ILivingEntityAccess) livingEntity).getLerpZ() - livingEntity.getZ()) / (double) LS;
                        ((ILivingEntityAccess) livingEntity).setLerpSteps(LS-1);
                        $$0.setPos(LX,LY,LZ);
                    }

                    ((ILivingEntityAccess) $$0).roundaboutPushEntities();
                } else {
                    $$0.walkDistO = $$0.walkDist;

                    $$0.xOld = $$0.getX();;
                    $$0.yOld = $$0.getY();
                    $$0.zOld = $$0.getZ();

                    if ($$0 instanceof FishingHook){
                        $$0.xo = $$0.getX();
                        $$0.yo = $$0.getY();
                        $$0.zo = $$0.getZ();
                    }
                }
                if ($$0 instanceof ItemEntity) {
                    ((IItemEntityAccess)$$0).RoundaboutTickPickupDelay();
                } else if ($$0 instanceof FishingHook){
                    ((IFishingRodAccess)$$0).roundaboutUpdateRodInTS();
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tickEntities", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity3(CallbackInfo ci) {
            ((TimeStop) this).tickTimeStoppingEntity();
    }

    @Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void roundaboutStopParticles(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) x, (int) y, (int) z))){
            ci.cancel();
        }
    }

    @Inject(method = "playLocalSound", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity2(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) $$0, (int) $$1, (int) $$2))){
            if (($$4 == SoundSource.WEATHER || $$4 == SoundSource.BLOCKS) && !$$3.getLocation().getPath().contains("break")) {
                ci.cancel();
            }
        }
    }
}

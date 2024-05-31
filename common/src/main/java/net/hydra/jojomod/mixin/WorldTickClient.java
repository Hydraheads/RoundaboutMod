package net.hydra.jojomod.mixin;


import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
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

                    $$0.setPos($$0.getPosition(delta));
                } else {
                    $$0.walkDistO = $$0.walkDist;
                    $$0.setOldPosAndRot();
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tickEntities", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity3(CallbackInfo ci) {
            ((TimeStop) this).tickTimeStoppingEntity();
    }
}

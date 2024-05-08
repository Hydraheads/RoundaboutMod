package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    /** Called every tick on the Client. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Inject(method = "tickEntity", at = @At(value = "TAIL"))
    private void tickEntity2(Entity entity, CallbackInfo ci) {

        if (!entity.isRemoved()) {
            this.standTickCheck(entity);
            for (Entity entity2 : entity.getPassengerList()) {
                if (!entity2.isRemoved()) {
                    this.standTickCheck(entity2);
                }
            }
        }
    }

    private void standTickCheck(Entity entity){
        if (entity.isLiving()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            StandUserComponent standUserData = MyComponents.STAND_USER.get(livingEntity);
            if (standUserData.getStand() != null) {
                StandEntity stand = standUserData.getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    this.tickStandIn(livingEntity, stand);
                }
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity stand) {
        if (stand == null || stand.isRemoved() || stand.getMaster() != entity) {
            return;
        }
        stand.resetPosition();
        ++stand.age;
        stand.tickStandOut();
    }

}

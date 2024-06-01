package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(Level.class)
public class TimeStopWorld implements TimeStop {
    /**Stropping time- uses a list of time stopping players.
     * Use that list to determine if a block or entity SHOULD be ticked.*/
    private ImmutableList<LivingEntity> timeStoppingEntities = ImmutableList.of();

    /**Adds an entity to the list of time stopping entities*/
    @Override
    public void addTimeStoppingEntity(LivingEntity $$0) {
        if (this.timeStoppingEntities.isEmpty()) {
            this.timeStoppingEntities = ImmutableList.of($$0);
        } else {
            if (!this.timeStoppingEntities.contains($$0)) {
                List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
                $$1.add($$0);
                this.timeStoppingEntities = ImmutableList.copyOf($$1);
            }
        }
        streamTimeStopToClients();
    }

    /**Adds an entity to the list of time stopping entities*/
    @Override
    public void removeTimeStoppingEntity(LivingEntity $$0) {

        if (!this.timeStoppingEntities.isEmpty()) {
            List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
            for (int i = this.timeStoppingEntities.size() - 1; i >= 0; --i) {
                if (this.timeStoppingEntities.get(i).getId() == $$0.getId()){
                    if (!((Level) (Object) this).isClientSide) {
                        streamTimeStopRemovalToClients(this.timeStoppingEntities.get(i));
                    }
                    $$1.remove(i);
                }
            }
            this.timeStoppingEntities = ImmutableList.copyOf($$1);
        }
    }


    /**Sends an array of packets to the client, of every time stopping entity*/
    @Override
    public void streamTimeStopToClients() {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (!this.timeStoppingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
                    for (int i = this.timeStoppingEntities.size() - 1; i >= 0; --i) {
                        Entity TSI = this.timeStoppingEntities.get(i);
                        /*You only need data of time stopping mobs that are relatively close by*/
                        if (MainUtil.cheapDistanceTo2(TSI.getX(),TSI.getZ(),serverPlayer.getX(),serverPlayer.getZ()) < 250){
                            ModPacketHandler.PACKET_ACCESS.timeStoppingEntityPacket(serverPlayer, TSI.getId(), false);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void streamTimeStopRemovalToClients(LivingEntity removedStoppingEntity) {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (!this.timeStoppingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    ModPacketHandler.PACKET_ACCESS.timeStoppingEntityPacket(serverPlayer, removedStoppingEntity.getId(), true);
                }
            }
        }
    }

    /**On the client side, takes streamed packets and adds/removes entities from them to the liste*/
    @Override
    public void processTSPacket(LivingEntity timeStoppingEntity, boolean removal){
        if (((Level) (Object) this).isClientSide) {
            if (removal) {
                removeTimeStoppingEntity(timeStoppingEntity);
            } else {
                addTimeStoppingEntity(timeStoppingEntity);
            }
        }
    }

    @Override
    public void tickTimeStoppingEntity() {
        if (!((Level) (Object) this).isClientSide) {
            streamTimeStopToClients();
        }
    }
    @Override
    public ImmutableList<LivingEntity> getTimeStoppingEntities() {
        return timeStoppingEntities;
    }

    @Override
    public boolean inTimeStopRange(Vec3i pos){
        if (!this.timeStoppingEntities.isEmpty()) {
            List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                LivingEntity it = $$1.get(i);
                if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).getStandPowers().getTimestopRange()) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean inTimeStopRange(Entity entity){
        return inTimeStopRange(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }

    /**Code this to check if they are able to use powers mid TS*/
    @Override
    public boolean CanTimeStopEntity(Entity entity){
        if (entity instanceof Player && ((Player) entity).isCreative() || entity.isSpectator()) {
            return false;
        } else if (entity instanceof Warden){
            return false;
        } else if (entity instanceof StandEntity) {
            LivingEntity Following = ((StandEntity) entity).getFollowing();
            if (Following != null && CanTimeStopEntity(Following)){
                return true;
            } else {
                return false;
            }
        } else if (entity instanceof LivingEntity){
            if (isTimeStoppingEntity((LivingEntity) entity)){
                return false;
            } else {
                return inTimeStopRange(entity);
            }
        } else {
            return inTimeStopRange(entity);
        }
    }

    /**Code this to check if they are in the list*/
    @Override
    public boolean isTimeStoppingEntity(LivingEntity entity){
        if (!this.timeStoppingEntities.isEmpty()) {
            List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
            for (int i = $$1.size() - 1; i >= 0; --i) {
               if ($$1.get(i).getId() == entity.getId()){
                   return true;
               }
            }
        }
        return false;
    }

    @Inject(method="shouldTickBlocksAt(Lnet/minecraft/core/BlockPos;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickBlocksAt(BlockPos $$0, CallbackInfoReturnable<Boolean> ci) {
        if (inTimeStopRange($$0)){
            ci.setReturnValue(false);
        }
    }
}

package net.hydra.jojomod.mixin.time_stop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IBlockEntityAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.GoBeyondEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Level.class)
public class TimeStopWorld implements TimeStop {
    /**Stropping time uses a list of time stopping entities.
     * Use that list to determine if a block or entity SHOULD be ticked.
     *
     * This class basically holds the list of time stoppers of a particular level/dimension
     * It can be called to add or remove time stoppers, to check the location and range of time stops, etc.
     * It also sends packets to client to update the list when changes occur*/

    private ImmutableList<LivingEntity> roundabout$timeStoppingEntities = ImmutableList.of();
    private ImmutableList<TimeStopInstance> roundabout$timeStoppingEntitiesClient = ImmutableList.of();

    @Unique
    public ImmutableList<TimeStopInstance> rdbt$getTimeStoppingEntitiesClient(){
        if (this.roundabout$timeStoppingEntities == null) {
            this.roundabout$timeStoppingEntities = ImmutableList.of();
        }
        return roundabout$timeStoppingEntitiesClient;
    }


    /**Adds an entity to the list of time stopping entities*/
    @Override
    public void addTimeStoppingEntity(LivingEntity $$0) {
        if (!((Level) (Object) this).isClientSide) {
            if (this.roundabout$timeStoppingEntities.isEmpty()) {
                this.roundabout$timeStoppingEntities = ImmutableList.of($$0);
            } else {
                if (!this.roundabout$timeStoppingEntities.contains($$0)) {
                    List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                    $$1.add($$0);
                    this.roundabout$timeStoppingEntities = ImmutableList.copyOf($$1);

                }
            }
            streamTimeStopToClients();
        }
    }

    @Override
    public void addTimeStoppingEntityClient(int id, double x, double y, double z, double range, int duration, int maxDuration) {
        if (((Level) (Object) this).isClientSide) {
            if (this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
                this.roundabout$timeStoppingEntitiesClient = ImmutableList.of(new TimeStopInstance(id,x,y,z,range, duration, maxDuration));
            } else {
                List<TimeStopInstance> $$0 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                int durationInterpolation = duration;
                for (int i = $$0.size() - 1; i >= 0; --i) {
                    if ($$0.get(i).id == id) {
                        durationInterpolation = $$0.get(i).durationInterpolation;
                        $$1.remove($$0.get(i));
                    }
                }
                TimeStopInstance tsi = new TimeStopInstance(id, x, y, z, range, duration, maxDuration);
                tsi.durationInterpolation = durationInterpolation;
                $$1.add(tsi);
                this.roundabout$timeStoppingEntitiesClient = ImmutableList.copyOf($$1);
            }
        }
    }

    /**Adds an entity to the list of time stopping entities*/
    @Override
    public void removeTimeStoppingEntity(LivingEntity $$0) {
        if (!((Level) (Object) this).isClientSide) {
            if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                for (int i = this.roundabout$timeStoppingEntities.size() - 1; i >= 0; --i) {
                    if (this.roundabout$timeStoppingEntities.get(i).getId() == $$0.getId()) {
                        streamTimeStopRemovalToClients(this.roundabout$timeStoppingEntities.get(i));
                        $$1.remove(i);
                    }
                }
                this.roundabout$timeStoppingEntities = ImmutableList.copyOf($$1);
            }
        }
    }

    @Override
    public void removeTimeStoppingEntityClient(int id){
        if (((Level) (Object) this).isClientSide) {
            if (!this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
                List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                for (int i = this.roundabout$timeStoppingEntitiesClient.size() - 1; i >= 0; --i) {
                    if (this.roundabout$timeStoppingEntitiesClient.get(i).id == id) {
                        $$1.remove(i);
                    }
                }
                this.roundabout$timeStoppingEntitiesClient = ImmutableList.copyOf($$1);
            }
        }
    }


    /**Sends an array of packets to the client, of every time stopping entity*/
    @Override
    public void streamTimeStopToClients() {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        Entity TSI = $$1.get(i);
                        /*You only need data of time stopping mobs that are relatively close by*/
                        if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) ||
                                MainUtil.timeStopDistanceTo(TSI.getX(),TSI.getY(),TSI.getZ(),serverPlayer.getX(),serverPlayer.getY(),serverPlayer.getZ()) <
                                        Math.max(250,ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite)){
                            S2CPacketUtil.addTSEntity(serverPlayer, TSI.getId(), TSI.getX(),
                                    TSI.getY(),TSI.getZ(),((StandUser) TSI).roundabout$getStandPowers().getTimestopRange(),
                                    ((StandUser) TSI).roundabout$getStandPowers().getChargedTSTicks(),
                                    ((StandUser) TSI).roundabout$getStandPowers().getMaxChargeTSTime());
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
                if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    S2CPacketUtil.removeTSEntity(serverPlayer, removedStoppingEntity.getId());
                }
            }
        }
    }

    /**On the client side, takes streamed packets and adds/removes entities from them to the liste*/
    @Override
    public void processTSPacket(int timeStoppingEntity, double x, double y, double z, double range, int duration, int maxDuration){
        if (((Level) (Object) this).isClientSide) {
            addTimeStoppingEntityClient(timeStoppingEntity, x,y,z, range, duration, maxDuration);
        }
    }
    @Override
    public void processTSRemovePacket(int timeStoppingEntity){
        if (((Level) (Object) this).isClientSide) {
           removeTimeStoppingEntityClient(timeStoppingEntity);
        }
    }

    @Override
    public void processTSBlockEntityPacket(BlockEntity blockEntity) {
        if (((Level) (Object) this).isClientSide) {
            ((IBlockEntityAccess) blockEntity).roundabout$setRoundaboutTimeInteracted(true);
        }
    }

    @Override
    public void tickTimeStoppingEntity() {
        if (!((Level) (Object) this).isClientSide) {
            streamTimeStopToClients();
        }
    }

    /**Ticks through time stop entities list, and then removes them from the list if they are dead or gone*/
    @Override
    public void tickAllTimeStops() {
        if (!((Level) (Object) this).isClientSide) {
            if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).isRemoved() || !$$1.get(i).isAlive() || $$1.get(i).level().dimensionTypeId() != this.dimensionTypeId) {
                        if ($$1.get(i).level().dimensionTypeId() != this.dimensionTypeId &&
                                (((StandUser) $$1.get(i)).roundabout$getStandPowers()) instanceof TWAndSPSharedPowers TP
                        ) {
                            TP.resumeTime(((Level) (Object) this));
                        } else {
                            removeTimeStoppingEntity($$1.get(i));
                        }
                    } else {
                        ((StandUser) $$1.get(i)).roundabout$getStandPowers().timeTickStopPower();
                    }
                }
            }
        } else {
            if (this.roundabout$timeStoppingEntitiesClient != null && !this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
                List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    $$1.get(i).durationInterpolation = Math.max(0,$$1.get(i).durationInterpolation-1);
                }
            }
        }

    }
    @Override
    public ImmutableList<LivingEntity> getTimeStoppingEntities() {
        if (!((Level) (Object) this).isClientSide) {
            return roundabout$timeStoppingEntities;
        } else {
            List<LivingEntity> $$0 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
            List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                Entity ent = ((Level) (Object) this).getEntity($$1.get(i).id);
                if (ent instanceof LivingEntity) {
                    $$0.add((LivingEntity) ent);
                }
            }
            return ImmutableList.copyOf($$0);
        }
    }

    @Override
    public boolean inTimeStopRange(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) || MainUtil.timeStopDistanceTo(pos.getX(), pos.getY(), pos.getZ(), it.getX(), it.getY(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getTimestopRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (!this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
                List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    TimeStopInstance it = $$1.get(i);
                        if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) || MainUtil.timeStopDistanceTo(pos.getX(), pos.getY(), pos.getZ(), it.x, it.y, it.z) <= it.range) {
                            return true;
                        }
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

    /**This is how the HUD finds who is time stopping you clientside*/
    @Override
    public TimeStopInstance getTimeStopperInstanceClient(Vec3 pos){
        if (!this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
            List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                TimeStopInstance it = $$1.get(i);
                if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) ||
                        MainUtil.timeStopDistanceTo(pos.x(), pos.y(), pos.z(), it.x, it.y, it.z) <= it.range) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public LivingEntity inTimeStopRangeEntity(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) || MainUtil.timeStopDistanceTo(pos.getX(), pos.getY(), pos.getZ(), it.getX(), it.getY(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getTimestopRange()) {
                        return it;
                    }
                }
            }
        } else {
            if (!this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
                List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    TimeStopInstance it = $$1.get(i);
                    if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) || MainUtil.timeStopDistanceTo(pos.getX(), pos.getY(), pos.getZ(), it.x, it.y, it.z) <= it.range) {
                        Entity it2 = ((Level) (Object) this).getEntity(it.id);
                        if (it2 instanceof LivingEntity){
                            return (LivingEntity) it2;
                        }
                    }
                }
            }
        }
        return null;
    }
    @Override
    public LivingEntity inTimeStopRangeEntity(Entity entity){
        return inTimeStopRangeEntity(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }

    /**Code this to check if they are able to use powers mid TS*/
    @Override
    public boolean CanTimeStopEntity(Entity entity){
        if (entity instanceof Player && ((Player) entity).isCreative() || entity.isSpectator()) {
            return false;
        } else if (entity instanceof Warden && ClientNetworking.getAppropriateConfig().timeStopSettings.wardenMovesInStoppedTime) {
            return false;
        } else if (entity instanceof GoBeyondEntity) {
            return false;
        } else if  (!(entity instanceof LivingEntity) && entity.getFirstPassenger() != null &&
                entity.getFirstPassenger() instanceof LivingEntity && this.isTimeStoppingEntity((LivingEntity) entity.getFirstPassenger())){
            return false;
        } else if (entity instanceof FollowingStandEntity FE) {
            LivingEntity Following = FE.getFollowing();
            if (Following != null && CanTimeStopEntity(Following)){
                return true;
            } else {
                return false;
            }
        } else if (entity instanceof Projectile) {
            if (((IProjectileAccess) entity).roundabout$getRoundaboutIsTimeStopCreated()){
                return false;
            } else {
                return inTimeStopRange(entity);
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
        if (!((Level) (Object) this).isClientSide) {
            if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).getId() == entity.getId()) {
                        return true;
                    }
                }
            }
        } else {
            if (!this.roundabout$timeStoppingEntitiesClient.isEmpty()) {
                List<TimeStopInstance> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).id == entity.getId()) {
                        return true;
                    }
                }
            }

        }
        return false;
    }



    /**When you interact with blocks like chests in timestop, sends packets to packet to let it render opening and such*/
    @Override
    public void streamTileEntityTSToCLient(BlockPos blockPos) {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (!this.roundabout$timeStoppingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    List<LivingEntity> $$1 = Lists.newArrayList(this.roundabout$timeStoppingEntities);
                        /*Streams updates to nearby players*/
                        if ((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1) ||
                                MainUtil.timeStopDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(),serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()) < Math.max(250,ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite)){
                            S2CPacketUtil.resumeTileEntityTSPacket(serverPlayer, new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                        }
                }
            }
        }
    }

    @Inject(method="shouldTickBlocksAt(Lnet/minecraft/core/BlockPos;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickBlocksAt(BlockPos $$0, CallbackInfoReturnable<Boolean> ci) {

        if (inTimeStopRange($$0) && !(((Level) (Object) this).getBlockState($$0).is(Blocks.MOVING_PISTON))){
            BlockEntity blk = ((Level) (Object) this).getBlockEntity($$0);
            if (blk != null && ((IBlockEntityAccess)blk).roundabout$getRoundaboutTimeInteracted()) {
                return;
            }
            ci.setReturnValue(false);
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private ResourceKey<DimensionType> dimensionTypeId;
}

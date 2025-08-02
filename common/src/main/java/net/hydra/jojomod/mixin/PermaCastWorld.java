package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(Level.class)
public class PermaCastWorld implements IPermaCasting {

    /**Uses a list of entities casting an unexpiring field with a stand, such as Justice's fog.
     * Use that list to determine if a block or entity SHOULD be ticked.*/
    @Unique
    private ImmutableList<LivingEntity> roundabout$PermaCastingEntities = ImmutableList.of();
    @Unique
    private ImmutableList<PermanentZoneCastInstance> roundabout$PermaCastingEntitiesClient = ImmutableList.of();

    @Shadow
    @Final
    private ResourceKey<DimensionType> dimensionTypeId;

    /**Adds an entity to the list of perma casting entities*/
    @Override
    public void roundabout$addPermaCaster(LivingEntity $$0) {
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (roundabout$PermaCastingEntities.isEmpty()) {
                roundabout$PermaCastingEntities = ImmutableList.of($$0);
            } else {
                if (!roundabout$PermaCastingEntities.contains($$0)) {
                    List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                    $$1.add($$0);
                    roundabout$PermaCastingEntities = ImmutableList.copyOf($$1);

                }
            }
            roundabout$streamPermaCastToClients();
        }
    }

    @Override
    public void roundabout$addPermaCastingEntityClient(int id, double x, double y, double z, double range, byte context) {
        if (((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (roundabout$PermaCastingEntitiesClient.isEmpty()) {
                roundabout$PermaCastingEntitiesClient = ImmutableList.of(new PermanentZoneCastInstance(id,x,y,z,range, context));
            } else {
                List<PermanentZoneCastInstance> $$0 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$0.size() - 1; i >= 0; --i) {
                    if ($$0.get(i).id == id) {
                        $$1.remove($$0.get(i));
                    }
                }
                $$1.add(new PermanentZoneCastInstance(id, x, y, z, range, context));
                roundabout$PermaCastingEntitiesClient = ImmutableList.copyOf($$1);
            }
        }
    }

    /**Adds an entity to the list of perma casting entities*/
    @Override
    public void roundabout$removePermaCastingEntity(LivingEntity $$0) {
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = roundabout$PermaCastingEntities.size() - 1; i >= 0; --i) {
                    if (roundabout$PermaCastingEntities.get(i).getId() == $$0.getId()) {
                        roundabout$permaCastRemovalToClients(roundabout$PermaCastingEntities.get(i));
                        $$1.remove(i);
                    }
                }
                roundabout$PermaCastingEntities = ImmutableList.copyOf($$1);
            }
        }
    }

    @Override
    public void roundabout$removePermaCastingEntityClient(int id){
        if (((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = roundabout$PermaCastingEntitiesClient.size() - 1; i >= 0; --i) {
                    if (roundabout$PermaCastingEntitiesClient.get(i).id == id) {
                        $$1.remove(i);
                    }
                }
                roundabout$PermaCastingEntitiesClient = ImmutableList.copyOf($$1);
            }
        }
    }


    /**Sends an array of packets to the client, of every perma casting entity*/
    @Override
    public void roundabout$streamPermaCastToClients() {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (roundabout$PermaCastingEntities == null){
                    roundabout$PermaCastingEntities = ImmutableList.of();
                }
                if (!roundabout$PermaCastingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        Entity TSI = $$1.get(i);
                        /*You only need data of perma casting mobs that are relatively close by*/
                        if (MainUtil.cheapDistanceTo2(TSI.getX(),TSI.getZ(),serverPlayer.getX(),serverPlayer.getZ()) < 250){
                            S2CPacketUtil.addPCEntity(serverPlayer, TSI.getId(), TSI.getX(),
                                    TSI.getY(),TSI.getZ(),((StandUser) TSI).roundabout$getStandPowers().getPermaCastRange(),
                                    ((StandUser) TSI).roundabout$getStandPowers().getPermaCastContext());
                        }
                    }
                }
            }
        }
    }
    public void roundabout$permaCastRemovalToClients(LivingEntity removedCastingEntity) {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (roundabout$PermaCastingEntities == null){
                    roundabout$PermaCastingEntities = ImmutableList.of();
                }
                if (!roundabout$PermaCastingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    S2CPacketUtil.removePCEntity(serverPlayer, removedCastingEntity.getId());
                }
            }
        }
    }

    /**On the client side, takes streamed packets and adds/removes entities from them to the liste*/
    @Override
    public void roundabout$processPermaCastPacket(int permaCastingEntity, double x, double y, double z, double range, byte context){
        if (((Level) (Object) this).isClientSide) {
            roundabout$addPermaCastingEntityClient(permaCastingEntity, x,y,z, range, context);
        }
    }
    @Override
    public void roundabout$processPermaCastRemovePacket(int permaCastingEntity){
        if (((Level) (Object) this).isClientSide) {
            roundabout$removePermaCastingEntityClient(permaCastingEntity);
        }
    }
    @Override
    public void roundabut$tickPermaCastingEntity() {
        if (!((Level) (Object) this).isClientSide) {
            roundabout$streamPermaCastToClients();
        }
    }

    /**Ticks through perma casting entities list, and then removes them from the list if they are dead or gone*/
    @Override
    public void roundabout$tickAllPermaCasts() {
        if (roundabout$PermaCastingEntities == null){
            roundabout$PermaCastingEntities = ImmutableList.of();
        }
        if (!roundabout$PermaCastingEntities.isEmpty()) {
            List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                if ($$1.get(i).isRemoved() || !$$1.get(i).isAlive() || $$1.get(i).level().dimensionTypeId() != this.dimensionTypeId){
                    roundabout$removePermaCastingEntity($$1.get(i));
                } else if (!((Level) (Object) this).isClientSide) {
                    ((StandUser)$$1.get(i)).roundabout$getStandPowers().tickPermaCast();
                }
            }
        }

    }
    public ImmutableList<LivingEntity> roundabout$getPermaCastingEntities() {
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            return roundabout$PermaCastingEntities;
        } else {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            List<LivingEntity> $$0 = Lists.newArrayList(roundabout$PermaCastingEntities);
            List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
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
    public boolean roundabout$inPermaCastRange(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    PermanentZoneCastInstance it = $$1.get(i);
                    if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean roundabout$inPermaCastRange(Entity entity){
        return roundabout$inPermaCastRange(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }

    @Override
    public LivingEntity roundabout$inPermaCastRangeEntity(Vec3i pos, byte permacast){
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).roundabout$getStandPowers().getPermaCastContext() == permacast &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                        return it;
                    }
                }
            }
        } else {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    PermanentZoneCastInstance it = $$1.get(i);
                    if (it.context == permacast &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
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
    public LivingEntity roundabout$inPermaCastFogRangeEntity(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).roundabout$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                        return it;
                    }
                }
            }
        } else {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    PermanentZoneCastInstance it = $$1.get(i);
                    if (it.context == PermanentZoneCastInstance.FOG_FIELD &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
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
    public LivingEntity roundabout$inPermaCastFogRangeEntity(Entity entity){
        return roundabout$inPermaCastFogRangeEntity(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }
    @Override
    public boolean roundabout$inPermaCastRange(Vec3i pos, byte type){
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).roundabout$getStandPowers().getPermaCastContext() == type &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    PermanentZoneCastInstance it = $$1.get(i);
                    if (it.context == type &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public boolean roundabout$inPermaCastFogRange(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).roundabout$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    PermanentZoneCastInstance it = $$1.get(i);
                    if (it.context == PermanentZoneCastInstance.FOG_FIELD &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean roundabout$inPermaCastFogRange(Entity entity){
        return roundabout$inPermaCastFogRange(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }
    /**This is how the HUD finds who is perma casting upon you clientside*/
    @Override
    public PermanentZoneCastInstance roundabout$getPermaCastingInstanceClient(Vec3 pos){

        if (roundabout$PermaCastingEntitiesClient == null){
            roundabout$PermaCastingEntitiesClient = ImmutableList.of();
        }
        if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
            List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                PermanentZoneCastInstance it = $$1.get(i);
                if (MainUtil.cheapDistanceTo2(pos.x(), pos.z(), it.x, it.z) <= it.range) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public LivingEntity roundabout$inPermaCastRangeEntityJustice(FallenMob fm, Vec3i pos){
        if (!fm.getActivated() && fm.getPhasesFull()) {
            if (!((Level) (Object) this).isClientSide) {

                if (roundabout$PermaCastingEntities == null) {
                    roundabout$PermaCastingEntities = ImmutableList.of();
                }
                if (!roundabout$PermaCastingEntities.isEmpty()) {
                    List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        LivingEntity it = $$1.get(i);
                        if (((StandUser) it).roundabout$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD &&
                                MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                            if (!fm.getTicksThroughPlacer() || it.is(fm.placer)){
                                if (((StandUser)it).roundabout$getStandPowers() instanceof PowersJustice PJ){
                                    if (PJ.queryJusticeEntities().size() < ClientNetworking.getAppropriateConfig().justiceSettings.maxCorpses){
                                        return it;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {

                if (roundabout$PermaCastingEntitiesClient == null) {
                    roundabout$PermaCastingEntitiesClient = ImmutableList.of();
                }
                if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                    List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        PermanentZoneCastInstance it = $$1.get(i);
                        if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
                            Entity it2 = ((Level) (Object) this).getEntity(it.id);
                            if (it2 instanceof LivingEntity && ((StandUser) it2).roundabout$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD) {
                                if (!fm.getTicksThroughPlacer() || it2.getId() == fm.getPlacer()) {
                                    if (((StandUser)it2).roundabout$getStandPowers() instanceof PowersJustice PJ) {
                                        if (PJ.queryJusticeEntities().size() < ClientNetworking.getAppropriateConfig().justiceSettings.maxCorpses) {
                                            return (LivingEntity) it2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    @Override
    public LivingEntity roundabout$inPermaCastRangeEntity(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {

            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).roundabout$getStandPowers().getPermaCastRange()) {
                        return it;
                    }
                }
            }
        } else {

            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    PermanentZoneCastInstance it = $$1.get(i);
                    if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
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
    public LivingEntity roundabout$inPermaCastRangeEntity(Entity entity){
        return roundabout$inPermaCastRangeEntity(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }



    /**Code this to check if they are in the list*/
    public boolean roundabout$isPermaCastingEntity(LivingEntity entity){
        if (!((Level) (Object) this).isClientSide) {
            if (roundabout$PermaCastingEntities == null){
                roundabout$PermaCastingEntities = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(roundabout$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).getId() == entity.getId()) {
                        return true;
                    }
                }
            }
        } else {
            if (roundabout$PermaCastingEntitiesClient == null){
                roundabout$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!roundabout$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(roundabout$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).id == entity.getId()) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
}

package net.hydra.jojomod.mixin.stand_users;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
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
    private ImmutableList<LivingEntity> sba$PermaCastingEntities = ImmutableList.of();
    @Unique
    private ImmutableList<PermanentZoneCastInstance> sba$PermaCastingEntitiesClient = ImmutableList.of();


    /**Adds an entity to the list of perma casting entities*/
    @Override
    public void sba$addPermaCaster(LivingEntity $$0) {
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (sba$PermaCastingEntities.isEmpty()) {
                sba$PermaCastingEntities = ImmutableList.of($$0);
            } else {
                if (!sba$PermaCastingEntities.contains($$0)) {
                    List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                    $$1.add($$0);
                    sba$PermaCastingEntities = ImmutableList.copyOf($$1);

                }
            }
            sba$streamPermaCastToClients();
        }
    }

    @Override
    public void sba$addPermaCastingEntityClient(int id, double x, double y, double z, double range, byte context) {
        if (((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (sba$PermaCastingEntitiesClient.isEmpty()) {
                sba$PermaCastingEntitiesClient = ImmutableList.of(new PermanentZoneCastInstance(id,x,y,z,range, context));
            } else {
                List<PermanentZoneCastInstance> $$0 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
                for (int i = $$0.size() - 1; i >= 0; --i) {
                    if ($$0.get(i).id == id) {
                        $$1.remove($$0.get(i));
                    }
                }
                $$1.add(new PermanentZoneCastInstance(id, x, y, z, range, context));
                sba$PermaCastingEntitiesClient = ImmutableList.copyOf($$1);
            }
        }
    }

    /**Adds an entity to the list of perma casting entities*/
    @Override
    public void sba$removePermaCastingEntity(LivingEntity entity) {
        if (((Level) (Object) this).isClientSide) {
            return;
        }

        if (sba$PermaCastingEntities == null) {
            sba$PermaCastingEntities = ImmutableList.of();
            return;
        }

        if (sba$PermaCastingEntities.isEmpty()) {
            return;
        }

        List<LivingEntity> updatedEntities =
                Lists.newArrayList(sba$PermaCastingEntities);

        boolean removed = updatedEntities.removeIf(existingEntity -> {
            if (existingEntity.getId() == entity.getId()) {
                sba$permaCastRemovalToClients(existingEntity);
                return true;
            }

            return false;
        });

        if (removed) {
            sba$PermaCastingEntities =
                    ImmutableList.copyOf(updatedEntities);
        }
    }

    @Override
    public void sba$removePermaCastingEntityClient(int id){
        if (((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
                for (int i = sba$PermaCastingEntitiesClient.size() - 1; i >= 0; --i) {
                    if (sba$PermaCastingEntitiesClient.get(i).id == id) {
                        $$1.remove(i);
                    }
                }
                sba$PermaCastingEntitiesClient = ImmutableList.copyOf($$1);
            }
        }
    }


    /**Sends an array of packets to the client, of every perma casting entity*/
    @Override
    public void sba$streamPermaCastToClients() {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (sba$PermaCastingEntities == null){
                    sba$PermaCastingEntities = ImmutableList.of();
                }
                if (!sba$PermaCastingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        Entity TSI = $$1.get(i);
                        /*You only need data of perma casting mobs that are relatively close by*/
                        if (MainUtil.cheapDistanceTo2(TSI.getX(),TSI.getZ(),serverPlayer.getX(),serverPlayer.getZ()) < 250){
                            S2CPacketUtil.addPCEntity(serverPlayer, TSI.getId(), TSI.getX(),
                                    TSI.getY(),TSI.getZ(),((StandUser) TSI).sba$getStandPowers().getPermaCastRange(),
                                    ((StandUser) TSI).sba$getStandPowers().getPermaCastContext());
                        }
                    }
                }
            }
        }
    }
    public void sba$permaCastRemovalToClients(LivingEntity removedCastingEntity) {
        if (!((Level) (Object) this).isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) (Object) this);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                if (sba$PermaCastingEntities == null){
                    sba$PermaCastingEntities = ImmutableList.of();
                }
                if (!sba$PermaCastingEntities.isEmpty()) {
                    ServerPlayer serverPlayer = serverWorld.players().get(j);
                    S2CPacketUtil.removePCEntity(serverPlayer, removedCastingEntity.getId());
                }
            }
        }
    }

    /**On the client side, takes streamed packets and adds/removes entities from them to the liste*/
    @Override
    public void sba$processPermaCastPacket(int permaCastingEntity, double x, double y, double z, double range, byte context){
        if (((Level) (Object) this).isClientSide) {
            sba$addPermaCastingEntityClient(permaCastingEntity, x,y,z, range, context);
        }
    }
    @Override
    public void sba$processPermaCastRemovePacket(int permaCastingEntity){
        if (((Level) (Object) this).isClientSide) {
            sba$removePermaCastingEntityClient(permaCastingEntity);
        }
    }
    @Override
    public void roundabut$tickPermaCastingEntity() {
        if (!((Level) (Object) this).isClientSide) {
            sba$streamPermaCastToClients();
        }
    }

    /**Ticks through perma casting entities list, and then removes them from the list if they are dead or gone*/
    @Override
    public void sba$tickAllPermaCasts() {
        if (sba$PermaCastingEntities == null){
            sba$PermaCastingEntities = ImmutableList.of();
        }
        if (!sba$PermaCastingEntities.isEmpty()) {
            List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                if ($$1.get(i).isRemoved() || !$$1.get(i).isAlive() || $$1.get(i).level().dimensionTypeId() != this.dimensionTypeId){
                    sba$removePermaCastingEntity($$1.get(i));
                } else if (!((Level) (Object) this).isClientSide) {
                    ((StandUser)$$1.get(i)).sba$getStandPowers().tickPermaCast();
                }
            }
        }

    }
    public ImmutableList<LivingEntity> sba$getPermaCastingEntities() {
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            return sba$PermaCastingEntities;
        } else {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            List<LivingEntity> $$0 = Lists.newArrayList(sba$PermaCastingEntities);
            List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public boolean sba$inPermaCastRange(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public boolean sba$inPermaCastRange(Entity entity){
        return sba$inPermaCastRange(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }

    @Override
    public LivingEntity sba$inPermaCastRangeEntity(Vec3i pos, byte permacast){
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).sba$getStandPowers().getPermaCastContext() == permacast &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                        return it;
                    }
                }
            }
        } else {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public LivingEntity sba$inPermaCastFogRangeEntity(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).sba$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                        return it;
                    }
                }
            }
        } else {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public LivingEntity sba$inPermaCastFogRangeEntity(Entity entity){
        return sba$inPermaCastFogRangeEntity(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }
    @Override
    public boolean sba$inPermaCastRange(Vec3i pos, byte type){
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).sba$getStandPowers().getPermaCastContext() == type &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public boolean sba$inPermaCastFogRange(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (((StandUser) it).sba$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD &&
                            MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                        return true;
                    }
                }
            }
        } else {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public boolean sba$inPermaCastFogRange(Entity entity){
        return sba$inPermaCastFogRange(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }
    /**This is how the HUD finds who is perma casting upon you clientside*/
    @Override
    public PermanentZoneCastInstance sba$getPermaCastingInstanceClient(Vec3 pos){

        if (sba$PermaCastingEntitiesClient == null){
            sba$PermaCastingEntitiesClient = ImmutableList.of();
        }
        if (!sba$PermaCastingEntitiesClient.isEmpty()) {
            List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public LivingEntity sba$inPermaCastRangeEntityJustice(FallenMob fm, Vec3i pos){
        if (!fm.getActivated() && fm.getPhasesFull()) {
            if (!((Level) (Object) this).isClientSide) {

                if (sba$PermaCastingEntities == null) {
                    sba$PermaCastingEntities = ImmutableList.of();
                }
                if (!sba$PermaCastingEntities.isEmpty()) {
                    List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        LivingEntity it = $$1.get(i);
                        if (((StandUser) it).sba$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD &&
                                MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                            if (!fm.getTicksThroughPlacer() || it.is(fm.placer)){
                                if (((StandUser)it).sba$getStandPowers() instanceof PowersJustice PJ){
                                    if (PJ.queryJusticeEntities().size() < ClientNetworking.getAppropriateConfig().justiceSettings.maxCorpses){
                                        return it;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {

                if (sba$PermaCastingEntitiesClient == null) {
                    sba$PermaCastingEntitiesClient = ImmutableList.of();
                }
                if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                    List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
                    for (int i = $$1.size() - 1; i >= 0; --i) {
                        PermanentZoneCastInstance it = $$1.get(i);
                        if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.x, it.z) <= it.range) {
                            Entity it2 = ((Level) (Object) this).getEntity(it.id);
                            if (it2 instanceof LivingEntity && ((StandUser) it2).sba$getStandPowers().getPermaCastContext() == PermanentZoneCastInstance.FOG_FIELD) {
                                if (!fm.getTicksThroughPlacer() || it2.getId() == fm.getPlacer()) {
                                    if (((StandUser)it2).sba$getStandPowers() instanceof PowersJustice PJ) {
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
    public LivingEntity sba$inPermaCastRangeEntity(Vec3i pos){
        if (!((Level) (Object) this).isClientSide) {

            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    LivingEntity it = $$1.get(i);
                    if (MainUtil.cheapDistanceTo2(pos.getX(), pos.getZ(), it.getX(), it.getZ()) <= ((StandUser) it).sba$getStandPowers().getPermaCastRange()) {
                        return it;
                    }
                }
            }
        } else {

            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
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
    public LivingEntity sba$inPermaCastRangeEntity(Entity entity){
        return sba$inPermaCastRangeEntity(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }



    /**Code this to check if they are in the list*/
    public boolean sba$isPermaCastingEntity(LivingEntity entity){
        if (!((Level) (Object) this).isClientSide) {
            if (sba$PermaCastingEntities == null){
                sba$PermaCastingEntities = ImmutableList.of();
            }
            if (!sba$PermaCastingEntities.isEmpty()) {
                List<LivingEntity> $$1 = Lists.newArrayList(sba$PermaCastingEntities);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).getId() == entity.getId()) {
                        return true;
                    }
                }
            }
        } else {
            if (sba$PermaCastingEntitiesClient == null){
                sba$PermaCastingEntitiesClient = ImmutableList.of();
            }
            if (!sba$PermaCastingEntitiesClient.isEmpty()) {
                List<PermanentZoneCastInstance> $$1 = Lists.newArrayList(sba$PermaCastingEntitiesClient);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    if ($$1.get(i).id == entity.getId()) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
    @Override
    public boolean sba$inPurpleHazeRange(Vec3i pos) {
        return sba$inPermaCastRange(
                pos,
                PermanentZoneCastInstance.PURPLE_SMOKE
        );
    }

    @Override
    public boolean sba$inPurpleHazeRange(Entity entity) {
        return sba$inPurpleHazeRange(
                new Vec3i(
                        (int) entity.getX(),
                        (int) entity.getY(),
                        (int) entity.getZ()
                )
        );
    }

    @Override
    public LivingEntity sba$inPurpleHazeRangeEntity(Vec3i pos) {
        return sba$inPermaCastRangeEntity(
                pos,
                PermanentZoneCastInstance.PURPLE_SMOKE
        );
    }

    @Override
    public LivingEntity sba$inPurpleHazeRangeEntity(Entity entity) {
        return sba$inPurpleHazeRangeEntity(
                new Vec3i(
                        (int) entity.getX(),
                        (int) entity.getY(),
                        (int) entity.getZ()
                )
        );
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private ResourceKey<DimensionType> dimensionTypeId;
}

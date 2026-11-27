package net.hydra.jojomod.access;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface IPermaCasting {
    void sba$addPermaCaster(LivingEntity $$0);
    void sba$addPermaCastingEntityClient(int id, double x, double y, double z, double range, byte context);
    void sba$removePermaCastingEntity(LivingEntity $$0);
    void sba$removePermaCastingEntityClient(int id);
    void sba$streamPermaCastToClients();
    boolean sba$isPermaCastingEntity(LivingEntity entity);
    LivingEntity sba$inPermaCastRangeEntity(Vec3i pos);
    LivingEntity sba$inPermaCastRangeEntity(Entity entity);
    LivingEntity sba$inPermaCastRangeEntity(Vec3i pos, byte permacast);
    LivingEntity sba$inPermaCastRangeEntityJustice(FallenMob fm, Vec3i pos);
    PermanentZoneCastInstance sba$getPermaCastingInstanceClient(Vec3 pos);
    boolean sba$inPermaCastRange(Entity entity);
    boolean sba$inPermaCastRange(Vec3i pos);
    ImmutableList<LivingEntity> sba$getPermaCastingEntities();
    void sba$tickAllPermaCasts();
    void roundabut$tickPermaCastingEntity();
    void sba$processPermaCastRemovePacket(int permaCastingEntity);
    void sba$permaCastRemovalToClients(LivingEntity removedCastingEntity);
    void sba$processPermaCastPacket(int permaCastingEntity, double x, double y, double z, double range, byte context);

    boolean sba$inPermaCastFogRange(Entity entity);
    boolean sba$inPermaCastFogRange(Vec3i pos);
    boolean sba$inPermaCastRange(Vec3i pos, byte type);
    LivingEntity sba$inPermaCastFogRangeEntity(Entity entity);
    LivingEntity sba$inPermaCastFogRangeEntity(Vec3i pos);

    boolean sba$inPurpleHazeRange(Entity entity);
    boolean sba$inPurpleHazeRange(Vec3i pos);

    LivingEntity sba$inPurpleHazeRangeEntity(Entity entity);
    LivingEntity sba$inPurpleHazeRangeEntity(Vec3i pos);
}

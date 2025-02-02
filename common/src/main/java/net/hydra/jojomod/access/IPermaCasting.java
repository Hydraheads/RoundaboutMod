package net.hydra.jojomod.access;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface IPermaCasting {
    void roundabout$addPermaCaster(LivingEntity $$0);
    void roundabout$addPermaCastingEntityClient(int id, double x, double y, double z, double range, byte context);
    void roundabout$removePermaCastingEntity(LivingEntity $$0);
    void roundabout$removePermaCastingEntityClient(int id);
    void roundabout$streamPermaCastToClients();
    boolean roundabout$isPermaCastingEntity(LivingEntity entity);
    LivingEntity roundabout$inPermaCastRangeEntity(Vec3i pos);
    LivingEntity roundabout$inPermaCastRangeEntity(Entity entity);
    LivingEntity roundabout$inPermaCastRangeEntityJustice(FallenMob fm, Vec3i pos);
    PermanentZoneCastInstance roundabout$getPermaCastingInstanceClient(Vec3 pos);
    boolean roundabout$inPermaCastRange(Entity entity);
    boolean roundabout$inPermaCastRange(Vec3i pos);
    ImmutableList<LivingEntity> roundabout$getPermaCastingEntities();
    void roundabout$tickAllPermaCasts();
    void roundabut$tickPermaCastingEntity();
    void roundabout$processPermaCastRemovePacket(int permaCastingEntity);
    void roundabout$permaCastRemovalToClients(LivingEntity removedCastingEntity);
    void roundabout$processPermaCastPacket(int permaCastingEntity, double x, double y, double z, double range, byte context);

    boolean roundabout$inPermaCastFogRange(Entity entity);
    boolean roundabout$inPermaCastFogRange(Vec3i pos);
    LivingEntity roundabout$inPermaCastFogRangeEntity(Entity entity);
    LivingEntity roundabout$inPermaCastFogRangeEntity(Vec3i pos);
}

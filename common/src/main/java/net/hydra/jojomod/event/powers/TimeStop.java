package net.hydra.jojomod.event.powers;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface TimeStop {
    ImmutableList<LivingEntity> getTimeStoppingEntities();
    boolean inTimeStopRange(Vec3i pos);
    boolean inTimeStopRange(Entity entity);
    boolean CanTimeStopEntity(Entity entity);
    boolean isTimeStoppingEntity(LivingEntity entity);
    void addTimeStoppingEntity(LivingEntity $$0);
    void removeTimeStoppingEntity(LivingEntity $$0);
    void streamTimeStopToClients(boolean force, boolean removal);
    void processTSPacket(LivingEntity timeStoppingEntity, boolean removal);
    void tickTimeStoppingEntity();
}

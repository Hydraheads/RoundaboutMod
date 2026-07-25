package net.hydra.jojomod.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TimeSkipSnapshot {
    public final int entityId;
    public final Vec3 position;
    public final float xRot;
    public final float yRot;

    public boolean has(TimeSkipSnapshot snapshot){
        return snapshot.entityId == entityId;
    }
    public boolean equals(TimeSkipSnapshot snapshot){
        return snapshot.entityId == entityId && snapshot.xRot == xRot && snapshot.yRot == yRot &&
                snapshot.position.equals(position);
    }

    public TimeSkipSnapshot(int entityId, Vec3 position, float xRot, float yRot) {
        this.entityId = entityId;
        this.position = position;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public Entity getEntity(Level level){
        return level.getEntity(entityId);
    }
    public int getEntityId(){
        return entityId;
    }
    public Vec3 getPosition() {
        return position;
    }
}

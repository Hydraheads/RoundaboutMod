package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.visages.CloneEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class D4CCloneEntity extends CloneEntity {
    public D4CCloneEntity(EntityType<? extends PathfinderMob> entity, Level world) {
        super(entity, world);
    }

    @Override
    public void tick() {
        super.tick();
    }

    private Vector3f sizeOffset = new Vector3f(0.f, 0.f, 0.f);
    public void setSizeOffset(Vector3f value) { this.sizeOffset = value; }
    public Vector3f getSizeOffset() { return this.sizeOffset; }
}

package net.hydra.jojomod.event;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class SavedSecond {

    public float headYRotation;
    public Vec2 rotationVec;
    public Vec3 position;


    public SavedSecond(float headYRotation,Vec2 rotationVec,Vec3 position){
        this.headYRotation = headYRotation;
        this.rotationVec = new Vec2(rotationVec.x,rotationVec.y);
        this.position = new Vec3(position.x,position.y,position.z);
    }
}

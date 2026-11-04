package net.hydra.jojomod.client;

import net.minecraft.world.phys.Vec3;

public class SilverChariotAfterimageState {

    private Vec3 pos;
    private float xRot;
    private float yRot;
    private Vec3 currVel;

    public SilverChariotAfterimageState(Vec3 pos, float xRot, float yRot, Vec3 currVel) {
        this.pos = pos;
        this.xRot = xRot;
        this.yRot = yRot;
        this.currVel = currVel;
    }

    public Vec3 getPos() {
        return pos;
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public Vec3 getCurrVel() {
        return currVel;
    }
}

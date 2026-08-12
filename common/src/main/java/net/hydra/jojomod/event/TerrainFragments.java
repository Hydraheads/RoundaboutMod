package net.hydra.jojomod.event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TerrainFragments {

    public BlockState state;

    public Vec3 pos;
    public Vec3 prevPos;
    public Vec3 velocity;

    public float rotX;
    public float rotY;
    public float rotZ;

    public float prevRotX;
    public float prevRotY;
    public float prevRotZ;

    public float rotSpeedX;
    public float rotSpeedY;
    public float rotSpeedZ;
    public float scale;
    public float prevScale;

    public int age;
    public int maxAge;
}
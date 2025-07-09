package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class RattEntity extends StandEntity {
    public RattEntity(EntityType<? extends Mob> entityType, Level world) {super(entityType, world);}

    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            MELON_SKIN = 3,
            SAND_SKIN = 4,
            AZTEC_SKIN = 5,
            REDD_SKIN = 6;


    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                MELON_SKIN,
                SAND_SKIN,
                REDD_SKIN
        );
    }



    public byte MotionState = PowersRatt.SHOULDER;
    public Vec3 Placement = null;

    public BlockState BlockBelow = null;
    public BlockState BlockInside = null;


    @Override
    public void tick() {
        super.tick();


        this.setYBodyRot(this.getBodyRotationY()+10);

        if (getUser() != null) {
            if (!this.getUserData(this.getUser()).roundabout$getActive()) {
                this.remove(RemovalReason.DISCARDED); // might not be the best method
            }
        }

        switch (MotionState) {
            case PowersRatt.SHOULDER -> {
                UpdateState(OffsetIndex.FOLLOW);
            }

            case PowersRatt.MOVING -> {
                UpdateState(OffsetIndex.LOOSE);
                Vec3 target = this.getUser().getPosition(0);
                if (Placement != null) {target = Placement;}


                UpdatePos(this.getPosition(0).lerp(target,0.83));


                if (getPosition(0).distanceTo(target) < 0.2) {
                    UpdatePos(target);
                    if (target == Placement) {
                        UpdateMotionState(PowersRatt.PLACED);
                        BlockPos pos = new BlockPos((int)this.getX(),(int)this.getY(),(int)this.getZ());
                        BlockBelow = this.level().getBlockState(pos);
                        BlockInside = this.level().getBlockState(pos.offset(0,1,0));
                        Roundabout.LOGGER.info(BlockBelow.toString());
                    } else {
                        UpdateMotionState(PowersRatt.SHOULDER);
                    }
                }
            }

            case PowersRatt.PLACED -> {
                UpdateState(OffsetIndex.LOOSE);
             //   setBodyRotationY(this.getBodyRotationY()+10);
                if (Placement != null) {
                    if (!getPosition(0).equals(Placement)) {
                      //  UpdatePos(Placement);
                    }
                } else {
                    UpdateMotionState(PowersRatt.MOVING);
                }

                BlockPos pos = new BlockPos((int)this.getX(),(int)this.getY(),(int)this.getZ());
                int cd = 0;
                // forced return code

                if (cd != 0) {
                    if (this.getUser() != null) {
                        PowersRatt powers  = ((PowersRatt) this.getUserData(this.getUser()).roundabout$getStandPowers());
                        powers.Recall();
                    }
                }

            }
        }
    }




    public void UpdatePos(Vec3 v) {
        if (this.getUser() != null) {
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryPosPowerPacket(PowersRatt.UPDATE_POSITION,v);
        }
    }
    public void UpdatePlacement(Vec3 v) {
        if (this.getUser() != null) {
            Placement = v;
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryPosPowerPacket(PowersRatt.UPDATE_PLACEMENT,v);
        }
    }
    public void NullPlacement() {
        if (this.getUser() != null) {
            Placement = null;
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryPowerPacket(PowersRatt.NULL_PLACEMENT);
        }
    }
    public void UpdateRotation(int x, int y, int z) {
        if (this.getUser() != null) {
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryPosPower(PowersRatt.ROTATE,true,new Vec3(x,y,z));
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryPosPowerPacket(PowersRatt.ROTATE,new Vec3(x,y,z));
        }
    }


    public void UpdateMotionState(byte s) {
        if (this.getUser() != null) {
            StandPowers powers = ((StandUser) this.getUser()).roundabout$getStandPowers();
            MotionState = s;
            powers.tryIntPowerPacket(PowersRatt.UPDATE_STATE,(int) s);
        }
    }
    public void UpdateState(byte s) {
        if (this.getUser() != null) {
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryIntPowerPacket(PowersRatt.UPDATE_OFFSET_TYPE,(int) s);
        }
    }


    @Override
    public boolean lockPos(){
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return true;
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
       /* if (this.getUser() != null) {
            if (this.getAnimation() == DEFACE) {
                this.deface.startIfStopped(this.tickCount);
            } else {
                this.deface.stop();
            }
            if (this.getAnimation() == VISAGES) {
                this.visages.startIfStopped(this.tickCount);
            } else {
                this.visages.stop();
            }
        }*/
    }
}


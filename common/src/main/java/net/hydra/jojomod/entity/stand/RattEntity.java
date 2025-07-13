package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.client.models.layers.PreRenderEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class RattEntity extends StandEntity {
    public RattEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            MELON_SKIN = 3,
            SAND_SKIN = 4,
            AZTEC_SKIN = 5,
            REDD_SKIN = 6,
            SNOWY_SKIN = 7;


    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                MELON_SKIN,
                SAND_SKIN,
                REDD_SKIN,
                SNOWY_SKIN
        );
    }




    @Override
    public void tick() {



        if (this.level().isClientSide()) {
            if (this.getUser() != null) {
                PowersRatt RE = (PowersRatt) this.getUserData(this.getUser()).roundabout$getStandPowers();
                Entity target = RE.getShootTarget();
                Vec3 targetPos = RE.getTargetPos().getLocation();
                if (target != null) {
                    targetPos = target.getPosition(0);
                }
                double x = targetPos.x() - this.getPosition(0).x();
                double z = targetPos.z() - this.getPosition(0).z();
                float rot = ((float) Math.atan2((float) z, (float) x) * 180 / (float) Math.PI) - 90;
                updateRotation(new Vec3(rot, rot, 0));
            }
        }


        super.tick();
    }


    public void updateRotation(Vec3 v) {
        if (this.getUser() != null){
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersRatt RE) {
                RE.tryPosPower(PowersRatt.ROTATE,true,v);
                RE.tryPosPowerPacket(PowersRatt.ROTATE,v);
            }
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


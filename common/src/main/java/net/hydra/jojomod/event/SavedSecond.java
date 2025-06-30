package net.hydra.jojomod.event;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class SavedSecond {

    public float headYRotation;
    public Vec2 rotationVec;
    public Vec3 position;
    public boolean hasHadParticle = false;


    public SavedSecond(float headYRotation,Vec2 rotationVec,Vec3 position){
        this.headYRotation = headYRotation;
        this.rotationVec = new Vec2(rotationVec.x,rotationVec.y);
        this.position = new Vec3(position.x,position.y,position.z);
    }

    public static SavedSecond saveEntitySecond(Entity ent) {
        if (ent instanceof Player PL) {
            return new SavedSecondPlayer(PL.getYHeadRot(), PL.getRotationVector(), PL.getPosition(1), PL.getActiveEffects(),
                    PL.getHealth(), PL.getFoodData().getFoodLevel(), PL.getFoodData().getSaturationLevel(), PL.getFoodData().getExhaustionLevel());
        } if (ent instanceof LivingEntity LE) {
            return new SavedSecondLiving(LE.getYHeadRot(), LE.getRotationVector(), LE.getPosition(1), LE.getActiveEffects(),
                    LE.getHealth());
        } if (ent != null){
            return new SavedSecond(ent.getYHeadRot(), ent.getRotationVector(), ent.getPosition(1));
        }
        return null;
    }

    public void loadTime(Entity ent){
        ent.setYHeadRot(this.headYRotation);
        ent.setXRot(this.rotationVec.x);
        ent.setYRot(this.rotationVec.y);
        ent.setPos(this.position);
    }
}

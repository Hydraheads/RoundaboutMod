package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Level.class)
public class ZLevel implements ILevelAccess {

    @Unique
    public List<SoftAndWetPlunderBubbleEntity> roundabout$plunderBubbles = new ArrayList<>();

    @Unique
    public void roundabout$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            if (!bubbleIteration.contains(plunder)){
                roundabout$plunderBubbles.add(plunder);
            }
        }
    }
    @Unique
    public void roundabout$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder)) {
            roundabout$plunderBubbles.remove(plunder);
        }
    }


    @Unique
    public void roundabout$bubbleInit(){
        if (roundabout$plunderBubbles == null) {
            roundabout$plunderBubbles = new ArrayList<>();
        }
    }

    /**Offload Bubbles that are done*/
    @Unique
    public void roundabout$tickPlunderBubbleRemoval(){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (value.isRemoved() || !value.isAlive()) {
                    roundabout$plunderBubbles.remove(value);
                }
            }
        }
    }

    @Unique
    public boolean roundabout$isFrictionPlundered(BlockPos blockPos){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (value.getPlunderType() == PlunderTypes.FRICTION.id) {
                    if (!value.isRemoved() && value.isAlive()) {
                        BlockPos bpos2 = value.getBlockPos();
                        if (MainUtil.cheapDistanceTo(blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                bpos2.getX(),bpos2.getY(),bpos2.getZ()) < 5){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Unique
    public boolean roundabout$isSoundPlundered(BlockPos blockPos){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (value.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!value.isRemoved() && value.isAlive()) {
                        BlockPos bpos2 = value.getBlockPos();
                        if (MainUtil.cheapDistanceTo(blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                bpos2.getX(),bpos2.getY(),bpos2.getZ()) < 5){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

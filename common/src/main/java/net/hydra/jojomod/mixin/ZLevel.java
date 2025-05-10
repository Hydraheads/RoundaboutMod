package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
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

    }
    @Unique
    public void roundabout$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder){

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
        List<SoftAndWetPlunderBubbleEntity> hurricaneSpecial2 = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : hurricaneSpecial2) {
                if (value.isRemoved() || !value.isAlive()) {
                    roundabout$plunderBubbles.remove(value);
                }
            }
        }
    }
}

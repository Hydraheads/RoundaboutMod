package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.StoredSoundInstance;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Level.class)
public class ZLevel implements ILevelAccess {

    @Unique
    public List<SoftAndWetPlunderBubbleEntity> roundabout$plunderBubbles = new ArrayList<>();
    @Unique
    public List<SoftAndWetPlunderBubbleEntity> roundabout$entityPlunderBubbles = new ArrayList<>();

    @Unique
    public List<SoftAndWetPlunderBubbleEntity> roundabout$frictionBubbles = new ArrayList<>();
    @Unique
    public List<SoftAndWetPlunderBubbleEntity> roundabout$entityFrictionBubbles = new ArrayList<>();

    @Unique
    public void roundabout$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder){
        roundabout$bubbleInit();

        if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.FRICTION.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$frictionBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                roundabout$frictionBubbles.add(plunder);
            }
        } else if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.SOUND.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                roundabout$plunderBubbles.add(plunder);
            }
        }
    }
    @Unique
    public void roundabout$addPlunderBubbleEntity(SoftAndWetPlunderBubbleEntity plunder){
        roundabout$bubbleInit();

        if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.FRICTION.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$entityFrictionBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                roundabout$entityFrictionBubbles.add(plunder);
            }
        } else if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.SOUND.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$entityPlunderBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                roundabout$entityPlunderBubbles.add(plunder);
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
        } if (roundabout$entityPlunderBubbles == null) {
            roundabout$entityPlunderBubbles = new ArrayList<>();
        }if (roundabout$frictionBubbles == null) {
            roundabout$plunderBubbles = new ArrayList<>();
        } if (roundabout$entityFrictionBubbles == null) {
            roundabout$entityPlunderBubbles = new ArrayList<>();
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
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration2 = new ArrayList<>(roundabout$entityPlunderBubbles) {
        };
        if (!bubbleIteration2.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration2) {
                if (value.isRemoved() || !value.isAlive()) {
                    roundabout$entityPlunderBubbles.remove(value);
                }
            }
        }
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration3 = new ArrayList<>(roundabout$entityFrictionBubbles) {
        };
        if (!bubbleIteration3.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration3) {
                if (value.isRemoved() || !value.isAlive()) {
                    roundabout$entityFrictionBubbles.remove(value);
                }
            }
        }
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration4 = new ArrayList<>(roundabout$frictionBubbles) {
        };
        if (!bubbleIteration4.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration4) {
                if (value.isRemoved() || !value.isAlive()) {
                    roundabout$frictionBubbles.remove(value);
                }
            }
        }
    }

    @Unique
    public boolean roundabout$isFrictionPlundered(BlockPos blockPos){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$frictionBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.FRICTION.id) {
                    if (!value.isRemoved() && value.isAlive()) {
                        BlockPos bpos2 = value.getBlockPos();
                        if (MainUtil.cheapDistanceTo(blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                bpos2.getX(),bpos2.getY(),bpos2.getZ()) < 7){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Unique
    public SoftAndWetPlunderBubbleEntity roundabout$getSoundPlunderedBubble(BlockPos blockPos){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!value.isRemoved() && value.isAlive()) {
                        BlockPos bpos2 = value.getBlockPos();
                        if (MainUtil.cheapDistanceTo(blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                bpos2.getX(),bpos2.getY(),bpos2.getZ()) < 5){
                            return value;
                        }
                    }
                }
            }
        }
        return null;
    }
    @Unique
    public boolean roundabout$isSoundPlundered(BlockPos blockPos){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.SOUND.id) {
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
    public SoftAndWetPlunderBubbleEntity roundabout$getSoundPlunderedBubbleEntity(Entity entity){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$entityPlunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!value.isRemoved() && value.isAlive() && entity !=null && value.getEntityStolen() == entity.getId()) {
                            return value;
                    }
                }
            }
        }
        return null;
    }
    @Unique
    public boolean roundabout$isSoundPlunderedEntity(Entity entity){
        roundabout$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(roundabout$entityPlunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!value.isRemoved() && value.isAlive() && entity !=null && value.getEntityStolen() == entity.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Inject(method = "playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$playSeededSound(Entity $$0, BlockPos $$1, SoundEvent $$2, SoundSource $$3, float $$4, float $$5, CallbackInfo ci) {
        if(this.roundabout$isSoundPlundered($$1)){
            SoftAndWetPlunderBubbleEntity sbpe = this.roundabout$getSoundPlunderedBubble($$1);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2, $$3, $$4, $$5);
            }
            ci.cancel();
        } else if(this.roundabout$isSoundPlunderedEntity($$0)){
            SoftAndWetPlunderBubbleEntity sbpe = this.roundabout$getSoundPlunderedBubbleEntity($$0);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2, $$3, $$4, $$5);
            }
            ci.cancel();
        }
    }
    @Inject(method = "playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$playSeededSound(Player $$0, Entity $$1, SoundEvent $$2, SoundSource $$3, float $$4, float $$5, CallbackInfo ci) {
        if(this.roundabout$isSoundPlunderedEntity($$1)){
            SoftAndWetPlunderBubbleEntity sbpe = this.roundabout$getSoundPlunderedBubbleEntity($$1);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2, $$3, $$4, $$5);
            }
            ci.cancel();
        }
    }
}

package net.hydra.jojomod.mixin;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public abstract class ZLevel implements ILevelAccess, LevelAccessor {

    @Shadow
    @Final
    protected List<TickingBlockEntity> blockEntityTickers;

    @Shadow
    public abstract boolean shouldTickBlocksAt(BlockPos blockPos);

    @Unique
    public List<SoftAndWetPlunderBubbleEntity> sba$plunderBubbles = new ArrayList<>();
    @Unique
    public List<SoftAndWetPlunderBubbleEntity> sba$entityPlunderBubbles = new ArrayList<>();

    @Unique
    public List<SoftAndWetPlunderBubbleEntity> sba$frictionBubbles = new ArrayList<>();
    @Unique
    public List<SoftAndWetPlunderBubbleEntity> sba$entityFrictionBubbles = new ArrayList<>();

    @Unique
    public void rdbt$skipTime(LivingEntity self,int ticks, int maxRange){
        if (((Level)(Object)this) instanceof ServerLevel serverLevel) {
            Position position = self.position();
            Iterator<TickingBlockEntity> iterator = this.blockEntityTickers.iterator();
            while (iterator.hasNext()) {
                TickingBlockEntity tickingBlockEntity = iterator.next();
                if (tickingBlockEntity == null){
                    continue;
                }
                if (tickingBlockEntity.isRemoved()) {
                    iterator.remove();
                    continue;
                }
                if (tickingBlockEntity.getPos() == null){
                    continue;
                }
                if (!this.shouldTickBlocksAt(tickingBlockEntity.getPos())) continue;

                BlockPos pos = tickingBlockEntity.getPos();
                if (MainUtil.cheapDistanceTo(position.x(), position.y(), position.z(),
                        pos.getX(), pos.getY(), pos.getZ()) < maxRange) {
                    BlockEntity be = this.getBlockEntity(tickingBlockEntity.getPos());
                    if (be == null || be.isRemoved()) {
                        break;
                    }
                    if (be instanceof AbstractFurnaceBlockEntity furnace) {
                        // skip furnace
                        for (int i = 0; i < ticks; i++) {
                            AbstractFurnaceBlockEntity.serverTick(
                                    serverLevel,
                                    furnace.getBlockPos(),
                                    furnace.getBlockState(),
                                    furnace
                            );
                        }
                    } else if (be instanceof BrewingStandBlockEntity brewing) {
                        // skip brewing
                        for (int i = 0; i < ticks; i++) {
                            BrewingStandBlockEntity.serverTick(
                                    serverLevel,
                                    brewing.getBlockPos(),
                                    brewing.getBlockState(),
                                    brewing
                            );
                        }
                    } else if (be instanceof CampfireBlockEntity campfireBlock) {
                        // campfire block
                        for (int i = 0; i < ticks; i++) {
                            CampfireBlockEntity.cookTick(
                                    serverLevel,
                                    campfireBlock.getBlockPos(),
                                    campfireBlock.getBlockState(),
                                    campfireBlock
                            );
                        }
                    }
                }
            }
        }
    }

    @Unique
    public void sba$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder){
        sba$bubbleInit();

        if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.FRICTION.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$frictionBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                sba$frictionBubbles.add(plunder);
            }
        } else if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.SOUND.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$plunderBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                sba$plunderBubbles.add(plunder);
            }
        }
    }
    @Unique
    public void sba$addPlunderBubbleEntity(SoftAndWetPlunderBubbleEntity plunder){
        sba$bubbleInit();

        if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.FRICTION.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityFrictionBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                sba$entityFrictionBubbles.add(plunder);
            }
        } else if (!plunder.getFinished() && plunder.getPlunderType() == PlunderTypes.SOUND.id) {
            List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityPlunderBubbles) {
            };
            if (!(!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder))) {
                sba$entityPlunderBubbles.add(plunder);
            }
        }
    }
    @Unique
    public void sba$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty() && bubbleIteration.contains(plunder)) {
            sba$plunderBubbles.remove(plunder);
        }
    }


    @Unique
    public void sba$bubbleInit(){
        if (sba$plunderBubbles == null) {
            sba$plunderBubbles = new ArrayList<>();
        } if (sba$entityPlunderBubbles == null) {
            sba$entityPlunderBubbles = new ArrayList<>();
        }if (sba$frictionBubbles == null) {
            sba$plunderBubbles = new ArrayList<>();
        } if (sba$entityFrictionBubbles == null) {
            sba$entityPlunderBubbles = new ArrayList<>();
        }
    }

    /**Offload Bubbles that are done*/
    @Unique
    public void sba$tickPlunderBubbleRemoval(){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$plunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (value.isRemoved() || !value.isAlive()) {
                    sba$plunderBubbles.remove(value);
                }
            }
        }
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration2 = new ArrayList<>(sba$entityPlunderBubbles) {
        };
        if (!bubbleIteration2.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration2) {
                if (value.isRemoved() || !value.isAlive()) {
                    sba$entityPlunderBubbles.remove(value);
                }
            }
        }
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration3 = new ArrayList<>(sba$entityFrictionBubbles) {
        };
        if (!bubbleIteration3.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration3) {
                if (value.isRemoved() || !value.isAlive()) {
                    sba$entityFrictionBubbles.remove(value);
                }
            }
        }
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration4 = new ArrayList<>(sba$frictionBubbles) {
        };
        if (!bubbleIteration4.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration4) {
                if (value.isRemoved() || !value.isAlive()) {
                    sba$frictionBubbles.remove(value);
                }
            }
        }
    }

    @Unique
    public boolean sba$isFrictionPlundered(BlockPos blockPos){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$frictionBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.FRICTION.id) {
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
    public SoftAndWetPlunderBubbleEntity sba$getSoundPlunderedBubble(BlockPos blockPos){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$plunderBubbles) {
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
    public boolean sba$isSoundPlundered(BlockPos blockPos){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$plunderBubbles) {
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
    public SoftAndWetPlunderBubbleEntity sba$getSoundPlunderedBubbleEntity(Entity entity){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityPlunderBubbles) {
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
    public void sba$raptureBubbles(BlockPos pos){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityPlunderBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!value.isRemoved() && value.isAlive()) {
                        BlockPos bpos2 = value.getBlockPos();
                        if (MainUtil.cheapDistanceTo(pos.getX(),pos.getY(),pos.getZ(),
                                bpos2.getX(),bpos2.getY(),bpos2.getZ()) < 50){
                            value.popBubble();
                        }
                    }
                }
            }
        }

        List<SoftAndWetPlunderBubbleEntity> bubbleIteration2 = new ArrayList<>(sba$plunderBubbles) {
        };
        if (!bubbleIteration2.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration2) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!value.isRemoved() && value.isAlive()) {
                        BlockPos bpos2 = value.getBlockPos();
                        if (MainUtil.cheapDistanceTo(pos.getX(),pos.getY(),pos.getZ(),
                                bpos2.getX(),bpos2.getY(),bpos2.getZ()) < 50){
                            value.popBubble();
                        }
                    }
                }
            }
        }
    }

    @Unique
    public boolean sba$isSoundPlunderedEntity(Entity entity){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityPlunderBubbles) {
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
    @Unique
    public boolean sba$isFrictionPlunderedEntity(Entity entity){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityFrictionBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.FRICTION.id) {
                    if (!value.isRemoved() && value.isAlive() && entity !=null && value.getEntityStolen() == entity.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    public boolean sba$isFrictionPlunderedEntity2(Entity entity){
        sba$bubbleInit();
        List<SoftAndWetPlunderBubbleEntity> bubbleIteration = new ArrayList<>(sba$entityFrictionBubbles) {
        };
        if (!bubbleIteration.isEmpty()) {
            for (SoftAndWetPlunderBubbleEntity value : bubbleIteration) {
                if (!value.getFinished() && value.getPlunderType() == PlunderTypes.FRICTION.id) {
                    if (!value.isRemoved() && value.isAlive() && entity !=null && value.getEntityStolen() == entity.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Inject(method = "playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", at = @At(value = "HEAD"), cancellable = true)
    private void sba$playSeededSound(Entity $$0, BlockPos $$1, SoundEvent $$2, SoundSource $$3, float $$4, float $$5, CallbackInfo ci) {
        if(this.sba$isSoundPlundered($$1)){
            SoftAndWetPlunderBubbleEntity sbpe = this.sba$getSoundPlunderedBubble($$1);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2, $$3, $$4, $$5);
            }
            ci.cancel();
        } else if(this.sba$isSoundPlunderedEntity($$0)){
            SoftAndWetPlunderBubbleEntity sbpe = this.sba$getSoundPlunderedBubbleEntity($$0);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2, $$3, $$4, $$5);
            }
            ci.cancel();
        }
    }
    @Inject(method = "playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", at = @At(value = "HEAD"), cancellable = true)
    private void sba$playSeededSound(Player $$0, Entity $$1, SoundEvent $$2, SoundSource $$3, float $$4, float $$5, CallbackInfo ci) {
        if(this.sba$isSoundPlunderedEntity($$1)){
            SoftAndWetPlunderBubbleEntity sbpe = this.sba$getSoundPlunderedBubbleEntity($$1);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2, $$3, $$4, $$5);
            }
            ci.cancel();
        }
    }

}
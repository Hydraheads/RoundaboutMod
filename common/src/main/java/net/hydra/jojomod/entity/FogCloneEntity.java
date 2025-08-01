package net.hydra.jojomod.entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public class FogCloneEntity extends CloneEntity {

    public int timer = 0;
    public FogCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public float lockedYRot = 0;
    @Override
    public float getSpeed() {
        if (this.getPlayer() != null){
            return this.getPlayer().getSpeed();
        }
        return super.getSpeed();
    }
    @Override
    public boolean canCollideWith(Entity $$0) {
        if (this.getPlayer() != null && this.getPlayer().is($$0)){
            return false;
        }

        return $$0.canBeCollidedWith() && !this.isPassengerOfSameVehicle($$0);
    }
    public void switchPlaces() {
        if (!this.level().isClientSide() && this.getPlayer() != null){
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            simulatePoof(new Vec3(this.getPlayer().getX(),
                    this.getPlayer().getY(),
                    this.getPlayer().getZ()));
            ((IPlayerEntityServer)this.getPlayer()).roundabout$setInvincibleTicks(5);
            ((ServerPlayer)this.getPlayer()).teleportTo(
                    (ServerLevel)this.level(),
                    x,
                    y,
                    z,
                    EnumSet.noneOf(RelativeMovement.class),
                    this.getYRot(),
                    this.getXRot()
            );
            packetNearby(new Vector3f((float) x, (float) y, (float) z));
            ((ServerPlayer)this.getPlayer()).isChangingDimension();
            this.discard();
        }
    }

    public final void packetNearby(Vector3f blip) {
        if (!this.level().isClientSide && this.getPlayer() != null) {
            ServerLevel serverWorld = ((ServerLevel) this.level());
            Vec3 userLocation = new Vec3(this.getX(),  this.getY(), this.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100) && !serverPlayerEntity.is(this.getPlayer())) {
                    S2CPacketUtil.sendBlipPacket(serverPlayerEntity, (byte) 2, this.getPlayer().getId(),blip);
                }
            }
        }
    }

    public int getTimer(){
        return timer;
    }
    public void setTimer(int timer){
        this.timer = timer;
    }
    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            this.setYRot(lockedYRot);
            this.setYHeadRot(lockedYRot);
            this.setYBodyRot(lockedYRot);
            this.yRotO = lockedYRot;
            this.yBodyRotO = lockedYRot;
            this.yHeadRot = lockedYRot;
            this.timer--;
            if (this.timer < 0) {
                this.goPoof();
                return;
            }

            nextPathfind--;
            if (nextPathfind <= 0) {
                doBasicPathfind();
                nextPathfind=10;
            }
        }
        super.tick();
    }

    public boolean addEffect(MobEffectInstance $$0, @Nullable Entity $$1) {
        if ($$1 != null){
            this.goPoof();
            return false;
        }
        return super.addEffect($$0,$$1);
    }

    int nextPathfind = 1;

    public void doBasicPathfind(){

        int range = 15;
        Vec3 vec3d = this.getEyePosition(0);
        Vec3 vec3d2 = this.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = this.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        BlockPos pos = blockHit.getBlockPos();
        this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 1);
    }

    @Override
    public void push(Entity $$0) {
        /**
         *
         Player safeLocal = ClientUtil.getPlayer();
         if (safeLocal != null && this.getPlayerUUID().isPresent() && safeLocal.getUUID().equals(this.getPlayerUUID().get())){
         return;
         }
         *
         * **/
        if (!this.level().isClientSide()){
            if (!($$0 instanceof FogCloneEntity) && !($$0 instanceof FallenMob) && $$0.isPushable()) {
                if (!(this.getPlayer() != null && this.getPlayer().is($$0))) {
                    goPoof();
                }
            }
        }
    }

    @Override
    public void doPush(Entity $$0) {
        if (!this.level().isClientSide()){
            if (!($$0 instanceof FogCloneEntity) && !($$0 instanceof FallenMob) && $$0.isPushable()){
                if (!(this.getPlayer() != null && this.getPlayer().is($$0))){
                    goPoof();
                }
            }
        }
    }
    @Override
    public boolean canBeCollidedWith() {
        if (this.level().isClientSide()){
            Player safeLocal = ClientUtil.getPlayer();
            if (safeLocal != null && this.getPlayerUUID().isPresent() && safeLocal.getUUID().equals(this.getPlayerUUID().get())){
                return false;
            }
        }

        return super.canBeCollidedWith();
    }

    public void goPoof(){
        simulatePoof(new Vec3(this.getX(),this.getY(),this.getZ()));
        this.discard();
    }

    public void simulatePoof(Vec3 vec){
        ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, vec.x(),
                vec.y()+this.getEyeHeight(), vec.z(),
                12, 0.2, 0.3, 0.2, 0.3);
        this.level().playSound(null,  vec.x(),
                vec.y(), vec.z(), ModSounds.POP_EVENT, this.getSoundSource(), 1.0F, (float)(1F+ Math.random()*0.03));
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        }
        goPoof();
        return false;
    }
}

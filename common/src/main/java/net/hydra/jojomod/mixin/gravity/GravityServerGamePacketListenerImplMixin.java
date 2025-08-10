package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerGamePacketListenerImpl.class)
public abstract class GravityServerGamePacketListenerImplMixin {


    @Shadow public abstract void teleport(double d, double e, double f, float g, float h);


    @Unique
    public ServerGamePacketListenerImpl rdbt$this(){
        return (ServerGamePacketListenerImpl)(Object)this;
    }

    @Inject(
            method = "handleMovePlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    private void roundabout$handleMovePlayer(ServerboundMovePlayerPacket $$0, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;
        ((IGravityEntity)this.player).rdbdt$setTaggedForFlip(true);
    }

    @Inject(
            method = "handleMoveVehicle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    private void roundabout$handleMoveVehicle(ServerboundMoveVehiclePacket $$0, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;
        Entity $$1 = this.player.getRootVehicle();
        ((IGravityEntity)$$1).rdbdt$setTaggedForFlip(true);
    }


    @Inject(
            method = "noBlocksAround",
            at = @At(
                    value = "HEAD",
                    target = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
            ),
            cancellable = true
    )
    private void roundabout$noBlocksAround(Entity $$0, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;

        Vec3 argVec = new Vec3(0.0, -0.55, 0.0);
        argVec = RotationUtil.vecWorldToPlayer(argVec, gravityDirection);

        cir.setReturnValue($$0.level().getBlockStates($$0.getBoundingBox().inflate(0.0625).expandTowards(argVec.x,argVec.y,argVec.z)).
                allMatch(BlockBehaviour.BlockStateBase::isAir));
    }


    @Shadow
    public ServerPlayer player;

    @Shadow
    private static double clampHorizontal(double d) {return 0;}

    ;

    @Shadow
    private static double clampVertical(double d) {return 0;}

    ;

    @Shadow
    private double lastGoodX;

    @Shadow
    private double lastGoodY;

    @Shadow
    private double lastGoodZ;
}

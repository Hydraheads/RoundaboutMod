package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerGamePacketListenerImpl.class)
public abstract class GravityServerGamePacketListenerImplMixin {


    @Shadow protected abstract boolean isPlayerCollidingWithAnythingNew(LevelReader levelReader, AABB aABB, double d, double e, double f);

    @Shadow @Final private MinecraftServer server;

    @Shadow protected abstract boolean noBlocksAround(Entity entity);

    @Shadow private boolean clientIsFloating;

    @Shadow protected abstract boolean isSingleplayerOwner();

    @Shadow @Final
    static Logger LOGGER;
    @Shadow private int knownMovePacketCount;
    @Shadow private int receivedMovePacketCount;
    @Shadow private double firstGoodZ;
    @Shadow private double firstGoodY;
    @Shadow private double firstGoodX;
    @Shadow private int awaitingTeleportTime;
    @Shadow @Nullable private Vec3 awaitingPositionFromClient;

    @Shadow public abstract void resetPosition();

    @Shadow private int tickCount;

    @Shadow public abstract void disconnect(Component component);

    @Shadow
    private static boolean containsInvalidValues(double d, double e, double f, float g, float h) {
        return false;
    }

    @Shadow public abstract void teleport(double d, double e, double f, float g, float h);


    @Unique
    public ServerGamePacketListenerImpl rdbt$this(){
        return (ServerGamePacketListenerImpl)(Object)this;
    }

    @Inject(
            method = "handleMovePlayer",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$handleMovePlayer(ServerboundMovePlayerPacket $$0, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();
        PacketUtils.ensureRunningOnSameThread($$0, (ServerGamePacketListenerImpl)(Object)this, this.player.serverLevel());
        if (containsInvalidValues($$0.getX(0.0), $$0.getY(0.0), $$0.getZ(0.0), $$0.getYRot(0.0F), $$0.getXRot(0.0F))) {
            this.disconnect(Component.translatable("multiplayer.disconnect.invalid_player_movement"));
        } else {
            ServerLevel $$1 = this.player.serverLevel();
            if (!this.player.wonGame) {
                if (this.tickCount == 0) {
                    this.resetPosition();
                }

                if (this.awaitingPositionFromClient != null) {
                    if (this.tickCount - this.awaitingTeleportTime > 20) {
                        this.awaitingTeleportTime = this.tickCount;
                        this.teleport(
                                this.awaitingPositionFromClient.x,
                                this.awaitingPositionFromClient.y,
                                this.awaitingPositionFromClient.z,
                                this.player.getYRot(),
                                this.player.getXRot()
                        );
                    }
                } else {
                    this.awaitingTeleportTime = this.tickCount;
                    double $$2 = clampHorizontal($$0.getX(this.player.getX()));
                    double $$3 = clampVertical($$0.getY(this.player.getY()));
                    double $$4 = clampHorizontal($$0.getZ(this.player.getZ()));
                    float $$5 = Mth.wrapDegrees($$0.getYRot(this.player.getYRot()));
                    float $$6 = Mth.wrapDegrees($$0.getXRot(this.player.getXRot()));
                    if (this.player.isPassenger()) {
                        this.player.absMoveTo(this.player.getX(), this.player.getY(), this.player.getZ(), $$5, $$6);
                        this.player.serverLevel().getChunkSource().move(this.player);
                    } else {
                        double $$7 = this.player.getX();
                        double $$8 = this.player.getY();
                        double $$9 = this.player.getZ();
                        double $$10 = $$2 - this.firstGoodX;
                        double $$11 = $$3 - this.firstGoodY;
                        double $$12 = $$4 - this.firstGoodZ;
                        double $$13 = this.player.getDeltaMovement().lengthSqr();
                        double $$14 = $$10 * $$10 + $$11 * $$11 + $$12 * $$12;
                        if (this.player.isSleeping()) {
                            if ($$14 > 1.0) {
                                this.teleport(this.player.getX(), this.player.getY(), this.player.getZ(), $$5, $$6);
                            }
                        } else {
                            this.receivedMovePacketCount++;
                            int $$15 = this.receivedMovePacketCount - this.knownMovePacketCount;
                            if ($$15 > 5) {
                                LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getName().getString(), $$15);
                                $$15 = 1;
                            }

                            if (!this.player.isChangingDimension()
                                    && (!this.player.level().getGameRules().getBoolean(GameRules.RULE_DISABLE_ELYTRA_MOVEMENT_CHECK) || !this.player.isFallFlying())
                            )
                            {
                                float $$16 = this.player.isFallFlying() ? 300.0F : 100.0F;
                                if ($$14 - $$13 > (double)($$16 * (float)$$15) && !this.isSingleplayerOwner()) {
                                    LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getName().getString(), $$10, $$11, $$12);
                                    this.teleport(this.player.getX(), this.player.getY(), this.player.getZ(), this.player.getYRot(), this.player.getXRot());
                                    return;
                                }
                            }

                            AABB $$17 = this.player.getBoundingBox();
                            $$10 = $$2 - this.lastGoodX;
                            $$11 = $$3 - this.lastGoodY;
                            $$12 = $$4 - this.lastGoodZ;
                            double c2 = clampHorizontal($$0.getX(this.player.getX()));
                            double c3 = clampVertical($$0.getY(this.player.getY()));
                            double c4 = clampHorizontal($$0.getZ(this.player.getZ()));
                            Vec3 myPositionVec = RotationUtil.vecWorldToPlayer(c2,c3,c4,gravityDirection);
                            Vec3 myLastPositionVec = RotationUtil.vecWorldToPlayer(lastGoodX,lastGoodY,lastGoodZ,gravityDirection);

                            boolean $$18 = (myPositionVec.y - myLastPositionVec.y > 0);

                            if (this.player.onGround() && !$$0.isOnGround() && $$18) {
                                this.player.jumpFromGround();
                            }

                            boolean $$19 = this.player.verticalCollisionBelow;
                            ((IGravityEntity)this.player).rdbdt$setTaggedForFlip(true);
                            this.player.move(MoverType.PLAYER, new Vec3($$10, $$11, $$12));
                            $$10 = $$2 - this.player.getX();
                            $$11 = $$3 - this.player.getY();

                            $$12 = $$4 - this.player.getZ();
                            $$14 = $$10 * $$10 + $$11 * $$11 + $$12 * $$12;
                            boolean $$21 = false;
                            if (!this.player.isChangingDimension()
                                    && $$14 > 0.5225
                                    && !this.player.isSleeping()
                                    && !this.player.gameMode.isCreative()
                                    && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                                $$21 = true;
                                LOGGER.warn("{} moved wrongly!", this.player.getName().getString());
                            }

                            if (this.player.noPhysics
                                    || this.player.isSleeping()
                                    || (!$$21 || !$$1.noCollision(this.player, $$17)) && !this.isPlayerCollidingWithAnythingNew($$1, $$17, $$2, $$3, $$4)) {
                                this.player.absMoveTo($$2, $$3, $$4, $$5, $$6);
                                this.clientIsFloating = $$11 >= -0.03125
                                        && !$$19
                                        && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR
                                        && !this.server.isFlightAllowed()
                                        && !this.player.getAbilities().mayfly
                                        && !this.player.hasEffect(MobEffects.LEVITATION)
                                        && !this.player.isFallFlying()
                                        && !this.player.isAutoSpinAttack()
                                        && this.noBlocksAround(this.player);
                                this.player.serverLevel().getChunkSource().move(this.player);
                                this.player.doCheckFallDamage(this.player.getX() - $$7, this.player.getY() - $$8, this.player.getZ() - $$9, $$0.isOnGround());
                                this.player
                                        .setOnGroundWithKnownMovement(
                                                $$0.isOnGround(), new Vec3(this.player.getX() - $$7, this.player.getY() - $$8, this.player.getZ() - $$9)
                                        );
                                if ($$18) {
                                    this.player.resetFallDistance();
                                }

                                this.player.checkMovementStatistics(this.player.getX() - $$7, this.player.getY() - $$8, this.player.getZ() - $$9);
                                this.lastGoodX = this.player.getX();
                                this.lastGoodY = this.player.getY();
                                this.lastGoodZ = this.player.getZ();
                            } else {
                                this.teleport($$7, $$8, $$9, $$5, $$6);
                                this.player.doCheckFallDamage(this.player.getX() - $$7, this.player.getY() - $$8, this.player.getZ() - $$9, $$0.isOnGround());
                            }
                        }
                    }
                }
            }
        }
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

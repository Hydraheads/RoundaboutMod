package net.hydra.jojomod.mixin.gravity.client;


import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public abstract class GravityClientPacketListener {
    @Shadow
    @Final
    private Minecraft minecraft;


    @Shadow private ClientLevel level;

    @Shadow
    @Final
    private RandomSource random;

    @Inject(
            method = "handleGameEvent(Lnet/minecraft/network/protocol/game/ClientboundGameEventPacket;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void redirect_onGameStateChange_getEyeY_0(ClientboundGameEventPacket $$0, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread($$0, (ClientPacketListener)(Object)this, this.minecraft);
        Player playerEntity = this.minecraft.player;
        Direction gravityDirection = GravityAPI.getGravityDirection(playerEntity);
        if (gravityDirection == Direction.DOWN)
            return;
        ClientboundGameEventPacket.Type $$2 = $$0.getEvent();
        if ($$2 == ClientboundGameEventPacket.ARROW_HIT_PLAYER) {
            ci.cancel();
            this.level.playSound(playerEntity, playerEntity.getEyePosition().x, playerEntity.getEyePosition().y,playerEntity.getEyePosition().x, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.18F, 0.45F);
        }
    }
    @Inject(
            method = "handleTakeItemEntity(Lnet/minecraft/network/protocol/game/ClientboundTakeItemEntityPacket;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$handleTakeItemEntity(ClientboundTakeItemEntityPacket $$0, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread($$0, (ClientPacketListener)(Object)this, this.minecraft);
        //d4c world merging items need to play pickup sounds in the right world...
        Entity entity = this.level.getEntity($$0.getItemId());
        if (PowerTypes.isExistentiallyElsewhere(entity)){
            ci.cancel();
            LivingEntity livingentity = (LivingEntity)this.level.getEntity($$0.getPlayerId());
            if (livingentity == null) {
                livingentity = this.minecraft.player;
            }

            if (entity != null) {
                if (!PowerTypes.isInADifferentExistenceNoTE(entity,Minecraft.getInstance().player)) {
                    if (entity instanceof ExperienceOrb) {
                        ClientUtil.playSoundWithInfo(this.level, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, (this.random.nextFloat() - this.random.nextFloat()) * 0.35F + 0.9F);
                    } else {
                        ClientUtil.playSoundWithInfo(this.level, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 1.4F + 2.0F);
                    }
                }

                this.minecraft.particleEngine.add(new ItemPickupParticle(this.minecraft.getEntityRenderDispatcher(), this.minecraft.renderBuffers(), this.level, entity, livingentity));
                if (entity instanceof ItemEntity) {
                    ItemEntity itementity = (ItemEntity)entity;
                    ItemStack itemstack = itementity.getItem();
                    if (!itemstack.isEmpty()) {
                        itemstack.shrink($$0.getAmount());
                    }

                    if (itemstack.isEmpty()) {
                        this.level.removeEntity($$0.getItemId(), Entity.RemovalReason.DISCARDED);
                    }
                } else if (!(entity instanceof ExperienceOrb)) {
                    this.level.removeEntity($$0.getItemId(), Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }
    @Inject(
            method = "handleExplosion",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void wrapOperation_onExplosion_add_0(ClientboundExplodePacket $$0, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread($$0, (ClientPacketListener)(Object)this, this.minecraft);
        Player playerEntity = this.minecraft.player;
        Direction gravityDirection = GravityAPI.getGravityDirection(playerEntity);
        if (gravityDirection == Direction.DOWN)
            return;

        ci.cancel();
        Vec3 player = RotationUtil.vecWorldToPlayer((double)$$0.getKnockbackX(), (double)$$0.getKnockbackY(), (double)$$0.getKnockbackZ(), gravityDirection);
        Explosion $$1 = new Explosion(this.minecraft.level, null, $$0.getX(), $$0.getY(), $$0.getZ(), $$0.getPower(), $$0.getToBlow());
        $$1.finalizeExplosion(true);
        this.minecraft
                .player
                .setDeltaMovement(
                        this.minecraft.player.getDeltaMovement().add(player.x,player.y,player.z)
                );
    }
}
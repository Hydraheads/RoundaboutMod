package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IInputEvents;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.client.gui.PauseTSScreen;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.item.FogBlockItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(value = Minecraft.class, priority = 100)
public abstract class InputEvents implements IInputEvents {

    protected InputEvents() {
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    public void roundaboutAttack(CallbackInfoReturnable<Boolean> ci) {

    }
    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    public void roundabout$entityGlowing(Entity $$0,CallbackInfoReturnable<Boolean> ci) {
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            if (powers.isPiloting()) {
                LivingEntity ent = powers.getPilotingStand();
                if (ent != null && powers instanceof PowersJustice){
                    if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                        ent.setYRot(camera.getYRot());
                        ent.setXRot(camera.getXRot());
                        ent.setYHeadRot(ent.getYRot());
                    }
                    Entity TE = MainUtil.rayCastEntity(ent,100);
                    if (TE != null && TE.is($$0)) {
                        ci.setReturnValue(true);
                        return;
                    }
                }
            }
        }

    }
    @Shadow
    @Final
    public Options options;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;
    @Shadow
    public int missTime;

    @Shadow
    @Final
    public ParticleEngine particleEngine;

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Unique
    public boolean roundabout$activeMining = false;

    @Shadow
    @Final
    public File gameDirectory;
    /** This class is in part for detecting and canceling mouse inputs during stand attacks.
     * Please note this should
     * only apply to non-enhancer stands while their physical body is out.*/
        @Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At("RETURN"))
        private void roundabout$initializeConfig(GameConfig $$0, CallbackInfo ci) {
            //handleInputEvents
        }


    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    public void roundabout$Attack(CallbackInfoReturnable<Boolean> ci) {
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            if (powers.isPiloting()){
                ci.setReturnValue(false);
                return;
            }

            boolean isMining = (standComp.roundabout$getActivePower() == PowerIndex.MINING);
            if (standComp.roundabout$isDazed() || ((TimeStop) player.level()).CanTimeStopEntity(player)) {
                ci.setReturnValue(true);
                return;
            } else if (standComp.roundabout$getActive() && standComp.roundabout$getStandPowers().interceptAttack()) {
                if (this.hitResult != null) {
                    boolean $$1 = false;
                    if (isMining) {
                        ci.setReturnValue(true);
                        return;
                    } else {
                        switch (this.hitResult.getType()) {
                            case BLOCK:
                                BlockHitResult $$2 = (BlockHitResult) this.hitResult;
                                BlockPos $$3 = $$2.getBlockPos();
                                if (!this.level.getBlockState($$3).isAir()) {
                                    this.gameMode.startDestroyBlock($$3, $$2.getDirection());
                                    if (this.level.getBlockState($$3).isAir()) {
                                        $$1 = true;
                                    }
                                    break;
                                }
                        }
                    }

                    if (ClientNetworking.getAppropriateConfig().disableMeleeWhileStandActive) {
                        ci.setReturnValue($$1);
                        return;
                    }
                }
            }
            //while (this.options.attackKey.wasPressed()) {
            //}
        }
    }

    private void roundabout$justiceContinueAttack(boolean $$0) {
        if (!$$0) {
            this.missTime = 0;
        }
        StandUser standComp = ((StandUser) player);
        StandPowers powers = standComp.roundabout$getStandPowers();
        StandEntity piloting = powers.getPilotingStand();
        HitResult $$47 = null;
        if (piloting != null && piloting.isAlive() && !piloting.isRemoved()){
            if (level != null) {
                double d0 = 10;
                $$47 = piloting.pick(d0, 0, false);
            }
        }
        if (this.missTime <= 0 && !this.player.isUsingItem()) {
            if ($$0 && $$47 != null && $$47.getType() == HitResult.Type.BLOCK) {
                BlockHitResult $$1 = (BlockHitResult)$$47;
                BlockPos $$2 = $$1.getBlockPos();
                if (!this.level.getBlockState($$2).isAir() && this.level.getBlockState($$2).getBlock() instanceof FogBlock) {
                    Direction $$3 = $$1.getDirection();
                    if (this.gameMode.continueDestroyBlock($$2, $$3)) {
                        this.particleEngine.crack($$2, $$3);
                        this.player.swing(InteractionHand.MAIN_HAND);
                    }
                }
            } else {
                this.gameMode.stopDestroyBlock();
            }
        }
    }

        @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutBlockBreak(boolean $$0, CallbackInfo ci) {
            if (player != null) {
                StandUser standComp = ((StandUser) player);

                StandPowers powers = standComp.roundabout$getStandPowers();
                if (powers.isPiloting()){
                    ci.cancel();
                    if (powers instanceof PowersJustice){
                        roundabout$justiceContinueAttack($$0);
                    }
                    return;
                }

                boolean isMining = (standComp.roundabout$getActivePower() == PowerIndex.MINING);
                if (standComp.roundabout$isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                    if (!$$0){
                        this.missTime = 0;
                    }
                    if (this.gameMode != null) {
                        this.gameMode.stopDestroyBlock();
                    }
                    ci.cancel();
                } else if (standComp.roundabout$getActive() && standComp.roundabout$getStandPowers().interceptAttack()){
                    if (isMining) {
                        if (!this.player.isUsingItem()) {
                            if ($$0 && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
                                roundabout$activeMining = true;
                                BlockHitResult $$1 = (BlockHitResult) this.hitResult;
                                BlockPos $$2 = $$1.getBlockPos();
                                if (!this.level.getBlockState($$2).isAir()) {
                                    Direction $$3 = $$1.getDirection();
                                    if (this.gameMode.continueDestroyBlock($$2, $$3)) {
                                        this.particleEngine.crack($$2, $$3);
                                    }
                                }
                            } else {
                                standComp.roundabout$tryPower(PowerIndex.NONE, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);

                                this.gameMode.stopDestroyBlock();
                            }
                        }
                        ci.cancel();
                    } else {
                        if (!this.options.keyAttack.isDown()){
                            roundabout$activeMining = false;
                        } else {
                            if (standComp.roundabout$getActivePower() == PowerIndex.NONE || standComp.roundabout$getAttackTimeDuring() == -1){
                                roundabout$activeMining = true;
                            } else {
                                ci.cancel();
                            }
                        }
                    }
                } else {
                    roundabout$activeMining = false;
                }
            }
        }
        @Inject(method = "startUseItem", at = @At("TAIL"), cancellable = true)
        public void roundabout$DoItemUse(CallbackInfo ci) {
            if (player != null) {
                roundabout$TryGuard();
            }
        }

        @Unique
        public boolean roundabout$TryGuard(){
            StandUser standComp = ((StandUser) player);
            if (standComp.roundabout$getActive() && standComp.roundabout$getStandPowers().interceptGuard()) {
                return standComp.roundabout$getStandPowers().preCheckButtonInputGuard(this.options.keyUse.isDown(),this.options);
            }
            return false;
        }

    @Unique
    float roundabout$pt = 0;
    @Unique
    float roundabout$tickDelta = 0;
    @Unique
    float roundabout$ppt = 0;

    @Inject(method = "runTick", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/FogRenderer;setupNoFog()V"), cancellable = true)
    public void roundabout$run(CallbackInfo ci) {
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {
            if (player != null && level != null) {
                boolean canTS = ((TimeStop) level).CanTimeStopEntity(player);
                if (canTS) {
                    roundabout$pt = this.timer.partialTick;
                    roundabout$tickDelta = this.timer.tickDelta;
                    roundabout$ppt = this.timer.partialTick;
                    this.timer.partialTick = 0;
                    this.timer.tickDelta = 0;
                    this.pausePartialTick = 0;
                }
            }
        }
    }
    @Inject(method = "runTick", at = @At(value = "TAIL"), cancellable = true)
    public void roundabout$run2(CallbackInfo ci) {
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {

            if (player != null && level != null) {
                boolean canTS = ((TimeStop) level).CanTimeStopEntity(player);
                if (canTS) {
                    this.timer.partialTick = roundabout$pt;
                    this.timer.tickDelta = roundabout$tickDelta;
                    this.pausePartialTick = roundabout$ppt;
                    roundabout$pt = 0;
                    roundabout$tickDelta = 0;
                    roundabout$ppt = 0;
                }
            }
        }
    }
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void roundabout$tickTick(CallbackInfo ci) {
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {
            if (player != null && level != null) {
                boolean canTS = ((TimeStop) level).CanTimeStopEntity(player);
                if (canTS) {
                    ClientUtil.wasFrozen = 5;
                    gui.tick(false);
                    ((StandUser)player).roundabout$getStandPowers().timeTick();


                    if (this.overlay == null && this.screen == null) {
                        this.handleKeybinds();
                        if (this.missTime > 0) {
                            this.missTime--;
                        }
                    }
                    keyboardHandler.tick();
                    ci.cancel();
                }
            }
        }
    }
    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    public void roundabout$tickTickTail(CallbackInfo ci) {
        if (!ClientUtil.getScreenFreeze() && ClientUtil.getWasFrozen()) {
            ClientUtil.wasFrozen -= 1;
        }
    }

    @Shadow
    public void setScreen(@javax.annotation.Nullable Screen $$0) {
    }


    @Unique
    public void roundabout$doItemUseWithJustice(){

        if (this.rightClickDelay == 0 && this.player != null && !this.player.isUsingItem() && this.level != null) {

            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            HitResult $$47 = null;
            if (piloting != null && piloting.isAlive() && !piloting.isRemoved()){
                if (level != null) {
                    double d0 = 10;
                    $$47 = piloting.pick(d0, 0, false);
                }
            }

            if (this.gameMode != null && !this.gameMode.isDestroying()) {
                this.rightClickDelay = 4;
                if (!this.player.isHandsBusy()) {

                    for (InteractionHand $$0 : InteractionHand.values()) {
                        ItemStack $$1 = this.player.getItemInHand($$0);
                        if (!$$1.isItemEnabled(this.level.enabledFeatures())) {
                            return;
                        }

                        if ($$47 != null && $$1.getItem() instanceof FogBlockItem) {
                            switch ($$47.getType()) {
                                case ENTITY:
                                    EntityHitResult $$2 = (EntityHitResult)$$47;
                                    Entity $$3 = $$2.getEntity();
                                    if (!this.level.getWorldBorder().isWithinBounds($$3.blockPosition())) {
                                        return;
                                    }

                                    InteractionResult $$4 = this.gameMode.interactAt(this.player, $$3, $$2, $$0);
                                    if (!$$4.consumesAction()) {
                                        $$4 = this.gameMode.interact(this.player, $$3, $$0);
                                    }

                                    if ($$4.consumesAction()) {
                                        if ($$4.shouldSwing()) {
                                            this.player.swing($$0);
                                        }

                                        return;
                                    }
                                    break;
                                case BLOCK:
                                    BlockHitResult $$5 = (BlockHitResult)$$47;
                                    int $$6 = $$1.getCount();
                                    InteractionResult $$7 = this.gameMode.useItemOn(this.player, $$0, $$5);
                                    if ($$7.consumesAction()) {
                                        if ($$7.shouldSwing()) {
                                            this.player.swing($$0);
                                            if (!$$1.isEmpty() && ($$1.getCount() != $$6 || this.gameMode.hasInfiniteItems())) {
                                                this.gameRenderer.itemInHandRenderer.itemUsed($$0);
                                            }
                                        }

                                        return;
                                    }

                                    if ($$7 == InteractionResult.FAIL) {
                                        return;
                                    }
                            }
                        }

                        if (!$$1.isEmpty()) {
                            InteractionResult $$8 = this.gameMode.useItem(this.player, $$0);
                            if ($$8.consumesAction()) {
                                if ($$8.shouldSwing()) {
                                    this.player.swing($$0);
                                }

                                this.gameRenderer.itemInHandRenderer.itemUsed($$0);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$DoItemUseCancel(CallbackInfo ci) {
        if (player != null) {


            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            if (powers.isPiloting()){
                ci.cancel();
                if (powers instanceof PowersJustice){
                    roundabout$doItemUseWithJustice();
                }
                return;
            }

            if (standComp.roundabout$isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                ci.cancel();
                return;
            } else if (standComp.roundabout$getActive()) {
                if (standComp.roundabout$isGuarding() || standComp.roundabout$isBarraging() || standComp.roundabout$isClashing() || standComp.roundabout$getStandPowers().cancelItemUse()) {
                    ci.cancel();
                    return;
                }
            }

            if (level != null) {
                if (!this.player.isHandsBusy()) {
                    if (this.hitResult != null) {

                        for (InteractionHand $$0 : InteractionHand.values()) {
                            ItemStack $$1 = this.player.getItemInHand($$0);
                if ((((IPlayerEntity)player).roundabout$getDodgeTime() > -1 ||
                                    ((StandUser)player).roundabout$getLeapTicks() > -1) &&
                                    ($$1.getItem().isEdible()
                                            || ($$1.getItem() instanceof PotionItem))){
                    ci.cancel();
                                return;
                            }
            }}}}


            if (level != null && ((StandUserClientPlayer) player).roundabout$getRoundaboutNoPlaceTSTicks() > -1) {
                if (!this.player.isHandsBusy()) {
                    if (this.hitResult != null) {

                        for (InteractionHand $$0 : InteractionHand.values()) {
                            ItemStack $$1 = this.player.getItemInHand($$0);
                            if (!$$1.isItemEnabled(this.level.enabledFeatures())) {
                                return;
                            }

                            if (this.hitResult != null) {
                                switch (this.hitResult.getType()) {
                                    case BLOCK:
                                        if ($$1.getItem() instanceof BlockItem) {
                                            Block block = ((BlockItem) $$1.getItem()).getBlock();
                                            if (block instanceof BedBlock || block instanceof WebBlock || block instanceof RespawnAnchorBlock
                                                    || block.defaultDestroyTime() > 20){
                                                roundabout$TryGuard();
                                                ci.cancel();
                                            }
                                        } else if ($$1.getItem() instanceof EndCrystalItem || $$1.getItem() instanceof MinecartItem
                                                || $$1.getItem() instanceof BucketItem){
                                            roundabout$TryGuard();
                                            ci.cancel();
                                        }
                                }
                            }
                        }
                    }
                }
            }

            if (level != null && ((TimeStop) level).isTimeStoppingEntity(player)) {
                if (!this.player.isHandsBusy() && ((StandUser)player).roundabout$getActivePower() <= PowerIndex.NONE) {
                    if (this.hitResult != null) {
                        if (this.rightClickDelay == 0) {
                            this.rightClickDelay = 5;
                            for (InteractionHand $$0 : InteractionHand.values()) {
                                ItemStack $$1 = this.player.getItemInHand($$0);
                                if (!$$1.isItemEnabled(this.level.enabledFeatures())) {
                                    return;
                                }

                                if (this.hitResult != null) {
                                    switch (this.hitResult.getType()) {
                                        case ENTITY:
                                            EntityHitResult $$2 = (EntityHitResult) this.hitResult;
                                            Entity $$3 = $$2.getEntity();
                                            if ($$3 instanceof LivingEntity) {
                                                roundabout$TryGuard();
                                                ci.cancel();

                                                if ((((IPlayerEntity) player).roundabout$getDodgeTime() > -1 ||
                                                        ((StandUser) player).roundabout$getLeapTicks() > -1) &&
                                                        ($$1.getItem().isEdible()
                                                                || ($$1.getItem() instanceof PotionItem))) {
                                                    return;
                                                }

                                                if (!$$1.isEmpty()) {
                                                    InteractionResult $$8 = this.gameMode.useItem(this.player, $$0);
                                                    if ($$8.consumesAction()) {
                                                        if ($$8.shouldSwing()) {
                                                            this.player.swing($$0);
                                                        }

                                                        this.gameRenderer.itemInHandRenderer.itemUsed($$0);
                                                        return;
                                                    }
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Shadow
    private int rightClickDelay;

    @Shadow protected abstract void startUseItem();

    @Unique
    private void roundabout$startUseOppositeItem() {

        if (!this.gameMode.isDestroying()) {
            this.rightClickDelay = 4;
            if (!this.player.isHandsBusy()) {
                if (this.hitResult == null) {
                }

                for (InteractionHand $$0 : InteractionHand.values()) {
                    if ($$0 == InteractionHand.MAIN_HAND){
                        $$0 = InteractionHand.OFF_HAND;
                    } else if ($$0 == InteractionHand.OFF_HAND){
                        $$0 = InteractionHand.MAIN_HAND;
                    }
                    ItemStack $$1 = this.player.getItemInHand($$0);
                    if (!$$1.isItemEnabled(this.level.enabledFeatures())) {
                        return;
                    }

                    if (this.hitResult != null) {
                        switch (this.hitResult.getType()) {
                            case ENTITY:
                                EntityHitResult $$2 = (EntityHitResult)this.hitResult;
                                Entity $$3 = $$2.getEntity();
                                if (!this.level.getWorldBorder().isWithinBounds($$3.blockPosition())) {
                                    return;
                                }

                                InteractionResult $$4 = this.gameMode.interactAt(this.player, $$3, $$2, $$0);
                                if (!$$4.consumesAction()) {
                                    $$4 = this.gameMode.interact(this.player, $$3, $$0);
                                }

                                if ($$4.consumesAction()) {
                                    if ($$4.shouldSwing()) {
                                        this.player.swing($$0);
                                    }

                                    return;
                                }
                                break;
                            case BLOCK:
                                BlockHitResult $$5 = (BlockHitResult)this.hitResult;
                                int $$6 = $$1.getCount();
                                InteractionResult $$7 = this.gameMode.useItemOn(this.player, $$0, $$5);
                                if ($$7.consumesAction()) {
                                    if ($$7.shouldSwing()) {
                                        this.player.swing($$0);
                                        if (!$$1.isEmpty() && ($$1.getCount() != $$6 || this.gameMode.hasInfiniteItems())) {
                                            this.gameRenderer.itemInHandRenderer.itemUsed($$0);
                                        }
                                    }

                                    return;
                                }

                                if ($$7 == InteractionResult.FAIL) {
                                    return;
                                }
                        }
                    }

                    if (!$$1.isEmpty()) {
                        InteractionResult $$8 = this.gameMode.useItem(this.player, $$0);
                        if ($$8.consumesAction()) {
                            if ($$8.shouldSwing()) {
                                this.player.swing($$0);
                            }

                            this.gameRenderer.itemInHandRenderer.itemUsed($$0);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Unique
    public ItemStack roundabout$getItemInOppositeHand(LivingEntity live, InteractionHand $$0) {
        if ($$0 == InteractionHand.MAIN_HAND) {
            return live.getItemBySlot(EquipmentSlot.OFFHAND);
        } else if ($$0 == InteractionHand.OFF_HAND) {
            return live.getItemBySlot(EquipmentSlot.MAINHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + $$0);
        }
    }

    @Unique
    public void roundabout$SetTSJump(boolean roundaboutTSJump){
        if (ClientNetworking.getAppropriateConfig().timeStopSettings.enableHovering){
            ((StandUser)player).roundabout$setTSJump(roundaboutTSJump);
            ModPacketHandler.PACKET_ACCESS.timeStopFloat(roundaboutTSJump);
        }
    }

    @javax.annotation.Nullable
    @Shadow
    public Screen screen;
    @javax.annotation.Nullable
    @Shadow
    private Overlay overlay;

    @Shadow
    private void handleKeybinds() {
    }

    @Shadow private static Minecraft instance;

    @Shadow @Final public Gui gui;

    @Shadow public abstract SoundManager getSoundManager();

    @Shadow @Final private Timer timer;
    @Shadow private volatile boolean pause;
    @Shadow private float pausePartialTick;
    @Shadow @Final public KeyboardHandler keyboardHandler;

    @Shadow public abstract RenderBuffers renderBuffers();

    @Shadow @Final private RenderBuffers renderBuffers;
    @Unique
    private static boolean roundabout$hasHandledBinds = false;

    @Inject(method = "tick", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",shift = At.Shift.BEFORE), cancellable = true)
    public void roundabout$forceGUI(CallbackInfo ci){
        if (this.screen instanceof NoCancelInputScreen) {
            this.handleKeybinds();
            if (this.missTime > 0) {
                this.missTime--;
            }
        }
    }

    @Inject(method = "getFrameTime", at = @At("HEAD"), cancellable = true)
    public void roundabout$getFrameTime(CallbackInfoReturnable<Float> cir){
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {
            if (player != null && level != null) {
                boolean canTS = ((TimeStop) level).CanTimeStopEntity(player);
                if (canTS) {
                   cir.setReturnValue(0F);
                }
            }
        }
    }

    @Inject(method = "getDeltaFrameTime", at = @At("HEAD"), cancellable = true)
    public void roundabout$getDeltaFrameTime(CallbackInfoReturnable<Float> cir){
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {
            if (player != null && level != null) {
                boolean canTS = ((TimeStop) level).CanTimeStopEntity(player);
                if (canTS) {
                        cir.setReturnValue(0F);
                }
            }
        }
    }
    @Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true)
    public void roundabout$Input(CallbackInfo ci){
        if (player != null) {

            if (player.isAlive()) {
                ((StandUser) player).roundabout$getStandPowers().updateGuard(
                        roundabout$sameKeyTwo(KeyInputRegistry.guardKey) || options.keyUse.isDown());
                //RoundaboutMod.LOGGER.info(""+client.options.forwardKey.isPressed());

                /**Time Stop Levitation*/
                boolean TSJumping = ((StandUser)player).roundabout$getTSJump();
                if (((TimeStop)player.level()).isTimeStoppingEntity(player)) {
                    if (player.getAbilities().flying && TSJumping) {
                        this.roundabout$SetTSJump(false);
                    } else {
                        if (TSJumping && player.onGround()) {
                            TSJumping = false;
                            this.roundabout$SetTSJump(false);
                        }
                        if (options.keyJump.isDown()) {
                            if (player.getDeltaMovement().y <= 0 && !player.onGround()) {
                                TSJumping = true;
                                this.roundabout$SetTSJump(true);
                            }
                        } else {
                            TSJumping = false;
                            this.roundabout$SetTSJump(false);
                        }
                    }
                } else {
                    if (TSJumping) {
                        this.roundabout$SetTSJump(false);
                    }
                }

                if (Poses.getPosFromByte(((IPlayerEntity) player).roundabout$GetPoseEmote()) != Poses.NONE){
                    if (options.keyUp.isDown() || options.keyDown.isDown() ||
                    options.keyLeft.isDown() || options.keyRight.isDown() || options.keyJump.isDown() ||
                    player.isUsingItem() || player.swinging || player.hurtTime > 0){
                        ((IPlayerEntity) player).roundabout$SetPos(Poses.NONE.id);
                        ModPacketHandler.PACKET_ACCESS.byteToServerPacket(Poses.NONE.id, PacketDataIndex.BYTE_STRIKE_POSE);
                    }
                }

                /*If you have a stand out, update the stand leaning attributes.
                 * Currently, strafe is reported, but unused.*/
                if (((StandUser) player).roundabout$getActive()) {
                    StandEntity stand = ((StandUser) player).roundabout$getStand();
                    if (stand != null) {
                        var mf = stand.getMoveForward();
                        byte forward = 0;
                        byte strafe = 0;
                        if (options.keyUp.isDown()) forward++;
                        if (options.keyDown.isDown()) forward--;
                        if (options.keyLeft.isDown()) strafe++;
                        if (options.keyRight.isDown()) strafe--;

                        if (mf != forward) {
                            ModPacketHandler.PACKET_ACCESS.moveSyncPacket(forward,strafe);
                        }
                    }
                }
                //RoundaboutMod.LOGGER.info("px");

                    //RoundaboutMod.LOGGER.info("px");
                    if (roundabout$sameKeyOne(KeyInputRegistry.summonKey)) {
                        //((IGameRenderer)this.gameRenderer).roundabout$loadEffect(new ResourceLocation("shaders/post/spider.json"));
                        KeyInputs.summonKey(player,((Minecraft) (Object) this));
                    }


                    KeyInputs.MoveKey1(player,((Minecraft) (Object) this), roundabout$sameKeyOne(KeyInputRegistry.abilityOneKey),
                        this.options);

                    KeyInputs.MoveKey2(player,((Minecraft) (Object) this), roundabout$sameKeyOne(KeyInputRegistry.abilityTwoKey),
                        this.options);

                    KeyInputs.MoveKey3(player,((Minecraft) (Object) this), roundabout$sameKeyOne(KeyInputRegistry.abilityThreeKey),
                        this.options);

                    KeyInputs.MoveKey4(player,((Minecraft) (Object) this), roundabout$sameKeyOne(KeyInputRegistry.abilityFourKey),
                            this.options);

                KeyInputs.showEXPKey(player,((Minecraft) (Object) this), roundabout$sameKeyThree(KeyInputRegistry.showExp),
                        this.options);
                KeyInputs.switchRowsKey(player,((Minecraft) (Object) this), roundabout$sameKeyThree(KeyInputRegistry.switchRow),
                        this.options);
                KeyInputs.strikePose(player,((Minecraft) (Object) this), KeyInputRegistry.pose.isDown(),
                        this.options);

                    if (KeyInputRegistry.menuKey.isDown()) {
                        KeyInputs.menuKey(player,((Minecraft) (Object) this));
                    }
                    if (KeyInputs.roundaboutClickCount > 0) {
                        KeyInputs.roundaboutClickCount--;
                    }


                if (player != null) {
                    if (roundabout$sameKeyTwo(KeyInputRegistry.guardKey) && !player.isUsingItem()) {

                        if (((StandUser) player).roundabout$getActive() && ((StandUser) player).roundabout$getStandPowers().interceptGuard()) {
                            if (roundabout$sameKeyTwo(KeyInputRegistry.guardKey)) {
                                roundabout$TryGuard();
                            }
                        } else {
                            if (!roundabout$sameKeyUseOverride(KeyInputRegistry.guardKey)) {
                                if (this.rightClickDelay == 0 && !this.player.isUsingItem()) {
                                    roundabout$startUseOppositeItem();
                                }
                            } else {
                                if (this.rightClickDelay == 0 && !this.player.isUsingItem()) {
                                    startUseItem();
                                }
                            }
                        }
                    }
                }

            }

            StandUser standComp = ((StandUser) player);
            if (!this.options.keyUse.isDown() && !roundabout$sameKeyOne(KeyInputRegistry.guardKey)) {
                if (standComp.roundabout$isGuarding() || standComp.roundabout$isBarraging() ||
                        standComp.roundabout$getStandPowers().clickRelease()) {
                        /*This code makes it so there is a slight delay between blocking and subsequent punch chain attacks.
                        * This delay exists so you can't right click left click chain for instant full power punches.*/
                   if (standComp.roundabout$getActivePowerPhase() > 0 ) {
                       standComp.roundabout$setInterruptCD(3);
                   }
                    standComp.roundabout$tryPower(PowerIndex.NONE,true);
                   ModPacketHandler.PACKET_ACCESS.StandGuardCancelClientPacket();
                }
            }

            StandPowers powers = standComp.roundabout$getStandPowers();
            if (standComp.roundabout$getActive() && !((TimeStop)player.level()).CanTimeStopEntity(player)) {
                boolean isMining = (standComp.roundabout$getActivePower() == PowerIndex.MINING)
                        || this.gameMode.isDestroying();
                if (this.options.keyAttack.isDown() && !player.isUsingItem()) {
                    if (powers.isMiningStand()){
                        Entity TE = standComp.roundabout$getTargetEntity(player, -1);
                        if (!isMining && TE == null && this.hitResult != null && !this.player.isHandsBusy()
                                && (standComp.roundabout$getActivePower() == PowerIndex.NONE || standComp.roundabout$getAttackTimeDuring() == -1)
                                && !standComp.roundabout$isGuarding()) {
                            boolean $$1 = false;
                            switch (this.hitResult.getType()) {
                                case BLOCK:
                                    BlockHitResult $$2 = (BlockHitResult) this.hitResult;
                                    BlockPos $$3 = $$2.getBlockPos();
                                    if (!this.level.getBlockState($$3).isAir()) {
                                            this.gameMode.startDestroyBlock($$3, $$2.getDirection());
                                        if (powers.canUseMiningStand()) {
                                            standComp.roundabout$tryPower(PowerIndex.MINING, true);
                                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.MINING);
                                        }
                                        isMining = true;
                                        break;
                                    }
                            }
                        }
                    }
                }
                powers.preCheckButtonInputUse(this.options.keyUse.isDown(),this.options);
                if (!isMining && !roundabout$activeMining && standComp.roundabout$getInterruptCD()) {
                    powers.preCheckButtonInputAttack(this.options.keyAttack.isDown(),this.options);
                }

                if (!isMining && standComp.roundabout$isGuarding() && !standComp.roundabout$isBarraging()){
                    powers.preCheckButtonInputBarrage(this.options.keyAttack.isDown(),this.options);
                }
            }
                //this.handleStandRush(this.currentScreen == null && this.options.attackKey.isPressed());
        }
    }

    @Override
    @Unique
    public boolean roundabout$sameKeyOne(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.options.keyLoadHotbarActivator) && this.options.keyLoadHotbarActivator.isDown())
                || (key1.same(this.options.keySaveHotbarActivator) && this.options.keySaveHotbarActivator.isDown())
        );
    }

    @Override
    @Unique
    public boolean roundabout$sameKeyTwo(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.options.keyPickItem) && this.options.keyPickItem.isDown())
                || (key1.same(this.options.keyUse) && this.options.keyUse.isDown()));
    }

    @Unique
    public boolean roundabout$sameKeyThree(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.options.keyPickItem) && this.options.keyPickItem.isDown()));
    }
    @Override
    @Unique
    public boolean roundabout$sameKeyUseOverride(KeyMapping key1){
        return (key1.same(this.options.keyUse));
    }
}

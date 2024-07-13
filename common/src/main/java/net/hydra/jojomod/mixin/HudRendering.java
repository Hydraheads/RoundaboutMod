package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IHudAccess;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public abstract class HudRendering implements IHudAccess {

    /** Code for when the client renders its huds*/
    @Shadow
    @Final
    @Mutable
    private Minecraft minecraft;
    @Shadow
    private int tickCount;
    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;

    @Unique
    private float flashAlpha = 0f;
    @Unique
    private float otherFlashAlpha = 0f;

    @Unique
    private boolean roundaboutRedo = false;

    @Shadow
    protected void renderTextureOverlay(GuiGraphics p_282304_, ResourceLocation p_281622_, float p_281504_) {
    }

    //private void renderHotbar(float tickDelta, DrawContext context) {


    /** The stand move HUD renders with the hotbar so that it may exist in all gamemodes.*/
    @Inject(method = "renderHotbar", at = @At(value = "TAIL"))
    private void renderHotbarMixin(float $$0, GuiGraphics $$1, CallbackInfo info) {
        StandHudRender.renderStandHud($$1, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()), flashAlpha, otherFlashAlpha);
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getDeltaFrameTime()F"))
    private void roundabout$renderOverlay(GuiGraphics $$0, float $$1, CallbackInfo ci) {
        if (this.minecraft.player != null) {
            if (Roundabout.renderGasOverlay) {
                int overlay = ((StandUser) this.minecraft.player).roundabout$getGasolineTime();
                if (overlay > 0) {
                    int overlayR = ((StandUser) this.minecraft.player).roundabout$getGasolineRenderTime();
                    float overlay2 = 0;
                    if (overlay <= 40) {
                        overlay2 = 0.5F - ((float) (40 - overlay) / 40) * 0.5F;
                    } else {
                        overlay2 = 0.5F - ((float) (40 - Math.min(overlayR, 40)) / 40) * 0.5F;
                    }
                    this.renderTextureOverlay($$0, StandIcons.GASOLINE_OVERLAY, overlay2);
                }
            }
            if (this.minecraft.options.getCameraType().isFirstPerson()) {
                if (((StandUser) this.minecraft.player).roundabout$getLocacacaCurse() == LocacacaCurseIndex.HEAD) {
                        this.renderTextureOverlay($$0, StandIcons.STONE_HEAD_OVERLAY, 1F);
                }
            }
        }
    }


    /** The guard HUD uses the exp bar, because you dont need to check exp
     *  while you are blocking, efficient HUD use.*/
    @Inject(method = "renderExperienceBar", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutRenderExperienceBar(GuiGraphics $$0, int $$1, CallbackInfo ci){
        if (roundaboutRenderBars($$0, $$1)){
            ci.cancel();
        }
    }
    @Inject(method = "renderJumpMeter", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutRenderMountJumpBar(PlayerRideableJumping $$0, GuiGraphics $$1, int $$2, CallbackInfo ci){
        if (roundaboutRenderBars($$1, $$2)){
            ci.cancel();
        }
    }


    @Shadow
    private void renderHearts(GuiGraphics $$0, Player $$1, int $$2, int $$3, int $$4, int $$5, float $$6, int $$7, int $$8, int $$9, boolean $$10) {}

    @Shadow
    private void renderPlayerHealth(GuiGraphics $$0) {}

    /**desaturate hearts when time is stopped*/
    @Inject(method = "renderPlayerHealth", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutRenderExperienceBar(GuiGraphics $$0, CallbackInfo ci){
        if (minecraft.player != null && minecraft.level != null) {
            if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player) && !roundaboutRedo) {
                roundaboutRedo = true;
                $$0.setColor(0.7F, 0.7F, 0.7F, 1.0F);
                renderPlayerHealth($$0);
                $$0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                roundaboutRedo = false;
                ci.cancel();
            }
        }
    }

    private boolean roundaboutRenderBars(GuiGraphics context, int x){
        if (minecraft.player != null && minecraft.level != null) {
            boolean isTSEntity = ((TimeStop) minecraft.level).isTimeStoppingEntity(minecraft.player);
            if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player)) {

                StandHudRender.renderTSHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, false, this.getFont());
                return true;
            } else if (((StandUser) minecraft.player).isClashing()) {
                ((StandUserClientPlayer) minecraft.player).setClashDisplayExtraTimestamp(this.minecraft.player.level().getGameTime());
                float c = (((StandUser) minecraft.player).getStandPowers().getClashProgress());
                ((StandUserClientPlayer) minecraft.player).setLastClashPower(c);
                StandHudRender.renderClashHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, c);
                return true;
            } else if (((StandUserClientPlayer) minecraft.player).getClashDisplayExtraTimestamp() >= this.minecraft.player.level().getGameTime() - 20) {
                StandHudRender.renderClashHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, ((StandUserClientPlayer) minecraft.player).getLastClashPower());
                return true;
            } else if (((StandUser) minecraft.player).isGuarding() || (((StandUser) minecraft.player).getGuardPoints() < ((StandUser) minecraft.player).getMaxGuardPoints()
            && !isTSEntity)) {
                StandHudRender.renderGuardHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha);
                return true;
            } else if (isTSEntity || (((StandUser) minecraft.player).getStandPowers().getMaxTSTime() > 0
                    && (((StandUser) minecraft.player).getStandPowers().getActivePower() == PowerIndex.SPECIAL) ||  ((StandUser) minecraft.player).getStandPowers().getActivePower() == PowerIndex.LEAD_IN)) {

                StandHudRender.renderTSHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, true, this.getFont());
                return true;
            }
        }
        return false;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "TAIL"))
    private void renderCrosshairMixin(GuiGraphics $$0, CallbackInfo info) {
        StandHudRender.renderAttackHud($$0, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()), flashAlpha, otherFlashAlpha);
    }

    @Shadow
    private Player getCameraPlayer() {
        return null;
    }

    @Shadow
    private LivingEntity getPlayerVehicleWithHealth() {
        return null;
    }

    @Shadow
    private int getVehicleMaxHearts(LivingEntity $$0) {
        return 0;
    }

    @Shadow public abstract Font getFont();

    @Override
    public void setFlashAlpha(float flashAlpha) {
        this.flashAlpha = flashAlpha;
    }

    @Override
    public void setOtherFlashAlpha(float otherFlashAlpha) {
        this.otherFlashAlpha = otherFlashAlpha;
    }
//    public static final String DEBUG_TEXT_1 = "hud.roundabout.standout";
}

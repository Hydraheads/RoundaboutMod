package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IHudAccess;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
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
    @Final
    private RandomSource random;


    @Shadow
    protected void renderTextureOverlay(GuiGraphics p_282304_, ResourceLocation p_281622_, float p_281504_) {
    }

    //private void renderHotbar(float tickDelta, DrawContext context) {


    /** The stand move HUD renders with the hotbar so that it may exist in all gamemodes.*/
    @Inject(method = "renderHotbar", at = @At(value = "TAIL"))
    private void renderHotbarMixin(float $$0, GuiGraphics $$1, CallbackInfo info) {
        StandHudRender.renderStandHud($$1, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()), flashAlpha, otherFlashAlpha);
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void roundabout$renderOverlay(GuiGraphics $$0, float $$1, CallbackInfo ci) {
        /*This does not work on forge becasue the forcefully overwrite this function*/
    }

    @Inject(method = "renderHearts", at = @At(value = "TAIL"))
    private void renderHotbarMixin(GuiGraphics $$0, Player $$1, int $$2, int $$3, int $$4, int $$5, float $$6, int $$7, int $$8, int $$9, boolean $$10, CallbackInfo ci) {
        if (((StandUser)$$1).roundabout$getLocacacaCurse()== LocacacaCurseIndex.HEART){
            int $$12 = 208;
            int $$13 = Mth.ceil((double)$$6 / 2.0);
            int $$14 = Mth.ceil((double)$$9 / 2.0);
            int $$15 = $$13 * 2;

            for (int $$16 = $$13 + $$14 - 1; $$16 >= 0; $$16--) {
                int $$17 = $$16 / 10;
                int $$18 = $$16 % 10;
                int $$19 = $$2 + $$18 * 8;
                int $$20 = $$3 - $$17 * $$4;
                if ($$7 + $$9 <= 4) {
                    $$20 += this.random.nextInt(2);
                }

                if ($$16 < $$13 && $$16 == $$5) {
                    $$20 -= 2;
                }
                int $$21 = $$16 * 2;

                if ($$10 && $$21 < $$8) {
                    boolean $$25 = $$21 + 1 == $$8;
                    boolean $$22 = $$16 >= $$13;
                        this.roundabout$renderStoneHeart($$0, $$19, $$20, $$12, true, $$25);
                }

                if ($$21 < $$7) {
                    boolean $$26 = $$21 + 1 == $$7;
                        this.roundabout$renderStoneHeart($$0, $$19, $$20, $$12, false, $$26);
                }
            }
        }
        if ($$1.hasEffect(ModEffects.STAND_VIRUS)){

            int $$12 = 216;
            int $$13 = Mth.ceil((double)$$6 / 2.0);
            int $$14 = Mth.ceil((double)$$9 / 2.0);
            int $$15 = $$13 * 2;

            for (int $$16 = $$13 + $$14 - 1; $$16 >= 0; $$16--) {
                int $$17 = $$16 / 10;
                int $$18 = $$16 % 10;
                int $$19 = $$2 + $$18 * 8;
                int $$20 = $$3 - $$17 * $$4;
                if ($$7 + $$9 <= 4) {
                    $$20 += this.random.nextInt(2);
                }

                if ($$16 < $$13 && $$16 == $$5) {
                    $$20 -= 2;
                }
                int $$21 = $$16 * 2;

                if ($$10 && $$21 < $$8) {
                    boolean $$25 = $$21 + 1 == $$8;
                    boolean $$22 = $$16 >= $$13;
                    this.roundabout$renderStoneHeart($$0, $$19, $$20, $$12, true, $$25);
                }

                if ($$21 < $$7) {
                    boolean $$26 = $$21 + 1 == $$7;
                    this.roundabout$renderStoneHeart($$0, $$19, $$20, $$12, false, $$26);
                }
            }
        }
    }

    @Unique
    private void roundabout$renderStoneHeart(GuiGraphics p_283024_, int p_283636_, int p_283279_, int p_283188_, boolean p_283440_, boolean p_282496_) {
        p_283024_.blit(StandIcons.JOJO_ICONS, p_283636_, p_283279_, roundabout$getXShift(p_282496_, p_283440_), p_283188_, 9, 9);
    }

    @Unique
    public int roundabout$getXShift(boolean p_168735_, boolean p_168736_) {
        int i;
        int j = p_168735_ ? 1 : 0;
        int k = p_168736_ ? 2 : 0;
        i = j + k;

        return 16 + (i * 9);
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
    public void roundabout$renderHealth(GuiGraphics $$0, CallbackInfo ci){
        if (minecraft.player != null && minecraft.level != null && !roundaboutRedo) {
            if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player)) {
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
                        this.renderTextureOverlay(context, StandIcons.GASOLINE_OVERLAY, overlay2);
                    }
                }
                if (this.minecraft.options.getCameraType().isFirstPerson()) {
                    if (((StandUser) this.minecraft.player).roundabout$getLocacacaCurse() == LocacacaCurseIndex.HEAD) {
                        this.renderTextureOverlay(context, StandIcons.STONE_HEAD_OVERLAY, 1F);
                    }
                }
            }




            boolean isTSEntity = ((TimeStop) minecraft.level).isTimeStoppingEntity(minecraft.player);
            if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player)) {

                StandHudRender.renderTSHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, false, this.getFont());
                return true;
            } else if (((StandUser) minecraft.player).roundabout$isClashing()) {
                ((StandUserClientPlayer) minecraft.player).setClashDisplayExtraTimestamp(this.minecraft.player.tickCount);
                float c = (((StandUser) minecraft.player).roundabout$getStandPowers().getClashProgress());
                ((StandUserClientPlayer) minecraft.player).setLastClashPower(c);
                StandHudRender.renderClashHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, c);
                return true;
            } else if (((StandUserClientPlayer) minecraft.player).getClashDisplayExtraTimestamp() >= minecraft.player.tickCount - 20) {
                StandHudRender.renderClashHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, ((StandUserClientPlayer) minecraft.player).getLastClashPower());
                return true;
            } else if (((StandUser) minecraft.player).roundabout$isGuarding() || (((StandUser) minecraft.player).roundabout$getGuardPoints() < ((StandUser) minecraft.player).roundabout$getMaxGuardPoints()
            && !isTSEntity)) {
                StandHudRender.renderGuardHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha);
                return true;
            } else if (isTSEntity || (((StandUser) minecraft.player).roundabout$getStandPowers().getMaxTSTime() > 0
                    && (((StandUser) minecraft.player).roundabout$getStandPowers().getActivePower() == PowerIndex.SPECIAL) ||  ((StandUser) minecraft.player).roundabout$getStandPowers().getActivePower() == PowerIndex.LEAD_IN)) {

                StandHudRender.renderTSHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha, true, this.getFont());
                return true;
            } else if (minecraft.player.getVehicle() != null && minecraft.player.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()){

                StandHudRender.renderGrabbedHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, flashAlpha, otherFlashAlpha);
                return true;
            }
        }
        return false;
    }


    @Inject(method = "renderExperienceBar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER),
            cancellable = true)
    public void roundabout$renderExperienceBar2(GuiGraphics $$0, int $$1, CallbackInfo ci){
        if (((StandUser) minecraft.player).roundabout$getLeapTicks() > -1){
            int k = screenWidth/2 - 5;
            int l = screenHeight - 31 - 5;
            $$0.blit(StandIcons.JOJO_ICONS, k, l, 183, 40, 9, 9);
            ci.cancel();
        }
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

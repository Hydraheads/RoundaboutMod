package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IHudAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.event.powers.visagedata.JosukePartEightVisage;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.phys.Vec3;
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
    private float roundabout$flashAlpha = 0f;
    @Unique
    private float roundabout$otherFlashAlpha = 0f;

    @Unique
    private boolean roundabout$Redo = false;

    @Shadow
    @Final
    private RandomSource random;


    @Shadow
    protected void renderTextureOverlay(GuiGraphics p_282304_, ResourceLocation p_281622_, float p_281504_) {
    }

    //private void renderHotbar(float tickDelta, DrawContext context) {


    private float rdbt$currentAlpha = 0.0F;
    private boolean rdbt$fadingIn = false;

    /** The stand move HUD renders with the hotbar so that it may exist in all gamemodes.*/
    @Inject(method = "renderHotbar", at = @At(value = "HEAD"))
    private void roundabout$renderHotbarMixin(float $$0, GuiGraphics $$1, CallbackInfo info) {


        if (this.minecraft.player != null) {
            float tsdelta = ClientUtil.getDelta();
            tsdelta = tsdelta % 1;

            if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player)) {
                tsdelta = 0;
            }
            StandUser user = ((StandUser) this.minecraft.player);
            boolean renderGasOverlay = ConfigManager.getClientConfig().renderGasSplatterOverlay;
            if (renderGasOverlay) {
                int overlay = user.roundabout$getGasolineTime();
                if (overlay > 0) {
                    int overlayR = user.roundabout$getGasolineRenderTime();
                    float overlay2 = 0;
                    if (overlay <= 40) {
                        overlay2 = 0.5F - ((float) (40 - overlay) / 40) * 0.5F;
                    } else {
                        overlay2 = 0.5F - ((float) (40 - Math.min(overlayR, 40)) / 40) * 0.5F;
                    }
                    RenderSystem.enableBlend();
                    this.renderTextureOverlay($$1, StandIcons.GASOLINE_OVERLAY, overlay2);
                }
            }
            if (((StandUser) this.minecraft.player ).roundabout$getStandPowers() instanceof PowersRatt) {
                if ( ((StandUser) this.minecraft.player).roundabout$getStandPowers().scopeLevel != 0 ) {
                    RenderSystem.enableBlend();
                    this.renderTextureOverlay($$1, StandIcons.RATT_SCOPE_OVERLAY, 0.99F);
                }
            }
            if (this.minecraft.options.getCameraType().isFirstPerson()) {
                if (FateTypes.hasBloodHunger(this.minecraft.player)){
                    // Fade speed per tick — lower = slower fade
                    float fadeStep = 1.0F / 30.0F; // same as before: full fade over ~30 ticks

                    boolean checksOut = false;
                    long timeOfDay = this.minecraft.level.getDayTime() % 24000L;
                    boolean isDay = timeOfDay < 12000L; // 0–12000 = day, 12000–24000 = night

                    if (this.minecraft.player.level().dimension().location().getPath().equals("overworld") &&
                            isDay) {
                        Vec3 yes = this.minecraft.player.getEyePosition();
                        int range = 3;
                        for (var i = -range; i <= range; i++) {
                            for (var j = -range; j <= range; j++) {
                                if (!(i == 0 || j == 0)) {
                                    if (this.minecraft.player.level().canSeeSky(BlockPos.containing(yes.add(i,0,j)))){
                                        checksOut = true;
                                    }
                                }
                            }
                        }
                    }

                    if (checksOut) {
                        rdbt$fadingIn = true;
                    } else {
                        rdbt$fadingIn = false;
                    }
                    // Smoothly approach target
                    if (rdbt$fadingIn) {
                        rdbt$currentAlpha = Mth.clamp(rdbt$currentAlpha + fadeStep, 0.0F, 1.0F);
                    } else {
                        rdbt$currentAlpha = Mth.clamp(rdbt$currentAlpha - fadeStep, 0.0F, 1.0F);
                    }

                    // Only render if visible
                    if (rdbt$currentAlpha > 0.01F) {
                        RenderSystem.enableBlend();
                        this.renderTextureOverlay($$1, StandIcons.SUN_TINGE_OVERLAY, rdbt$currentAlpha);
                    }

                }
                if (user.roundabout$isBubbleEncased()){
                    RenderSystem.enableBlend();
                    this.renderTextureOverlay($$1, StandIcons.IN_BUBBLE_OVERLAY, 0.99F);
                }
                if (MainUtil.isWearingStoneMask(this.minecraft.player)){
                    RenderSystem.enableBlend();
                    this.renderTextureOverlay($$1, StandIcons.STONE_MASK_OVERLAY, 0.6F);
                }
                if (MainUtil.isWearingBloodyStoneMask(this.minecraft.player)){
                    RenderSystem.enableBlend();
                    this.renderTextureOverlay($$1, StandIcons.BLOODY_MASK_OVERLAY, 0.55F);
                }
                if (user.roundabout$getLocacacaCurse() == LocacacaCurseIndex.HEAD) {
                    if (((IPlayerEntity) this.minecraft.player).roundabout$getMaskSlot() != null &&
                            !((IPlayerEntity) this.minecraft.player).roundabout$getMaskSlot().isEmpty() &&
                            ((IPlayerEntity) this.minecraft.player).roundabout$getMaskSlot().getItem() instanceof MaskItem ME &&
                            ME.visageData instanceof JosukePartEightVisage){
                        RenderSystem.enableBlend();
                        this.renderTextureOverlay($$1, StandIcons.STONE_HEAD_OVERLAY_JOSUKE, 1F);
                    } else {
                        RenderSystem.enableBlend();
                        this.renderTextureOverlay($$1, StandIcons.STONE_HEAD_OVERLAY, 1F);
                    }
                }

                float ticks = user.roundabout$getZappedTicks();
                if (ticks > -1){
                    if (user.roundabout$getZappedToID() > -1){
                        ticks+=tsdelta;
                    } else {
                        ticks-=tsdelta;
                    }
                    ticks = Mth.clamp(ticks,0,10);
                    ticks*=0.1F;
                    RenderSystem.enableBlend();
                    roundabout$renderTextureOverlay($$1, StandIcons.SURVIVOR_ANGER, ticks*0.6F,1F,1F,1F);
                }
            }

             StandPowers powers = user.roundabout$getStandPowers();
                if (powers.timeRewindOverlayTicks > -1) {
                    RenderSystem.enableBlend();
                    this.renderTextureOverlay($$1, StandIcons.TIME_REWIND, powers.getOverlayFromOverlayTicks($$0));
                }

        }
        StandHudRender.renderStandHud($$1, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()), roundabout$flashAlpha, roundabout$otherFlashAlpha);

        RenderSystem.enableBlend();
    }


    private void roundabout$renderTextureOverlay(GuiGraphics $$0, ResourceLocation $$1, float opacity, float r, float g, float b) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        $$0.setColor(r, g, b, opacity);
        $$0.blit($$1, 0, 0, -90, 0.0F, 0.0F, this.screenWidth, this.screenHeight, this.screenWidth, this.screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        $$0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void roundabout$renderOverlay(GuiGraphics $$0, float $$1, CallbackInfo ci) {
        /*This does not work on forge becasue the forcefully overwrite this function*/
    }

    @Inject(method = "renderHearts", at = @At(value = "TAIL"))
    private void roundabout$renderHotbarMixin(GuiGraphics $$0, Player $$1, int $$2, int $$3, int $$4, int $$5, float $$6, int $$7, int $$8, int $$9, boolean $$10, CallbackInfo ci) {
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
    public void roundabout$RenderExperienceBar(GuiGraphics $$0, int $$1, CallbackInfo ci){
        if (roundabout$RenderBars($$0, $$1)){
            ci.cancel();
        }
    }
    @Inject(method = "renderJumpMeter", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$RenderMountJumpBar(PlayerRideableJumping $$0, GuiGraphics $$1, int $$2, CallbackInfo ci){
        if (roundabout$RenderBars($$1, $$2)){
            ci.cancel();
        }
    }


    @Shadow
    private void renderHearts(GuiGraphics $$0, Player $$1, int $$2, int $$3, int $$4, int $$5, float $$6, int $$7, int $$8, int $$9, boolean $$10) {}

    @Shadow
    private void renderPlayerHealth(GuiGraphics $$0) {}

    @Shadow
    private int getVisibleVehicleHeartRows(int $$0) {
        return 0;
    }

        /**desaturate hearts when time is stopped*/
    @Inject(method = "renderPlayerHealth", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderHealth(GuiGraphics $$0, CallbackInfo ci){
        if (minecraft.player != null && minecraft.level != null){
           if (!roundabout$Redo) {
                if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player)) {
                    roundabout$Redo = true;
                    $$0.setColor(0.7F, 0.7F, 0.7F, 1.0F);
                    renderPlayerHealth($$0);
                    $$0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    roundabout$Redo = false;
                    ci.cancel();
                    return;
                }
           }
        }
    }


    @Inject(method = "renderPlayerHealth", at = @At(value = "HEAD"), cancellable = true)
    private void rdbt$renderPlayerHealthX(GuiGraphics $$0, CallbackInfo ci) {

        Player $$1 = this.getCameraPlayer();
        if ($$1 != null) {
            if (!FateTypes.hasBloodHunger($$1))
                return;
            ci.cancel();
            int $$2 = Mth.ceil($$1.getHealth());
            boolean $$3 = this.healthBlinkTime > (long)this.tickCount && (this.healthBlinkTime - (long)this.tickCount) / 3L % 2L == 1L;
            long $$4 = Util.getMillis();
            if ($$2 < this.lastHealth && $$1.invulnerableTime > 0) {
                this.lastHealthTime = $$4;
                this.healthBlinkTime = (long)(this.tickCount + 20);
            } else if ($$2 > this.lastHealth && $$1.invulnerableTime > 0) {
                this.lastHealthTime = $$4;
                this.healthBlinkTime = (long)(this.tickCount + 10);
            }

            if ($$4 - this.lastHealthTime > 1000L) {
                this.lastHealth = $$2;
                this.displayHealth = $$2;
                this.lastHealthTime = $$4;
            }

            this.lastHealth = $$2;
            int $$5 = this.displayHealth;
            this.random.setSeed((long)(this.tickCount * 312871));
            FoodData $$6 = $$1.getFoodData();
            int $$7 = $$6.getFoodLevel();
            int $$8 = this.screenWidth / 2 - 91;
            int $$9 = this.screenWidth / 2 + 91;
            int $$10 = this.screenHeight - 39;
            float $$11 = Math.max((float)$$1.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max($$5, $$2));
            int $$12 = Mth.ceil($$1.getAbsorptionAmount());
            int $$13 = Mth.ceil(($$11 + (float)$$12) / 2.0F / 10.0F);
            int $$14 = Math.max(10 - ($$13 - 2), 3);
            int $$15 = $$10 - ($$13 - 1) * $$14 - 10;
            int $$16 = $$10 - 10;
            int $$17 = $$1.getArmorValue();
            int $$18 = -1;
            if ($$1.hasEffect(MobEffects.REGENERATION)) {
                $$18 = this.tickCount % Mth.ceil($$11 + 5.0F);
            }

            this.minecraft.getProfiler().push("armor");

            for (int $$19 = 0; $$19 < 10; $$19++) {
                if ($$17 > 0) {
                    int $$20 = $$8 + $$19 * 8;
                    if ($$19 * 2 + 1 < $$17) {
                        $$0.blit(GUI_ICONS_LOCATION, $$20, $$15, 34, 9, 9, 9);
                    }

                    if ($$19 * 2 + 1 == $$17) {
                        $$0.blit(GUI_ICONS_LOCATION, $$20, $$15, 25, 9, 9, 9);
                    }

                    if ($$19 * 2 + 1 > $$17) {
                        $$0.blit(GUI_ICONS_LOCATION, $$20, $$15, 16, 9, 9, 9);
                    }
                }
            }

            this.minecraft.getProfiler().popPush("health");
            this.renderHearts($$0, $$1, $$8, $$10, $$14, $$18, $$11, $$2, $$5, $$12, $$3);
            LivingEntity $$21 = this.getPlayerVehicleWithHealth();
            int $$22 = this.getVehicleMaxHearts($$21);
            if ($$22 == 0) {
                this.minecraft.getProfiler().popPush("food");
                ClientUtil.renderHungerStuff($$0,$$1,$$9,$$10,this.random.nextInt(3),$$7,this.tickCount);

                $$16 -= 10;
            }

            this.minecraft.getProfiler().popPush("air");
            int $$28 = $$1.getMaxAirSupply();
            int $$29 = Math.min($$1.getAirSupply(), $$28);
            if ($$1.isEyeInFluid(FluidTags.WATER) || $$29 < $$28) {
                int $$30 = this.getVisibleVehicleHeartRows($$22) - 1;
                $$16 -= $$30 * 10;
                int $$31 = Mth.ceil((double)($$29 - 2) * 10.0 / (double)$$28);
                int $$32 = Mth.ceil((double)$$29 * 10.0 / (double)$$28) - $$31;

                for (int $$33 = 0; $$33 < $$31 + $$32; $$33++) {
                    if ($$33 < $$31) {
                        $$0.blit(GUI_ICONS_LOCATION, $$9 - $$33 * 8 - 9, $$16, 16, 18, 9, 9);
                    } else {
                        $$0.blit(GUI_ICONS_LOCATION, $$9 - $$33 * 8 - 9, $$16, 25, 18, 9, 9);
                    }
                }
            }

            this.minecraft.getProfiler().pop();
        }
        rdbt$renderHealth2Common($$0);
    }

    @Unique
    public void rdbt$renderHealth2Common(GuiGraphics $$0){
        if (minecraft.player != null && minecraft.level != null){
            int oxygenBonus = ((StandUser)minecraft.player).roundabout$getStandPowers().getAirAmount();
            int maxOxygenBonus = ((StandUser)minecraft.player).roundabout$getStandPowers().getMaxAirAmount();
            if (oxygenBonus > -1 && ((StandUser)minecraft.player).roundabout$getActive()) {
                int $$28 = minecraft.player.getMaxAirSupply();
                int $$29 = Math.min(minecraft.player.getAirSupply(), $$28);
                if (minecraft.player.isEyeInFluid(FluidTags.WATER) || $$29 < $$28 || oxygenBonus < maxOxygenBonus) {
                    LivingEntity $$21 = this.getPlayerVehicleWithHealth();
                    int $$22 = this.getVehicleMaxHearts($$21);
                    int $$30 = this.getVisibleVehicleHeartRows($$22) - 1;
                    int $$9 = this.screenWidth / 2 + 6;
                    int $$10 = this.screenHeight - 39;
                    int $$16 = $$10 - 10;
                    $$16 -= $$30 * 10;

                    if ($$22 == 0) {
                        $$16 -= 10;
                    }

                    int airWidth = (int) Math.floor(((double) 81 /maxOxygenBonus)*oxygenBonus);

                    if (oxygenBonus > 0) {
                        $$0.blit(StandIcons.JOJO_ICONS, $$9, $$16 - 3, 165, 171, 4+airWidth, 15);
                    }
                    $$0.blit(StandIcons.JOJO_ICONS, $$9, $$16 - 3, 165, 186, 89, 15);
                }
            }
        }
    }


    @Inject(method = "renderPlayerHealth", at = @At(value = "TAIL"))
    public void roundabout$renderHealth2(GuiGraphics $$0, CallbackInfo ci){
        rdbt$renderHealth2Common($$0);
    }

    @Unique
    private boolean roundabout$RenderBars(GuiGraphics context, int x){
        if (minecraft.player != null && minecraft.level != null) {

            StandUser user = ((StandUser) minecraft.player);

            boolean removeNum = false;
            if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW && !user.roundabout$getEffectiveCombatMode() &&
                    (PW.getShootTicks() > 0)){
                removeNum = true;
            }


            boolean isTSEntity = ((TimeStop) minecraft.level).isTimeStoppingEntity(minecraft.player);
            if (((TimeStop) minecraft.level).CanTimeStopEntity(minecraft.player)) {

                StandHudRender.renderTSHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha, false, this.getFont());
                return true;
            } if (user.roundabout$getPossesionTime() > 0) {
                StandHudRender.renderNumberHUD(context,minecraft,screenWidth,screenHeight,x, (double) user.roundabout$getPossesionTime() /20,5,StandIcons.JOJO_ICONS,0,161,16730092);
                return true;
            } else if (user.roundabout$isClashing()) {
                ((StandUserClientPlayer) minecraft.player).roundabout$setClashDisplayExtraTimestamp(this.minecraft.player.tickCount);
                float c = (user.roundabout$getStandPowers().getClashProgress());
                ((StandUserClientPlayer) minecraft.player).roundabout$setLastClashPower(c);
                StandHudRender.renderClashHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha, c);
                return true;
            } else if (((StandUserClientPlayer) minecraft.player).roundabout$getClashDisplayExtraTimestamp() >= minecraft.player.tickCount - 20) {
                StandHudRender.renderClashHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha, ((StandUserClientPlayer) minecraft.player).roundabout$getLastClashPower());
                return true;
            } else if (((StandUser) minecraft.player).roundabout$isGuarding()) {

                if (!(user.roundabout$getStandPowers() instanceof PowersRatt)) {
                    StandHudRender.renderGuardHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha);
                    return true;
                }
            } else if (isTSEntity || (((StandUser) minecraft.player).roundabout$getStandPowers().getMaxTSTime() > 0
                    && (((StandUser) minecraft.player).roundabout$getStandPowers().getActivePower() == PowerIndex.SPECIAL) ||  ((StandUser) minecraft.player).roundabout$getStandPowers().getActivePower() == PowerIndex.LEAD_IN)) {

                StandHudRender.renderTSHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha, true, this.getFont());
                return true;
            } else if (minecraft.player.getVehicle() != null && minecraft.player.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()){

                StandHudRender.renderGrabbedHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha);
                return true;
            } else if (((StandUser)minecraft.player).roundabout$isSealed()){

                StandHudRender.renderSealedDiscHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha);
                return true;
            } else if (((StandUser)minecraft.player).roundabout$getStandPowers().isPiloting()){
                if (this.getCameraPlayer() != null) {
                    StandHudRender.renderDistanceHUDJustice(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, ((StandUser) minecraft.player).roundabout$getStandPowers().getPilotingStand());
                }
                return true;
            }else if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW && user.roundabout$getEffectiveCombatMode()){
                StandHudRender.renderShootModeSoftAndWet(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, x, PW);
                return true;

            } else if (user.roundabout$getStandPowers().replaceHudActively()){
                user.roundabout$getStandPowers().getReplacementHUD(context,this.getCameraPlayer(),screenWidth,screenHeight,x);
                return true;
            } else if (((StandUser) minecraft.player).roundabout$getGuardPoints() < ((StandUser) minecraft.player).roundabout$getMaxGuardPoints()){
                StandHudRender.renderGuardHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha);
                return true;
            } else if (minecraft.player.getVehicle() != null && minecraft.player.getVehicle() instanceof RoadRollerEntity RRE && RRE.getPavingBoolean()) {
                StandHudRender.renderRoadRollerHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha,  RRE);
                return true;
            } else if (ClientUtil.getRoadRollerPickingRRE() != null && ClientUtil.getRoadRollerPickingRRE() instanceof RoadRollerEntity && ClientUtil.getRoadRollerPickingRRE().getPickupBoolean()==minecraft.player.getId()) {
                StandHudRender.renderRoadRollerPickupHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha,  ClientUtil.getRoadRollerPickingRRE());
                return true;
            } else if (((IPlayerEntity)minecraft.player).roundabout$getDisplayExp() && ((StandUser)minecraft.player).roundabout$hasAStand()){

                StandHudRender.renderExpHud(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, x, roundabout$flashAlpha, roundabout$otherFlashAlpha,removeNum);
                if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW && !user.roundabout$getEffectiveCombatMode() &&
                        (PW.getShootTicks() > 0)){
                    StandHudRender.renderShootModeLightSoftAndWet(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, x, PW);
                }
                return true;
            } else if (removeNum){
                StandHudRender.renderExperienceBar(minecraft,screenWidth, screenHeight,context);
                if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW && !user.roundabout$getEffectiveCombatMode() &&
                        (PW.getShootTicks() > 0)){
                    StandHudRender.renderShootModeLightSoftAndWet(context, minecraft, this.getCameraPlayer(), screenWidth, screenHeight, x, PW);
                }
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
        StandHudRender.renderAttackHud($$0, this.getCameraPlayer(), screenWidth, screenHeight, tickCount, this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()), roundabout$flashAlpha, roundabout$otherFlashAlpha);
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

    @Shadow private long healthBlinkTime;

    @Shadow private int lastHealth;

    @Shadow private long lastHealthTime;

    @Shadow private int displayHealth;

    @Shadow @Final private static ResourceLocation GUI_ICONS_LOCATION;

    @Override
    public void roundabout$setFlashAlpha(float flashAlpha) {
        this.roundabout$flashAlpha = flashAlpha;
    }

    @Override
    public void roundabout$setOtherFlashAlpha(float otherFlashAlpha) {
        this.roundabout$otherFlashAlpha = otherFlashAlpha;
    }
//    public static final String DEBUG_TEXT_1 = "hud.roundabout.standout";
}

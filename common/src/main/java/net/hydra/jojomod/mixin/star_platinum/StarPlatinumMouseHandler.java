package net.hydra.jojomod.mixin.star_platinum;

import com.mojang.blaze3d.Blaze3D;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.SmoothDouble;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class StarPlatinumMouseHandler {

    /**Star Platinum makes the mouse sensitivity go down so you move your view slower, this is because
     * it is hard to keep up with full speed, and so the community requested this feature.
     *
     * The class isn't super accessible so this mixin uses a lot of base code*/

    @Inject(method = "turnPlayer()V",
            at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$turnPlayer(CallbackInfo ci) {
        if (Minecraft.getInstance().player != null){
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                int scopelvl = ((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers().scopeLevel;
                if (scopelvl > 0) {
                    ci.cancel();

                    double $$0 = Blaze3D.getTime();
                    double $$1 = $$0 - this.lastMouseEventTime;
                    this.lastMouseEventTime = $$0;
                    if (this.isMouseGrabbed() && this.minecraft.isWindowActive()) {
                        double $$2 = this.minecraft.options.sensitivity().get() * 0.6F + 0.2F;
                        double $$3 = $$2 * $$2 * $$2;

                        if (scopelvl == 1) {
                            $$3 /= 2;
                        } else if (scopelvl == 2) {
                            $$3 /= 4;
                        } else if (scopelvl == 3) {
                            $$3 /= 8;
                        }

                        double $$4 = $$3 * 8.0;
                        double $$7;
                        double $$8;
                        if (this.minecraft.options.smoothCamera) {
                            double $$5 = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * $$4, $$1 * $$4);
                            double $$6 = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * $$4, $$1 * $$4);
                            $$7 = $$5;
                            $$8 = $$6;
                        } else if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player.isScoping()) {
                            this.smoothTurnX.reset();
                            this.smoothTurnY.reset();
                            $$7 = this.accumulatedDX * $$3;
                            $$8 = this.accumulatedDY * $$3;
                        } else {
                            this.smoothTurnX.reset();
                            this.smoothTurnY.reset();
                            $$7 = this.accumulatedDX * $$4;
                            $$8 = this.accumulatedDY * $$4;
                        }

                        this.accumulatedDX = 0.0;
                        this.accumulatedDY = 0.0;
                        int $$13 = 1;
                        if (this.minecraft.options.invertYMouse().get()) {
                            $$13 = -1;
                        }

                        this.minecraft.getTutorial().onMouse($$7, $$8);
                        if (this.minecraft.player != null) {
                            this.minecraft.player.turn($$7, $$8 * (double)$$13);
                        }
                    } else {
                        this.accumulatedDX = 0.0;
                        this.accumulatedDY = 0.0;
                    }


                }
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private boolean isLeftPressed;
    @Shadow
    private boolean isMiddlePressed;
    @Shadow
    private boolean isRightPressed;
    @Shadow
    private double xpos;
    @Shadow
    private double ypos;
    @Shadow
    private int fakeRightMouse;
    @Shadow
    private int activeButton = -1;
    @Shadow
    private boolean ignoreFirstMove = true;
    @Shadow
    private int clickDepth;
    @Shadow
    private double mousePressedTime;
    @Shadow
    @Final
    private SmoothDouble smoothTurnX = new SmoothDouble();
    @Shadow
    @Final
    private SmoothDouble smoothTurnY = new SmoothDouble();
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;
    @Shadow
    private double accumulatedScroll;
    @Shadow
    private double lastMouseEventTime = Double.MIN_VALUE;
    @Shadow
    private boolean mouseGrabbed;
    @Shadow
    public boolean isMouseGrabbed() {
        return this.mouseGrabbed;
    }
}

package net.hydra.jojomod.mixin.time_stop;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.BowlerHatItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class)
public class TimeStopGameRenderer {

    /**When time is stopped, you shouldn't experience bobbing from getting hurt*/

    @Unique
    public boolean roundabout$cleared = false;

    @Shadow
    private void bobHurt(PoseStack $$0, float $$1){}
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tiltViewWhenHurt2(PoseStack $$0, float $$1, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        if (!roundabout$cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() instanceof LivingEntity) {
                    Player $$2 = (Player) this.minecraft.getCameraEntity();
                    $$1 = ((IEntityAndData) $$2).roundabout$getPreTSTick();
                    //recursive logic, set to true is important even if the compiler doesnt know it
                    roundabout$cleared = true;
                    this.bobHurt($$0, $$1);
                    roundabout$cleared = false;
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutBobView(PoseStack $$0, float $$1, CallbackInfo ci) {
        LivingEntity player = minecraft.player;
        if (!roundabout$cleared) {
            if (player != null && ((TimeStop) player.level()).CanTimeStopEntity(player)) {
                if (this.minecraft.getCameraEntity() instanceof Player) {
                    Player $$2 = (Player) this.minecraft.getCameraEntity();
                    $$1 = ((IEntityAndData) $$2).roundabout$getPreTSTick();
                    roundabout$cleared = true;
                    this.bobView($$0, $$1);
                    roundabout$cleared = false;
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderItemInHand", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$RenderHandsWithItems(PoseStack $$0, Camera $$1, float $$2, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        if (!roundabout$cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() != null) {
                    Entity Ent = this.minecraft.getCameraEntity();
                    $$2 = ((IEntityAndData) Ent).roundabout$getPreTSTick();
                    roundabout$cleared = true;
                    this.renderItemInHand($$0, $$1, $$2);
                    roundabout$cleared = false;
                }
                ci.cancel();
                return;
            }
        }
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    public void renderItemInHand(PoseStack $$0, Camera $$1, float $$2) {}


    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    private void bobView(PoseStack $$0, float $$1){}
}

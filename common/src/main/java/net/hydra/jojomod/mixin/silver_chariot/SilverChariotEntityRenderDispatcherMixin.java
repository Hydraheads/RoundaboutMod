package net.hydra.jojomod.mixin.silver_chariot;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.ICamera;
import net.hydra.jojomod.client.SilverChariotRenderClient;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class SilverChariotEntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void render(
            T entity,
            double x,
            double y,
            double z,
            float yaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        ICamera cameraAccess = (ICamera) camera;

        Entity controlEntity = cameraAccess.roundabout$getPovSwitch();

        if (!(controlEntity instanceof SilverChariotEntity SCE)) {
            return;
        }

        if (!(SCE.getUser() instanceof Player player)) {
            return;
        }

        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        if (entity == controlEntity) {
            return;
        }

        if (!SilverChariotRenderClient.shouldBeVisibleToUser(player, entity, partialTick)) {
            ci.cancel();
        }
    }

}

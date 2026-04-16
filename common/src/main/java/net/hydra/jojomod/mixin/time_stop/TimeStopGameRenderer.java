package net.hydra.jojomod.mixin.time_stop;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
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


        Minecraft mc = Minecraft.getInstance();
       Player player = mc.player;
        Camera camera = $$1;
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            if (powers.isPiloting() && piloting != null && piloting.isAlive() && !piloting.isRemoved()) {
                Vec3 camPos = camera.getPosition();

                // Make a very small box around the camera
                AABB cameraBox = new AABB(
                        camPos.x - 1e-4, camPos.y - 1e-4, camPos.z - 1e-4,
                        camPos.x + 1e-4, camPos.y + 1e-4, camPos.z + 1e-4
                );

                Level level = mc.level;

                // Check if ANY block collision shape intersects this box
                boolean insideCollision = !level.noCollision(cameraBox);
                if (insideCollision){
                    powers.setPiloting(0);
                    IPlayerEntity ipe = ((IPlayerEntity) player);
                    ipe.roundabout$setIsControlling(0);
                    C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,0);
                    ClientUtil.setCameraEntity(null);
                }
            }
        }


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

    private void rdbt$renderTex(TextureAtlasSprite $$0, PoseStack $$1) {
        RenderSystem.setShaderTexture(0, $$0.atlasLocation());
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        BufferBuilder $$2 = Tesselator.getInstance().getBuilder();
        float $$3 = 0.1F;
        float $$4 = -1.0F;
        float $$5 = 1.0F;
        float $$6 = -1.0F;
        float $$7 = 1.0F;
        float $$8 = -0.5F;
        float $$9 = $$0.getU0();
        float $$10 = $$0.getU1();
        float $$11 = $$0.getV0();
        float $$12 = $$0.getV1();
        Matrix4f $$13 = $$1.last().pose();
        $$2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        $$2.vertex($$13, -1.0F, -1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$10, $$12).endVertex();
        $$2.vertex($$13, 1.0F, -1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$9, $$12).endVertex();
        $$2.vertex($$13, 1.0F, 1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$9, $$11).endVertex();
        $$2.vertex($$13, -1.0F, 1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$10, $$11).endVertex();
        BufferUploader.drawWithShader($$2.end());
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

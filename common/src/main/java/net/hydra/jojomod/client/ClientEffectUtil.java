package net.hydra.jojomod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.hydra.jojomod.event.TerrainFragments;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalDouble;

public class ClientEffectUtil {
    public static void spawnTerrainFragment(LocalPlayer player) {
        if (ConfigManager.getClientConfig() != null && ConfigManager.getClientConfig().generalSettings != null &&
                !ConfigManager.getClientConfig().generalSettings.timeEraseBlocks){
            return;
        }
        if (player != null && (((TimeStop) player.level()).inTimeStopRange(player))){
            return;
        }
        Level level = player.level();
        RandomSource random = level.random;

        int radius = 30;

        int x = Mth.floor(player.getX()) + random.nextInt(radius * 2 + 1) - radius;
        int z = Mth.floor(player.getZ()) + random.nextInt(radius * 2 + 1) - radius;

        int y = Mth.floor(player.getY()) + 20;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);

        while (pos.getY() > level.getMinBuildHeight()) {

            BlockState state = level.getBlockState(pos);

            if (level.getBlockState(pos.above()).isSolid()) {
                pos.move(Direction.DOWN);
                continue;
            }

            if (!state.isAir()
                    && state.getRenderShape() == RenderShape.MODEL
                    && state.getFluidState().isEmpty()
                    && state.isSolidRender(level, pos)) {

                TerrainFragments fragment = new TerrainFragments();

                fragment.state = state;

                fragment.pos = Vec3.atCenterOf(pos).add(
                        random.nextDouble() * 0.4 - 0.2,
                        0.05,
                        random.nextDouble() * 0.4 - 0.2
                );

                fragment.prevPos = fragment.pos;

                fragment.velocity = new Vec3(
                        random.nextGaussian() * 0.01,
                        0.04 + random.nextDouble() * 0.02,
                        random.nextGaussian() * 0.01
                );

                fragment.rotX = random.nextFloat() * 360F;
                fragment.rotY = random.nextFloat() * 360F;
                fragment.rotZ = random.nextFloat() * 360F;

                fragment.prevRotX = fragment.rotX;
                fragment.prevRotY = fragment.rotY;
                fragment.prevRotZ = fragment.rotZ;

                fragment.rotSpeedX = random.nextFloat() * 4F - 2F;
                fragment.rotSpeedY = random.nextFloat() * 4F - 2F;
                fragment.rotSpeedZ = random.nextFloat() * 4F - 2F;

                fragment.prevScale = 1;
                fragment.scale = 1;

                fragment.age = 0;
                fragment.maxAge = 50 + random.nextInt(30);

                terrainFragments.add(fragment);
                return;
            }

            pos.move(Direction.DOWN);
        }
    }

    public static void renderPurpleBox(
            PoseStack poseStack,
            MultiBufferSource buffer,
            Camera camera,
            BlockPos pos1,
            BlockPos pos2
    ) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());

        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        AABB box = new AABB(
                minX - 0.004,
                minY - 0.004,
                minZ - 0.004,
                maxX + 1.004,
                maxY + 1.004,
                maxZ + 1.004
        );

        Vec3 cameraPos = camera.getPosition();

        poseStack.pushPose();

        poseStack.translate(
                -cameraPos.x,
                -cameraPos.y,
                -cameraPos.z
        );

        // Make the lines thicker
        //RenderSystem.lineWidth(4.0F);

        VertexConsumer vertexConsumer =
                buffer.getBuffer(RenderType.lines());

        LevelRenderer.renderLineBox(
                poseStack,
                vertexConsumer,
                box,
                0.7F,  // R
                0.0F,  // G
                1.0F,  // B
                1.0F   // A
        );

        poseStack.popPose();

        // IMPORTANT: restore the default line width
        RenderSystem.lineWidth(1.0F);
    }

    public static final List<TerrainFragments> terrainFragments = new ArrayList<>();
    public static void updateTerrainFragments() {
        if (ConfigManager.getClientConfig() != null && ConfigManager.getClientConfig().generalSettings != null &&
                !ConfigManager.getClientConfig().generalSettings.timeEraseBlocks){
            return;
        }
        Player player = ClientUtil.getPlayer();
        if (player != null && (((TimeStop) player.level()).inTimeStopRange(player))){
            return;
        }

        Iterator<TerrainFragments> it = terrainFragments.iterator();

        while (it.hasNext()) {

            TerrainFragments frag = it.next();

            frag.prevPos = frag.pos;

            frag.prevRotX = frag.rotX;
            frag.prevRotY = frag.rotY;
            frag.prevRotZ = frag.rotZ;

            frag.velocity = frag.velocity.add(0, 0.005, 0);

            frag.pos = frag.pos.add(frag.velocity);

            frag.rotX += frag.rotSpeedX;
            frag.rotY += frag.rotSpeedY;
            frag.rotZ += frag.rotSpeedZ;
            frag.prevScale = frag.scale;

            if (ClientUtil.isUsingTimeErase && frag.age < frag.maxAge) {
                frag.scale = 1.0F;
            } else {
                frag.scale = Math.max(0.0F, frag.scale - 0.05F);
            }
            frag.age++;

            if (frag.scale <= 0) {
                it.remove();
            }
        }
    }
}

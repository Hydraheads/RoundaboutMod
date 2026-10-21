// code taken and inspired from: https://github.com/Violunae/arrowtrails
package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class TrailRenderer {
    // in order to use this, you'll need to keep track of the projectile's position and send it with the renderer. look
    // at RoundaboutBulletEntityRenderer and RoundaboutBulletEntity for examples
    public record Settings(
            // the end and start were swapped and i am NOT figuring out all the math to fix this.
            float length,
            float endWidth,
            float startWidth,
            Vector3f endColor,
            Vector3f startColor,
            float endAlpha,
            float startAlpha
    ) {
        // white trail, basically a template
        public static final Settings DEFAULT = new Settings(
                16.0f, 0.15f, 0.0f,
                new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f),
                1.0f, 0.0f
        );
        // yellow trail for bullets. to use the color selector, just put in your rgb values and divide by 255
        public static final Settings BULLETS = new Settings(
                16.0f, 0.03f, 0.12f,
                new Vector3f(1, 173/255f, 13/255f), new Vector3f(255/255f, 210/255f, 17 / 255f),
                0.4f, 0.7f
        );
    }

    public static void drawTrailRaw(
            Vec3 anchor,
            List<Vec3> points,
            PoseStack.Pose pose,
            int packedLight,
            float partialTicks,
            Settings settings,
            MultiBufferSource bufferSource
    ) {
        if (points.size() < 2) return;

        float total = settings.length() + 1f;

        Vec3[] left = new Vec3[points.size()];
        Vec3[] right = new Vec3[points.size()];

        for (int i = 0; i < points.size(); i++) {
            Vec3 p = points.get(i).subtract(anchor);

            float t = ((float) i + partialTicks) / total;
            float w = Mth.lerp(t, settings.startWidth(), settings.endWidth());

            Vec3 dir;
            if (i == points.size() - 1) {
                dir = p.subtract(points.get(i - 1).subtract(anchor)).normalize();
            } else {
                dir = points.get(i + 1).subtract(anchor).subtract(p).normalize();
            }

            if (Minecraft.getInstance().getCameraEntity() == null) return;
            Vec3 cameraPos = Minecraft.getInstance().getCameraEntity().getPosition(partialTicks);
            Vec3 viewDir = cameraPos.subtract(anchor).subtract(p).normalize();
            Vec3 side = dir.cross(viewDir).normalize().scale(w * 0.5f);

            left[i] = p.add(side);
            right[i] = p.subtract(side);
        }

        VertexConsumer consumer = bufferSource.getBuffer(ModRenderTypes.TRAIL_RENDER_TYPE);
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3 v1a = left[i];
            Vec3 v1b = right[i];
            Vec3 v2a = left[i + 1];
            Vec3 v2b = right[i + 1];

            float t1 = ((float) i + partialTicks) / total;
            float t2 = ((float) (i + 1) + partialTicks) / total;

            Vector3f c1 = lerp(t1, settings.startColor(), settings.endColor());
            Vector3f c2 = lerp(t2, settings.startColor(), settings.endColor());
            float a1 = Mth.lerp(t1, settings.startAlpha(), settings.endAlpha());
            float a2 = Mth.lerp(t2, settings.startAlpha(), settings.endAlpha());

            vertex(pose, consumer, packedLight, (float) v1a.x, (float) v1a.y, (float) v1a.z, c1, a1);
            vertex(pose, consumer, packedLight, (float) v2a.x, (float) v2a.y, (float) v2a.z, c2, a2);
            vertex(pose, consumer, packedLight, (float) v2b.x, (float) v2b.y, (float) v2b.z, c2, a2);
            vertex(pose, consumer, packedLight, (float) v1b.x, (float) v1b.y, (float) v1b.z, c1, a1);
        }
    }

    private static Vector3f lerp(float t, Vector3f a, Vector3f b) {
        return new Vector3f(
                Mth.lerp(t, a.x, b.x),
                Mth.lerp(t, a.y, b.y),
                Mth.lerp(t, a.z, b.z)
        );
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer, int packedLight, float x, float y, float z, Vector3f c, float a) {
        consumer.vertex(pose.pose(), x, y, z)
                .color(c.x, c.y, c.z, a)
                .uv(0.0f, 0.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(pose.normal(), 0.0f, 0.0f, 1.0f)
                .endVertex();
    }
}
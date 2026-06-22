package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.UVModel;
import net.hydra.jojomod.client.models.visages.parts.RipperEyesAnimation;
import net.hydra.jojomod.entity.projectile.ColdBlastProjectile;
import net.hydra.jojomod.entity.projectile.UltravioletProjectile;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ColdBlastRenderer extends EntityRenderer<ColdBlastProjectile> {

    public ColdBlastRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }


    public void render(ColdBlastProjectile $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {

            if (((TimeStop) $$0.level()).inTimeStopRange($$0)) {
                $$2 = 0;
            }

            $$3.pushPose();
            $$3.translate(0,-1F,0);
            $$3.scale(1.6f,1.6f,1.6f);
            ModStrayModels.WhiteAlbumCold.render2($$0, $$2, $$3, $$4, $$5, 1.0F, 1.0F, 1.0F, 1f);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);

    }

    @Override
    public ResourceLocation getTextureLocation(ColdBlastProjectile var1) {
        return new ResourceLocation(Roundabout.MOD_ID, "textures/stand/white_album/projectiles/shooting_mode.png");
    }

}

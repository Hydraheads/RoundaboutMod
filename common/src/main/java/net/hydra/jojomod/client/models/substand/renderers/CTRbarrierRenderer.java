package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.GentlyWeepsModel;
import net.hydra.jojomod.client.models.visages.parts.RipperEyesAnimation;
import net.hydra.jojomod.entity.substand.CTRbarrierEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersCatchTheRainbow;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class CTRbarrierRenderer extends EntityRenderer<CTRbarrierEntity> {

    //idk how to stop the game from crashing so here's a renderer that uses the gently weeps model with an invisible texture 🥀

    private final GentlyWeepsModel model;

    public CTRbarrierRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GentlyWeepsModel<>(context.bakeLayer(ModEntityRendererClient.GENTLY_WEEPS_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(CTRbarrierEntity ctrbarrierEntity) {
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/stand/achtung_baby/very_badass_texture.png");
    }
}

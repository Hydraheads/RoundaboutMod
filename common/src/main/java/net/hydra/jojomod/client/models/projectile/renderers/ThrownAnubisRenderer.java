package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.entity.projectile.ThrownAnubisEntity;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class ThrownAnubisRenderer extends ThrownObjectRenderer<ThrownAnubisEntity> {

    public ThrownAnubisRenderer(EntityRendererProvider.Context $$0) {super($$0);}

    @Override
    public void render(@NotNull ThrownAnubisEntity context, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int i) {

        ModStrayModels.ANUBIS.render(context, f, poseStack, bufferSource, i,
                1, 1, 1, 1.0F, PowersAnubis.ANIME);

        Roundabout.LOGGER.info("WHYYY");
    }

}

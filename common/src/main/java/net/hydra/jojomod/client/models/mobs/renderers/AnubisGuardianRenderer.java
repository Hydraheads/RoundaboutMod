/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.AnubisGuardianModel;
import net.hydra.jojomod.client.models.mobs.layers.AnubisGuardianLayer;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class AnubisGuardianRenderer<T extends AnubisGuardian>
        extends MobRenderer<T, AnubisGuardianModel<T>> {


    public AnubisGuardianRenderer(EntityRendererProvider.Context context) {
        super(context,new AnubisGuardianModel<>(context.bakeLayer(ModEntityRendererClient.ANUBIS_GUARDIAN_LAYER)),0.5F);
        this.addLayer(new AnubisGuardianLayer<>(this,context.getItemInHandRenderer()));
    }

    @Override
    protected void scale(T abstractIllager, PoseStack poseStack, float f) {
        float g = 0.9375f;
        poseStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    public static final ResourceLocation normal = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/wandering_guardian.png");
    public static final ResourceLocation revealed = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/wandering_guardian_revealed.png");


    @Override
    public ResourceLocation getTextureLocation(T t) {
        if (t.getSummoner(t.level()) != null) {
            return revealed;
        }
        return normal;
    }
}


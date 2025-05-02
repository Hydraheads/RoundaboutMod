package net.hydra.jojomod.entity.npcs;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.mobs.DIOModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class ZombieAestheticianRenderer<T extends ZombieAesthetician> extends MobRenderer<T, NonJojoNpcModel<T>> {
    private static final ResourceLocation AES1 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/zombie_aesthetician.png");

    public ZombieAestheticianRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieAestheticianModel<>(context.bakeLayer(ModEntityRendererClient.ZOMBIE_AESTHETICIAN_LAYER)),0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return AES1;
    }

    protected boolean isShaking(T p_113773_) {
        return super.isShaking(p_113773_) || p_113773_.isUnderWaterConverting();
    }
}

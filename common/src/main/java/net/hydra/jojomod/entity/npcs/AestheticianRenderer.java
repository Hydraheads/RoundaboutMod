package net.hydra.jojomod.entity.npcs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.hydra.jojomod.entity.visages.mobs.AyaModel;
import net.hydra.jojomod.entity.visages.mobs.AyaNPC;
import net.hydra.jojomod.entity.visages.mobs.AyaRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AestheticianRenderer<T extends Aesthetician> extends AyaRenderer<Aesthetician> {

    private static final ResourceLocation AES1 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/aesthetician.png");
    private static final ResourceLocation AES2 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/aesthetician_second.png");
    private static final ResourceLocation AES3 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/aesthetician_third.png");
    private static final ResourceLocation AES4 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/aesthetician_fourth.png");
    private static final ResourceLocation AES5 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/aesthetician_fifth.png");
    public AestheticianRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        int skinNumber = entity.getSkinNumber();
        if (skinNumber == 2){
            return AES2;
        } if (skinNumber == 3){
            return AES3;
        } if (skinNumber == 4){
            return AES4;
        } if (skinNumber == 5){
            return AES5;
        }
        return AES1;
    }
}

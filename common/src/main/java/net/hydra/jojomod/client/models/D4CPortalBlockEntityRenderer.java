package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.block.D4CPortalBlockEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class D4CPortalBlockEntityRenderer implements BlockEntityRenderer<D4CPortalBlockEntity> {

    public D4CPortalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(D4CPortalBlockEntity blockEntity, float v, PoseStack matrices, MultiBufferSource bufferSource, int light, int overlay) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null)
            return;

        if (!ClientUtil.canSeeStands(client.player))
            return;

    }

}

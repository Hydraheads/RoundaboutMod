package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.item.RoadRollerItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class RoadRollerItemRendererMixin {
}

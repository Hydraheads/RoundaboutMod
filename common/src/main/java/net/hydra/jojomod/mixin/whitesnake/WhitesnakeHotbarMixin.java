package net.hydra.jojomod.mixin.whitesnake;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class WhitesnakeHotbarMixin {
    @Unique private static final ResourceLocation WHITESNAKE_WIDGETS =
            new ResourceLocation("textures/gui/widgets.png");
    @Shadow private int screenWidth;
    @Shadow private int screenHeight;

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$renderHotbar(float partialTick, GuiGraphics graphics, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;
        WhitesnakeEntity stand = WhitesnakeControlInventory.controlledStand(player);
        if (stand == null) return;
        if (stand.isMeltingModeActive()) {
            ci.cancel();
            return;
        }
        int x = screenWidth / 2;
        int y = screenHeight - 22;
        RenderSystem.enableBlend();
        graphics.blit(WHITESNAKE_WIDGETS, x - 91, y, 0, 0, 182, 22);
        graphics.blit(WHITESNAKE_WIDGETS,
                x - 92 + player.getInventory().selected * 20, y - 1, 0, 22, 24, 22);
        for (int slot = 0; slot < WhitesnakeControlInventory.SIZE; slot++) {
            ItemStack stack = WhitesnakeControlInventory.get(player).get(slot);
            if (stack.isEmpty()) continue;
            int itemX = x - 90 + slot * 20 + 2;
            int itemY = y + 3;
            graphics.renderItem(player, stack, itemX, itemY, slot);
            graphics.renderItemDecorations(minecraft.font, stack, itemX, itemY);
        }
        RenderSystem.disableBlend();
        ci.cancel();
    }
}

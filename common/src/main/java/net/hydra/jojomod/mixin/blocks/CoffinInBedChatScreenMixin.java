package net.hydra.jojomod.mixin.blocks;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InBedChatScreen.class)
public abstract class CoffinInBedChatScreenMixin extends ChatScreen {

    @Unique
    private Button rdbt$leaveCoffinButton = null;

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"),cancellable = true)
    private void cfrender(GuiGraphics $$0, int $$1, int $$2, float $$3,CallbackInfo ci) {

        if (Minecraft.getInstance().player != null){
            if (Minecraft.getInstance().player.getSleepingPos().isPresent()){
                if (Minecraft.getInstance().player.level().getBlockState(Minecraft.getInstance().player.getSleepingPos().get()).is(ModBlocks.COFFIN_BLOCK)){

                    if (rdbt$leaveCoffinButton == null){
                        rdbt$leaveCoffinButton = Button.builder(Component.translatable("roundabout.coffin_sleep"), $$0x -> this.sendWakeUp())
                                .bounds(this.width / 2 - 100, this.height - 40, 200, 20)
                                .build();
                        this.clearWidgets();
                        this.addRenderableWidget(rdbt$leaveCoffinButton);
                    }

                    if (!this.minecraft.getChatStatus().isChatAllowed(this.minecraft.isLocalServer())) {
                        rdbt$leaveCoffinButton.render($$0, $$1, $$2, $$3);
                    } else {
                        super.render($$0, $$1, $$2, $$3);
                    }
                    ci.cancel();
                }
            }
        }
    }
    public CoffinInBedChatScreenMixin(String $$0) {
        super($$0);
    }

    @Shadow protected abstract void sendWakeUp();
}

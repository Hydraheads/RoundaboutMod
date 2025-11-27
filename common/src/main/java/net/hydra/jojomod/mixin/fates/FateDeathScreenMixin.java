package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
public abstract class FateDeathScreenMixin extends Screen {
    //Respawn as human button integration


    @Inject(method = "init()V", at = @At(value = "HEAD"), cancellable = true)
    public void rdbt$init(CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && !FateTypes.isHuman(Minecraft.getInstance().player) && !hardcore){
            this.delayTicker = 0;
            this.exitButtons.clear();
            Component $$0 = this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");

            this.exitButtons.add(this.addRenderableWidget(Button.builder($$0, $$0x -> {
                rdbt$respawnPacket();
                $$0x.active = false;
            }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));


                Component humanRespawn = Component.translatable("text.roundabout.respawn_as_human");
                this.exitButtons.add(this.addRenderableWidget(Button.builder(humanRespawn, $$0x -> {
                    rdbt$respawnHumanPacket();
                    $$0x.active = false;
                }).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build()));

                this.exitToTitleButton = this.addRenderableWidget(
                        Button.builder(
                                        Component.translatable("deathScreen.titleScreen"),
                                        $$0x -> this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true)
                                )
                                .bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20)
                                .build()
                );
            this.exitButtons.add(this.exitToTitleButton);
            this.setButtonsActive(false);
            this.deathScore = Component.translatable("deathScreen.score")
                    .append(": ")
                    .append(Component.literal(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW));
            ci.cancel();
        }
    }


    @Unique
    protected void rdbt$respawnPacket(){
        C2SPacketUtil.byteToServerPacket(PacketDataIndex.BYTE_RESPAWN_STRATEGY,(byte)0);
    }
    @Unique
    protected void rdbt$respawnHumanPacket(){
        C2SPacketUtil.byteToServerPacket(PacketDataIndex.BYTE_RESPAWN_STRATEGY,(byte)1);
    }

    @Shadow private int delayTicker;

    @Shadow @Final private List<Button> exitButtons;

    @Shadow @Final private boolean hardcore;

    @Shadow @Nullable private Button exitToTitleButton;

    @Shadow protected abstract void setButtonsActive(boolean bl);

    @Shadow private Component deathScore;

    @Shadow protected abstract void handleExitToTitleScreen();

    @Shadow protected abstract void exitToTitleScreen();

    protected FateDeathScreenMixin(Component $$0) {
        super($$0);
    }
}

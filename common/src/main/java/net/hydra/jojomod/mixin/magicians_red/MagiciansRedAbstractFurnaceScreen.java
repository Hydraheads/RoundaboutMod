package net.hydra.jojomod.mixin.magicians_red;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceScreen.class)
public abstract class MagiciansRedAbstractFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener {

    /**Magician's Red Furnace Assist, this mixin essentially makes the furnace button appear, and allows it to
     * be clicked on to send the message to the server to activate the furnace*/

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "TAIL"))
    private void roundabout$render(GuiGraphics context, int mouseX, int mouseY, float delta,CallbackInfo ci) {

        Player pl = Minecraft.getInstance().player;
        int i = this.leftPos;
        int j = this.topPos;
        if (pl != null) {
            StandUser user = ((StandUser) pl);
            StandPowers powers = user.roundabout$getStandPowers();

            if (powers.canLightFurnace() && !user.roundabout$isSealed()){
                context.blit(StandIcons.FURNACE, i-150, j, 0, 0, 0, 0);
                int leftGearPos = i +80;
                int topGearPos = j+55;
                if (roundabout$isSurelyHovering(leftGearPos, topGearPos, 19, 18, mouseX, mouseY)) {
                    context.blit(StandIcons.FURNACE, leftGearPos, topGearPos, 20, 0, 19, 18);
                } else {
                    context.blit(StandIcons.FURNACE, leftGearPos, topGearPos, 0, 0, 19, 18);
                }
            }
        }
    }
    @Inject(method = "mouseClicked(DDI)Z", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$mouseClicked(double mouseX, double mouseY, int $$2, CallbackInfoReturnable<Boolean> cir) {
        Player pl = Minecraft.getInstance().player;
        int i = this.leftPos;
        int j = this.topPos;
        if (pl != null) {
            StandUser user = ((StandUser) pl);
            StandPowers powers = user.roundabout$getStandPowers();

            if (powers.canLightFurnace()){
                int it = ClientNetworking.getAppropriateConfig().magiciansRedSettings.magiciansRedFurnaceTicks;
                if (it > 0) {
                    int leftGearPos = i + 80;
                    int topGearPos = j + 55;
                    if (roundabout$isSurelyHovering(leftGearPos, topGearPos, 19, 18, mouseX, mouseY)) {
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SMELT);
                        SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
                        soundmanager.play(SimpleSoundInstance.forUI(SoundEvents.FIRECHARGE_USE, 1.0F));
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
    @Unique
    protected boolean roundabout$isSurelyHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return p_97772_ >= (double)(p_97768_) && p_97772_ < (double)(p_97768_ + p_97770_) && p_97773_ >= (double)(p_97769_) && p_97773_ < (double)(p_97769_ + p_97771_);
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    public MagiciansRedAbstractFurnaceScreen(T $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
    }

}

package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    /** Code for vanilla item rendering in first person.*/

    @Shadow
    private float equipProgressMainHand;
    @Shadow
    private float equipProgressOffHand;
    @Shadow
    private final MinecraftClient client;
    @Shadow
    private ItemStack mainHand = ItemStack.EMPTY;
    @Shadow
    private ItemStack offHand = ItemStack.EMPTY;

    public HeldItemRendererMixin(MinecraftClient client) {
        this.client = client;
    }

    /** This makes certain items lower when your stand is out. Indicates that you can't really use tools
     * like swords or pickaxes while a stand is out.
     * The reason for that design decision is mostly to prevent sword swings overriding stand attacks
     * to mobs in a close range, and accidentally breaking blocks when attacking.*/
    @Inject(method = "updateHeldItems", at = @At(value = "TAIL"))
    public void injectHeldItems(CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity2 = this.client.player;
        if (!this.client.player.isRiding()) {
        if (((StandUser) this.client.player).getActive()) {
            ItemStack itemStack3 = clientPlayerEntity2.getMainHandStack();
            ItemStack itemStack4 = clientPlayerEntity2.getOffHandStack();
            if (itemStack3.getItem() instanceof ToolItem){
            if (this.equipProgressMainHand > 0.6) {
                this.equipProgressMainHand = MathHelper.clamp(this.equipProgressMainHand - 0.4f, 0.6f, 1.0f);
            }}

            if (this.equipProgressOffHand > 0.6) {
                this.equipProgressOffHand = MathHelper.clamp(this.equipProgressOffHand - 0.4f, 0.6f, 1.0f);
            }

        }}
    }
}

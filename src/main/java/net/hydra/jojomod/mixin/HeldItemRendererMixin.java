package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    //Use this to make the item you hold lower down when a stand is active
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

    //prevEquipProgressMainHand
    //equipProgressMainHand
    @Inject(method = "updateHeldItems", at = @At(value = "TAIL"))
    public void injectHeldItems(CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity2 = this.client.player;
        if (!this.client.player.isRiding()) {
        if (MyComponents.STAND_USER.get(this.client.player).getActive()) {
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
    //renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm)
//    @Inject(method = "renderFirstPersonItem", at = @At(value = ".-Head"))
//    public void updateHeldItems(CallbackInfo ci) {
//        ClientPlayerEntity clientPlayerEntity = client.player;
//        NbtCompound pd = ((IEntityDataSaver) client.player).getPersistentData();
//        if (pd.getBoolean("stand_on")) {
//
//        }
//        RoundaboutMod.LOGGER.info("CLIENT tickStandIn");
//    }
}

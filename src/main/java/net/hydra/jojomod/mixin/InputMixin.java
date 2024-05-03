package net.hydra.jojomod.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class InputMixin {

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    public int attackCooldown;
    @Shadow
    @Nullable
    public Screen currentScreen;

    /** This class is in part for detecting and canceling mouse inputs during stand attacks.
     * Please note this should
     * only apply to non-enhancer stands while their physical body is out.*/
        @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutAttack(CallbackInfoReturnable<Boolean> ci) {
            //handleInputEvents
            if (player != null) {
                StandUserComponent standComp = MyComponents.STAND_USER.get(player);
                if (standComp.getActive()){
                    ci.setReturnValue(true);
                }
                //while (this.options.attackKey.wasPressed()) {
                //}
            }
        }
        @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
        public void roundaboutBlockBreak(boolean breaking, CallbackInfo ci) {
            if (player != null) {
                StandUserComponent standComp = MyComponents.STAND_USER.get(player);
                if (standComp.getActive()) {
                    if (!breaking){
                        this.attackCooldown = 0;
                    }
                    this.interactionManager.cancelBlockBreaking();
                    ci.cancel();
                }
            }
        }
        @Inject(method = "doItemUse", at = @At("TAIL"), cancellable = true)
        public void roundaboutDoItemUse(CallbackInfo ci) {
            if (player != null) {
                StandUserComponent standComp = MyComponents.STAND_USER.get(player);
                if (standComp.getActive()) {
                    if (!standComp.isGuarding() && !standComp.isBarraging()) {
                        ClientPlayNetworking.send(ModMessages.STAND_GUARD_PACKET, PacketByteBufs.create());
                        standComp.tryPower(PowerIndex.GUARD,true);
                    }
                }
            }
        }
    @Inject(method = "doItemUse", at = @At("Head"), cancellable = true)
    public void roundaboutDoItemUseCancel(CallbackInfo ci) {
        if (player != null) {
            StandUserComponent standComp = MyComponents.STAND_USER.get(player);
            if (standComp.getActive()) {
                if (standComp.isGuarding()) {
                    ci.cancel();
                }
            }
        }
    }

        @Inject(method = "handleInputEvents", at = @At("HEAD"), cancellable = true)
        public void roundaboutInput(CallbackInfo ci){
            if (player != null) {
                StandUserComponent standComp = MyComponents.STAND_USER.get(player);
                if (!this.options.useKey.isPressed()) {
                    if (standComp.isGuarding() || standComp.isBarraging()) {
                            /*This code makes it so there is a slight delay between blocking and subsequent punch chain attacks.
                            * This delay exists so you can't right click left click chain for instant full power punches.*/
                       if (standComp.getActivePowerPhase() > 0 ) {
                           standComp.setInterruptCD(3);
                       }

                       if (standComp.isBarraging()){
                           MinecraftClient.getInstance().getSoundManager().stopSounds(standComp.getStandPowers().getBarrageCryID(), SoundCategory.PLAYERS);
                       }
                       ClientPlayNetworking.send(ModMessages.STAND_GUARD_CANCEL_PACKET, PacketByteBufs.create());
                       standComp.tryPower(PowerIndex.NONE,true);
                    }
                }
                if (standComp.getActive()) {
                    if (this.options.attackKey.isPressed() && !player.isUsingItem()) {

                        if (standComp.getInterruptCD()) {
                            if (standComp.canAttack()) {
                                ClientPlayNetworking.send(ModMessages.STAND_ATTACK_PACKET, PacketByteBufs.create());
                                standComp.tryPower(PowerIndex.ATTACK, true);
                            }
                        }

                        if (standComp.isGuarding() && !standComp.isBarraging()){
                            ClientPlayNetworking.send(ModMessages.STAND_BARRAGE_PACKET, PacketByteBufs.create());
                            standComp.tryPower(PowerIndex.BARRAGE, true);
                        }
                    }
                }
                    //this.handleStandRush(this.currentScreen == null && this.options.attackKey.isPressed());
            }
        }

        public void handleStandRush(boolean standRush){
            if (!standRush) {
                //Make stand attack cooldown for repeated attacks here?
                //this.attackCooldown = 0;
            }
            if (standRush){
                return;
            }

            //Make the packet below ONLY run when the cooldown runs dry

            //ClientPlayNetworking.send(ModMessages.STAND_ATTACK_CANCEL_PACKET, PacketByteBufs.create());
        }

    //
}

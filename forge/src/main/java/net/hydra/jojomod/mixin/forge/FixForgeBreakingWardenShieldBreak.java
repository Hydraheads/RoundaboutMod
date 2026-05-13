package net.hydra.jojomod.mixin.forge;


import net.hydra.jojomod.client.ClientNetworking;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This is embarrassing, forge
@Mixin(Player.class)
public abstract class FixForgeBreakingWardenShieldBreak extends LivingEntity  {

    @Shadow
    public abstract void disableShield(boolean p_36385_);

    protected FixForgeBreakingWardenShieldBreak(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "blockUsingShield(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    private void rdbt$blockUsingShield(LivingEntity p_36295_, CallbackInfo ci) {
        if (ClientNetworking.getAppropriateConfig().vanillaMinecraftTweaks.fixModLoaderCreatedBugs) {
            super.blockUsingShield(p_36295_);
            if (p_36295_.getMainHandItem().canDisableShield(this.useItem, this, p_36295_)
                    || p_36295_.canDisableShield()) {
                this.disableShield(true);
            }
            ci.cancel();
        }
    }
}

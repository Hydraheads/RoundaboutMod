package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ScreenEffectRenderer.class)
public abstract class ZScreenEffectRenderer {
    /**Use this to hide the block effect covering your screen when your player is suffocating during pilot*/
    @Shadow
    @Nullable
    private static BlockState getViewBlockingState(Player $$0) {
        return null;
    }

    @Shadow
    private static void renderTex(TextureAtlasSprite $$0, PoseStack $$1) {
    }

    @Shadow
    private static void renderWater(Minecraft $$0, PoseStack $$1) {
    }

    @Shadow
    private static void renderFire(Minecraft $$0, PoseStack $$1) {
    }

    @Unique
    private static BlockState roundabout$getViewBlockingState(LivingEntity $$0) {
        BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();

        for (int $$2 = 0; $$2 < 8; $$2++) {
            double $$3 = $$0.getX() + (double)(((float)(($$2 >> 0) % 2) - 0.5F) * $$0.getBbWidth() * 0.8F);
            double $$4 = $$0.getEyeY() + (double)(((float)(($$2 >> 1) % 2) - 0.5F) * 0.1F);
            double $$5 = $$0.getZ() + (double)(((float)(($$2 >> 2) % 2) - 0.5F) * $$0.getBbWidth() * 0.8F);
            $$1.set($$3, $$4, $$5);
            BlockState $$6 = $$0.level().getBlockState($$1);
            if ($$6.getRenderShape() != RenderShape.INVISIBLE && $$6.isViewBlocking($$0.level(), $$1)) {
                return $$6;
            }
        }

        return null;
    }
    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "HEAD"),cancellable = true)
    private static void roundabout$renderScreenEffect(Minecraft $$0, PoseStack $$1, CallbackInfo ci) {

        Player $$2 = $$0.player;
        if ($$2 != null) {
            StandUser standComp = ((StandUser) $$2);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            if (powers.isPiloting() && piloting != null && piloting.isAlive() && !piloting.isRemoved() ) {

                if (!$$2.noPhysics && !(powers instanceof PowersJustice)) {
                    BlockState $$3 = roundabout$getViewBlockingState(piloting);
                    if ($$3 != null) {
                        renderTex($$0.getBlockRenderer().getBlockModelShaper().getParticleIcon($$3), $$1);
                    }
                }

                if (!$$0.player.isSpectator()) {
                    if ($$0.player.isEyeInFluid(FluidTags.WATER)) {
                        renderWater($$0, $$1);
                    }

                    if ($$0.player.isOnFire()) {
                        renderFire($$0, $$1);
                    }
                }
                ci.cancel();
            }
        }

    }
}

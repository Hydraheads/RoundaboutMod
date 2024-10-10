package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public abstract class ForgeForgeGui extends Gui {

    public ForgeForgeGui(Minecraft p_232355_, ItemRenderer p_232356_) {
        super(p_232355_, p_232356_);
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void roundabout$render(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        if (minecraft.player != null && minecraft.level != null){
            int oxygenBonus = ((StandUser)minecraft.player).roundabout$getStandPowers().getAirAmount();
            int maxOxygenBonus = ((StandUser)minecraft.player).roundabout$getStandPowers().getMaxAirAmount();
            if (oxygenBonus > -1 && ((StandUser)minecraft.player).roundabout$getActive()) {
                int $$28 = minecraft.player.getMaxAirSupply();
                int $$29 = Math.min(minecraft.player.getAirSupply(), $$28);
                if (minecraft.player.isEyeInFluid(FluidTags.WATER)  || oxygenBonus < maxOxygenBonus) {
                    LivingEntity $$21 = roundabout$getPlayerVehicleWithHealth();
                    int $$22 = roundabout$getVehicleMaxHearts($$21);
                    int $$30 = roundabout$etVisibleVehicleHeartRows($$22) - 1;
                    int $$9 = this.screenWidth / 2 + 6;
                    int $$10 = this.screenHeight - 39;
                    int $$16 = $$10 - 10;
                    $$16 -= $$30 * 10;

                    if ($$22 == 0) {
                        $$16 -= 10;
                    }

                    int airWidth = (int) Math.floor(((double) 81 /maxOxygenBonus)*oxygenBonus);

                    if (oxygenBonus > 0) {
                        guiGraphics.blit(StandIcons.JOJO_ICONS, $$9, $$16 - 3, 165, 171, 4+airWidth, 15);
                    }
                    guiGraphics.blit(StandIcons.JOJO_ICONS, $$9, $$16 - 3, 165, 186, 89, 15);
                }
            }
        }
    }

    @Unique
    private LivingEntity roundabout$getPlayerVehicleWithHealth() {
        Player player = roundabout$getCameraPlayer();
        if (player != null) {
            Entity entity = player.getVehicle();
            if (entity == null) {
                return null;
            }

            if (entity instanceof LivingEntity) {
                return (LivingEntity)entity;
            }
        }

        return null;
    }


    @Unique
    private int roundabout$getVehicleMaxHearts(LivingEntity p_93023_) {
        if (p_93023_ != null && p_93023_.showVehicleHealth()) {
            float f = p_93023_.getMaxHealth();
            int i = (int)(f + 0.5F) / 2;
            if (i > 30) {
                i = 30;
            }

            return i;
        } else {
            return 0;
        }
    }

    @Unique
    private int roundabout$etVisibleVehicleHeartRows(int p_93013_) {
        return (int)Math.ceil((double)p_93013_ / 10.0D);
    }


    @Unique
    private Player roundabout$getCameraPlayer() {
        return !(this.minecraft.getCameraEntity() instanceof Player) ? null : (Player)this.minecraft.getCameraEntity();
    }
}

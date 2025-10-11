package net.hydra.jojomod.mixin.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public abstract class ForgeForgeGui extends Gui {

    @Shadow public int rightHeight;

    public ForgeForgeGui(Minecraft p_232355_, ItemRenderer p_232356_) {
        super(p_232355_, p_232356_);
    }

    @Inject(method = "renderFood(IILnet/minecraft/client/gui/GuiGraphics;)V", at = @At(value = "HEAD"), remap = false, cancellable = true)
    public void roundabout$renderFood(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        Player player = (Player) this.minecraft.getCameraEntity();
        if (player != null){
            if (FateTypes.isVampire(player)){
                ci.cancel();
                minecraft.getProfiler().push("food");
                RenderSystem.enableBlend();

                FoodData stats = minecraft.player.getFoodData();
                int level = stats.getFoodLevel();

                int left = width / 2 + 91;
                int top = height - rightHeight;
                rightHeight += 10;


                ClientUtil.renderHungerStuff(guiGraphics,player,left,top,
                        this.random.nextInt(3),level,this.tickCount);

                RenderSystem.disableBlend();
                minecraft.getProfiler().pop();
            }
        }
    }

    @Inject(method = "renderAir(IILnet/minecraft/client/gui/GuiGraphics;)V", at = @At(value = "INVOKE",
            target="Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V",shift = At.Shift.BEFORE), remap = false)
    public void roundabout$renderAir(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (minecraft.player != null && minecraft.level != null){
            int oxygenBonus = ((StandUser)minecraft.player).roundabout$getStandPowers().getAirAmount();
            int maxOxygenBonus = ((StandUser)minecraft.player).roundabout$getStandPowers().getMaxAirAmount();
            if (oxygenBonus > -1 && ((StandUser)minecraft.player).roundabout$getActive()) {
                int $$28 = minecraft.player.getMaxAirSupply();
                int $$29 = Math.min(minecraft.player.getAirSupply(), $$28);
                boolean $$3 = !minecraft.player.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(minecraft.player) &&
                        (!minecraft.player.getAbilities().invulnerable);
                if ((minecraft.player.isEyeInFluid(FluidTags.WATER) && $$3)  || oxygenBonus < maxOxygenBonus) {
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

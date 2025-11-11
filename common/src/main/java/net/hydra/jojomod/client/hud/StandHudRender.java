package net.hydra.jojomod.client.hud;


import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class StandHudRender {
    /** Renders the HUD for stand attacks/abilities.
     * Keep in mind it has to slide in so some of the code may look awkward.*/


    private static final int guiSize = 174;
    private static float presentX = 0;

    public static boolean configIsLoaded(){
        return ConfigManager.getClientConfig() != null;
    }

    public static void renderStandHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                      float flashAlpha, float otherFlashAlpha) {
        if (!configIsLoaded())
            return;
        if (ConfigManager.getClientConfig() == null || ConfigManager.getClientConfig().dynamicSettings == null)
                return;
        if (playerEntity != null) {
            RenderSystem.enableBlend();
            int x = 0;
            int y = 0;

            int width = scaledWidth;
            int height = scaledHeight;
            int textureWidth = guiSize;
            int textureHeight = 30;


            int iconHeight = 18;
            int iconWidth = 18;
            x = (int) (-20-guiSize+ presentX);
            //x = (int) (-20);
            y = ConfigManager.getClientConfig().abilityIconHudY;
            Minecraft mc = Minecraft.getInstance();
            float tickDelta = mc.getDeltaFrameTime();

            boolean standOn = ((StandUser) playerEntity).roundabout$getActive();
            boolean renderIcons = (standOn || !FateTypes.isHuman(playerEntity)) && !ConfigManager.getClientConfig().dynamicSettings.hideGUI
                    && !(ConfigManager.getClientConfig().enablePickyIconRendering && !((StandUser) playerEntity).roundabout$getStandPowers().hasCooldowns());
            if (renderIcons || presentX > 0.1){
                if (!renderIcons){
                    if (ConfigManager.getClientConfig().abilityIconHudIsAnimated){
                        presentX = Math.max(controlledLerp(tickDelta, presentX,0,0.5f),0);
                    } else {
                        presentX = 0;
                    }
                } else {
                    if (ConfigManager.getClientConfig().abilityIconHudIsAnimated) {
                        if (presentX < ConfigManager.getClientConfig().abilityIconHudX) {
                            presentX++;
                            presentX = Math.min(controlledLerp(tickDelta, presentX, ConfigManager.getClientConfig().abilityIconHudX, 0.5f), ConfigManager.getClientConfig().abilityIconHudX);
                        }
                    } else {
                        presentX = ConfigManager.getClientConfig().abilityIconHudX;
                    }
                }
                context.setColor(1.0f, 1.0f, 1.0f, 0.9f);
                //Draws the empty bar
                //context.drawTexture(ARROW_ICON,x,y-2,0, 0, textureWidth, textureHeight, textureWidth, textureHeight);


                if (standOn){
                    ((StandUser) playerEntity).roundabout$getStandPowers().renderIcons(context, x, y);
                } else {
                    ((IFatePlayer) playerEntity).rdbt$getFatePowers().renderIcons(context, x, y);
                }


                context.setColor(1.0f, 1.0f, 1.0f, 1f);
            }
        }
    }


    public static Component fixKey(Component textIn){

        String X = textIn.getString();
        if (X.length() > 1){
        String[] split = X.split("\\s");
        if (split.length > 1){
            return Component.nullToEmpty(""+split[0].charAt(0)+split[1].charAt(0));
        } else {
            if (split[0].length() > 1){
                return Component.nullToEmpty(""+split[0].charAt(0)+split[0].charAt(1));
            } else {
                return Component.nullToEmpty(""+split[0].charAt(0));
            }
        }
        } else {
            return textIn;
        }
    }
    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        return start + (delta * (end - start))*multiplier;
    }

    /**Attack Meter code for combat stands here*/
    public static void renderAttackHud(GuiGraphics context,  Player playerEntity,
                                       int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                       float flashAlpha, float otherFlashAlpha){
        if (playerEntity != null) {
            ((StandUser) playerEntity).roundabout$getStandPowers().renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
            ((IFatePlayer) playerEntity).rdbt$getFatePowers().renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    public static void renderRoadRollerHud(GuiGraphics context, Minecraft client, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int x, float flashAlpha, float otherFlashAlpha, RoadRollerEntity RRE) {
        ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(RRE.getConcreteColour().getItem());
        if (itemID == null) return;

        String fixedString = itemID.getPath().replace("_powder", "");
        ResourceLocation blockID = new ResourceLocation(itemID.getNamespace(), fixedString);
        Block actualConcrete = BuiltInRegistries.BLOCK.getOptional(blockID).orElse(Blocks.BLACK_CONCRETE);

        int l = scaledHeight - 32 + 3;
        int k = (int) (182 - ((float)182/800)*((float)RRE.getPavingTimer()));

        int vOffset;
        if (actualConcrete.equals(Blocks.BLACK_CONCRETE)) vOffset = 45;
        else if (actualConcrete.equals(Blocks.BLUE_CONCRETE)) vOffset = 65;
        else if (actualConcrete.equals(Blocks.BROWN_CONCRETE)) vOffset = 50;
        else if (actualConcrete.equals(Blocks.PURPLE_CONCRETE)) vOffset = 70;
        else if (actualConcrete.equals(Blocks.PINK_CONCRETE)) vOffset = 20;
        else if (actualConcrete.equals(Blocks.CYAN_CONCRETE)) vOffset = 15;
        else if (actualConcrete.equals(Blocks.MAGENTA_CONCRETE)) vOffset = 25;
        else if (actualConcrete.equals(Blocks.YELLOW_CONCRETE)) vOffset = 5;
        else if (actualConcrete.equals(Blocks.RED_CONCRETE)) vOffset = 10;
        else if (actualConcrete.equals(Blocks.GRAY_CONCRETE)) vOffset = 40;
        else if (actualConcrete.equals(Blocks.LIGHT_BLUE_CONCRETE)) vOffset = 75;
        else if (actualConcrete.equals(Blocks.LIGHT_GRAY_CONCRETE)) vOffset = 35;
        else if (actualConcrete.equals(Blocks.GREEN_CONCRETE)) vOffset = 60;
        else if (actualConcrete.equals(Blocks.LIME_CONCRETE)) vOffset = 0;
        else if (actualConcrete.equals(Blocks.WHITE_CONCRETE)) vOffset = 30;
        else if (actualConcrete.equals(Blocks.ORANGE_CONCRETE)) vOffset = 55;
        else vOffset = 0;

        context.blit(StandIcons.VEHICLE_ICONS, x, l, 0, 0, 182, 5);

        if (k > 0) {
            context.blit(StandIcons.VEHICLE_ICONS, x, l, 0, vOffset + 5, k, 5);
        }

        int iconU = 193;
        int iconV = 40;
        int iconW = 9;
        int iconH = 9;
        int iconX = scaledWidth / 2 - 5;
        int iconY = scaledHeight - 31 - 10;

        context.blit(StandIcons.VEHICLE_ICONS, iconX, iconY, iconU, iconV, iconW, iconH);

        int overlayU = 193;
        int overlayV = 141;
        if (actualConcrete.equals(Blocks.BLACK_CONCRETE)) overlayV = 140;
        else if (actualConcrete.equals(Blocks.BLUE_CONCRETE)) {
            overlayV = 60;
            overlayU = 209;
        }
        else if (actualConcrete.equals(Blocks.BROWN_CONCRETE)) overlayV = 150;
        else if (actualConcrete.equals(Blocks.PURPLE_CONCRETE)) {
            overlayV = 70;
            overlayU = 209;
        }
        else if (actualConcrete.equals(Blocks.PINK_CONCRETE)) overlayV = 90;
        else if (actualConcrete.equals(Blocks.CYAN_CONCRETE)) overlayV = 80;
        else if (actualConcrete.equals(Blocks.MAGENTA_CONCRETE)) overlayV = 100;
        else if (actualConcrete.equals(Blocks.YELLOW_CONCRETE)) overlayV = 60;
        else if (actualConcrete.equals(Blocks.RED_CONCRETE)) overlayV = 70;
        else if (actualConcrete.equals(Blocks.GRAY_CONCRETE)) overlayV = 130;
        else if (actualConcrete.equals(Blocks.LIGHT_BLUE_CONCRETE)) {
            overlayV = 80;
            overlayU = 209;
        }
        else if (actualConcrete.equals(Blocks.LIGHT_GRAY_CONCRETE)) overlayV = 120;
        else if (actualConcrete.equals(Blocks.GREEN_CONCRETE)) {
            overlayV = 50;
            overlayU = 209;
        }
        else if (actualConcrete.equals(Blocks.LIME_CONCRETE)) overlayV = 50;
        else if (actualConcrete.equals(Blocks.WHITE_CONCRETE)) overlayV = 110;
        else if (actualConcrete.equals(Blocks.ORANGE_CONCRETE)) overlayV = 160;
        else {
            overlayV = 141;
            overlayU = 193;
        }
        context.blit(StandIcons.VEHICLE_ICONS, iconX, iconY, overlayU, overlayV, iconW, iconH);
    }

    public static void renderRoadRollerPickupHud(GuiGraphics context, Minecraft client, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int x, float flashAlpha, float otherFlashAlpha, RoadRollerEntity RRE) {
        int l = scaledHeight - 32 + 3;
        int k = (int)(((float) 182 / 100f) * (float) RRE.getPickupTimer());

        context.blit(StandIcons.VEHICLE_ICONS, x, l, 0, 90, 182, 5);

        if (k > 0) {
            context.blit(StandIcons.VEHICLE_ICONS, x, l, 0, 85, k, 5);
        }

        int iconU = 183;
        int iconV = 0;
        int iconW = 9;
        int iconH = 9;
        int iconX = scaledWidth / 2 - 5;
        int iconY = scaledHeight - 31 - 10;

        context.blit(StandIcons.VEHICLE_ICONS, iconX, iconY, iconU, iconV, iconW, iconH);
    }

    public static void renderCreamVoidTimerHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                         int scaledWidth, int scaledHeight, int x,
                                         PowersCream PC) {
        int l = scaledHeight - 32 + 3;
        int k = (int)(((float) 182 / 100f) * (100 - (float) PC.getVoidTime()));

        context.blit(StandIcons.CREAM_ICONS, x, l, 0, 90, 182, 5);

        if (k > 0) {
            context.blit(StandIcons.CREAM_ICONS, x, l, 0, 5, k, 5);
        }

        int iconU = 183;
        int iconV = 0;
        int iconW = 9;
        int iconH = 9;
        int iconX = scaledWidth / 2 - 5;
        int iconY = scaledHeight - 31 - 5;

        context.blit(StandIcons.CREAM_ICONS, iconX, iconY, iconU, iconV, iconW, iconH);
    }

    public static void renderCreamTransformTimerHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                               int scaledWidth, int scaledHeight, int x,
                                               PowersCream PC) {
        int l = scaledHeight - 32 + 3;
        int k;
        if (PC.getTransformDirection() == 1) {
            k = (int)(((float) 182 / 100f) * (float) PC.getTransformTimer());
        } else {
            k = (int)(((float) 182 / 20f) * (float) PC.getTransformTimer());
        }

        context.blit(StandIcons.CREAM_ICONS, x, l, 0, 90, 182, 5);

        if (k > 0) {
            context.blit(StandIcons.CREAM_ICONS, x, l, 0, 5, k, 5);
        }

        int iconU = 183;
        int iconV = 10;
        int iconW = 9;
        int iconH = 9;
        int iconX = scaledWidth / 2 - 5;
        int iconY = scaledHeight - 31 - 5;

        context.blit(StandIcons.CREAM_ICONS, iconX, iconY, iconU, iconV, iconW, iconH);
    }

    private static int getFinalATimeInt(StandUser standUser) {
        int barrageWindup = standUser.roundabout$getStandPowers().getBarrageWindup();
        int barrageLength = standUser.roundabout$getStandPowers().getBarrageLength();
        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        int finalATimeInt;
        if (attackTimeDuring <= barrageWindup){
            finalATimeInt = Math.round((attackTimeDuring / barrageWindup)*15);
        } else {
            finalATimeInt = 15 - Math.round(((attackTimeDuring-barrageWindup) / barrageLength)*15);
        }
        return finalATimeInt;
    }

    public static void renderGrabbedHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int x,
                                      float flashAlpha, float otherFlashAlpha) {
        int l;
        int k;
        int v;
        l = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser) playerEntity);
        k = (int) Math.floor((182/30F)*standUser.roundabout$getRestrainedTicks());
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, 70, 182, 5);
        if (k > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 70+5, k, 5);
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;
        context.blit(StandIcons.JOJO_ICONS, k, l, u, 50, 9, 9);
    }

    public static void renderSealedDiscHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                        int scaledWidth, int scaledHeight, int ticks, int x,
                                        float flashAlpha, float otherFlashAlpha) {
        int l;
        int k;
        int v;
        l = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser) playerEntity);
        k = (int) Math.floor(((double) 182 /standUser.roundabout$getMaxSealedTicks())*(standUser.roundabout$getMaxSealedTicks()-standUser.roundabout$getSealedTicks()));
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, 90, 182, 5);
        if (k > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 90+5, k, 5);
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;
        context.blit(StandIcons.JOJO_ICONS, k, l, u, 60, 9, 9);
    }
    public static void renderWalkingHeartHud(GuiGraphics context, Player playerEntity,
                                             int scaledWidth, int scaledHeight, int x) {

        StandUser standUser = ((StandUser) playerEntity);
        if (standUser.roundabout$getStandPowers() instanceof PowersWalkingHeart PW) {

            int l;
            int k;
            l = scaledHeight - 32 + 3;

            int st = PW.getMaxShootTicks();
            int sc = PW.getShootTicks();
            sc = Mth.clamp(sc, 0, st);
            int blt2 = 182 - (int) Math.floor(((double) 182 / st) * (sc));

            int bleh = 10;
            if (!PW.inCombatMode()){
                bleh+=10;
            }

            context.blit(StandIcons.JOJO_ICONS_2, x, l, 0, bleh, 182, 5);
            if (blt2 > 0) {
                bleh+=5;
                context.blit(StandIcons.JOJO_ICONS_2, x, l, 0, bleh, blt2, 5);
            }

            int u = 183;
            k = scaledWidth / 2 - 5;
            l = scaledHeight - 31 - 5;

            if (PW.canShootSpikes(PW.getUseTicks())) {
                context.blit(StandIcons.JOJO_ICONS_2, k, l, u, 0, 9, 9);
            } else {
                context.blit(StandIcons.JOJO_ICONS_2, k, l, u, 10, 9, 9);
            }
        }

    }

    public static void renderInvisibilityHUD(GuiGraphics context, Player playerEntity,
                                                 int scaledWidth, int scaledHeight, int x) {

        int l;
        int gb = ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstDuration;
        int gc =((IEntityAndData)playerEntity).roundabout$getTrueInvisibility();  gc= Mth.clamp(gc,0,gb);
        int gc2 = Mth.floor(((float)gc+19)/20);
        l = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser)playerEntity);
        int blt = (int) Math.floor(((double) 182 /gb)*(gc));
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, 0, 182, 5);
        if (blt > 0) {
            context.blit(StandIcons.JOJO_ICONS_2, x, l, 0, 5, blt, 5);
        }

        Minecraft minecraft = Minecraft.getInstance();
        int y = 10398321;
        Font renderer = minecraft.font;
        String $$6 = gc2 + "";
        int $$7 = (scaledWidth - renderer.width($$6)) / 2;
        int $$8 = scaledHeight - 31 - 4;
        context.drawString(renderer, $$6, $$7 + 1, $$8, 0, false);
        context.drawString(renderer, $$6, $$7 - 1, $$8, 0, false);
        context.drawString(renderer, $$6, $$7, $$8 + 1, 0, false);
        context.drawString(renderer, $$6, $$7, $$8 - 1, 0, false);
        context.drawString(renderer, $$6, $$7, $$8, y, false);

    }

    public static void renderShootModeSoftAndWet(GuiGraphics context, Minecraft client, Player playerEntity,
                                    int scaledWidth, int scaledHeight, int x,
                                    PowersSoftAndWet PW) {
        int l;
        int k;
        int gb = PW.getMaxGoBeyondChargeTicks();
        int gc = PW.getGoBeyondCharge(); gc= Mth.clamp(gc,0,gb);
        l = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser)playerEntity);
        if (PW.getInExplosiveSpinMode()) {

            int blt = (int) Math.floor(((double) 182 /gb)*(gc));
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 151, 182, 5);
            if (blt > 0) {
                context.blit(StandIcons.JOJO_ICONS, x, l, 0, 156, blt, 5);
            }
        } else {

            int st = PW.getMaxShootTicks();
            int sc = PW.getShootTicks();  sc= Mth.clamp(sc,0,st);
            int blt = (int) Math.floor(((double) 182 /gb)*(gc));
            int blt2 = (int) Math.floor(((double) 182 /st)*(sc));
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 141, 182, 2);
            context.blit(StandIcons.JOJO_ICONS, x, l+2, 0, 143, 182, 3);
            if (blt > 0) {
                context.blit(StandIcons.JOJO_ICONS, x, l, 0, 146, blt, 2);
            }
            if (blt2 > 0) {
                context.blit(StandIcons.JOJO_ICONS, x, l+2, 0, 148, blt2, 3);
            }
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;
        if (PW.getInExplosiveSpinMode()) {
            context.blit(StandIcons.JOJO_ICONS, k, l, u, 80, 9, 9);
        } else if (PW.canShootExplosive(PW.getUseTicks())){
            context.blit(StandIcons.JOJO_ICONS, k, l, u, 70, 9, 9);
        } else {
            context.blit(StandIcons.JOJO_ICONS, k, l, u, 90, 9, 9);
        }

    }

    public static void renderShootModeLightSoftAndWet(GuiGraphics context, Minecraft client, Player playerEntity,
                                                 int scaledWidth, int scaledHeight, int x,
                                                 PowersSoftAndWet PW) {

        int l;
        int k;
        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;
        if (PW.getInExplosiveSpinMode()) {
            context.blit(StandIcons.JOJO_ICONS, k, l, u, 80, 9, 9);
        } else if (PW.canShootExplosive(PW.getUseTicks())){
            context.blit(StandIcons.JOJO_ICONS, k, l, u, 70, 9, 9);
        } else {
            context.blit(StandIcons.JOJO_ICONS, k, l, u, 90, 9, 9);
        }
    }

    public static void renderExpHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                           int scaledWidth, int scaledHeight, int ticks, int x,
                                           float flashAlpha, float otherFlashAlpha, boolean removeNum) {
        int l;
        int k;
        int v;
        StandUser standUser = ((StandUser)playerEntity);
        byte level = ((IPlayerEntity)playerEntity).roundabout$getStandLevel();
        int exp = ((IPlayerEntity)playerEntity).roundabout$getStandExp();
        int maxXP = standUser.roundabout$getStandPowers().getExpForLevelUp(level);
        if (level == standUser.roundabout$getStandPowers().getMaxLevel() ||
                (!standUser.roundabout$getStandDisc().isEmpty() && standUser.roundabout$getStandDisc().getItem()
                        instanceof MaxStandDiscItem)) {
            exp = maxXP;
        }
        int blt = (int) Math.floor(((double) 182 /maxXP)*(exp));
        l = scaledHeight - 32 + 3;
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, 100, 182, 5);
        if (blt > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 105, blt, 5);
        }

        if (!removeNum) {

            int y = 6141337;
            Font renderer = client.font;
            String $$6 = level + "";
            int $$7 = (scaledWidth - renderer.width($$6)) / 2;
            int $$8 = scaledHeight - 31 - 4;
            context.drawString(renderer, $$6, $$7 + 1, $$8, 0, false);
            context.drawString(renderer, $$6, $$7 - 1, $$8, 0, false);
            context.drawString(renderer, $$6, $$7, $$8 + 1, 0, false);
            context.drawString(renderer, $$6, $$7, $$8 - 1, 0, false);
            context.drawString(renderer, $$6, $$7, $$8, y, false);
        }
    }

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    public static void renderExperienceBar(Minecraft client,int scaledWidth, int scaledHeight, GuiGraphics $$0) {
        int $$2 = client.player.getXpNeededForNextLevel();
        if ($$2 > 0) {
            int $$3 = 182;
            int $$4 = (int)(client.player.experienceProgress * 183.0F);
            int $$5 = scaledHeight - 32 + 3;
            int $$7 = scaledWidth / 2 - 91;
            $$0.blit(GUI_ICONS_LOCATION, $$7, $$5, 0, 64, 182, 5);
            if ($$4 > 0) {
                $$0.blit(GUI_ICONS_LOCATION, $$7, $$5, 0, 69, $$4, 5);
            }
        }

    }

    // basic function which displays a xp bar with a min and a max, feel free to change around if needed
    // bar is the y function of
    public static void renderNumberHUD(GuiGraphics context, Minecraft client, int scaledWidth, int scaledHeight,
                                       int x, double value, double max, ResourceLocation file, int bx, int by, int color) {
        int l;
        int k;
        int v;
        int blt = (int) Math.floor(((double) 182 / max)*(value));
        l = scaledHeight - 32 + 3;
        context.blit(file, x, l, bx, by, 182, 5);
        if (blt > 0) {
            context.blit(file, x, l, bx, by+5, blt, 5);
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;

        int y = color;
        Font renderer = client.font;
        String $$6 = (int)value + "";
        int $$7 = (scaledWidth - renderer.width($$6)) / 2;
        int $$8 = scaledHeight - 31 - 4;
        context.drawString(renderer, $$6, $$7 + 1, $$8, 0, false);
        context.drawString(renderer, $$6, $$7 - 1, $$8, 0, false);
        context.drawString(renderer, $$6, $$7, $$8 + 1, 0, false);
        context.drawString(renderer, $$6, $$7, $$8 - 1, 0, false);
        context.drawString(renderer, $$6, $$7, $$8, y, false);
    }

    public static void renderDistanceHUDJustice(GuiGraphics context, Minecraft client, Player playerEntity,
                                                int scaledWidth, int scaledHeight, int ticks, int x, StandEntity stand) {
        int l;
        int k;
        int v;
        StandUser standUser = ((StandUser)playerEntity);
        StandPowers powers = standUser.roundabout$getStandPowers();
        int mode = powers.getPilotMode();
        int maxDistance = powers.getMaxPilotRange();
        int distance = 1;
        if (mode == 1){
            distance = (int) stand.position().distanceTo(playerEntity.position());
        } else if (mode == 2){
            distance = (int) MainUtil.cheapDistanceTo2(stand.getX(),stand.getZ(),playerEntity.getX(),playerEntity.getZ());
        }
        int blt = (int) Math.floor(((double) 182 /maxDistance)*(distance));
        l = scaledHeight - 32 + 3;
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, 131, 182, 5);
        if (blt > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 136, blt, 5);
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;

        int y = 16173823;
        Font renderer = client.font;
        String $$6 = distance + "";
        int $$7 = (scaledWidth - renderer.width($$6)) / 2;
        int $$8 = scaledHeight - 31 - 4;
        context.drawString(renderer, $$6, $$7 + 1, $$8, 0, false);
        context.drawString(renderer, $$6, $$7 - 1, $$8, 0, false);
        context.drawString(renderer, $$6, $$7, $$8 + 1, 0, false);
        context.drawString(renderer, $$6, $$7, $$8 - 1, 0, false);
        context.drawString(renderer, $$6, $$7, $$8, y, false);
    }
    public static void renderGuardHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int x,
                                      float flashAlpha, float otherFlashAlpha) {
        int l;
        int k;
        int v;
        l = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser) playerEntity);
        if (standUser.roundabout$getGuardBroken() || !standUser.roundabout$shieldNotDisabled()){
            v = 10;
        } else {
            v = 0;
        }
        k = (int) Math.floor((182/standUser.roundabout$getMaxGuardPoints())*standUser.roundabout$getGuardPoints());
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, v, 182, 5);
        if (k > 0) {
           context.blit(StandIcons.JOJO_ICONS, x, l, 0, v+5, k, 5);
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;
        context.blit(StandIcons.JOJO_ICONS, k, l, u, v, 9, 9);
    }

    public static void renderPossessionHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int x) {
        int l = scaledHeight - 32 + 3;
        int k = (int)(((float) 182 / 100f) * (float) ((StandUser)playerEntity).roundabout$getPossessionTime() );

        context.blit(StandIcons.JOJO_ICONS, x, l, 0, 161, 182, 5);

        if (k > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, 166, k, 5);
        }

        int iconX = scaledWidth / 2 - 5;
        int iconY = scaledHeight - 32 - 4;

        context.blit(StandIcons.JOJO_ICONS, iconX, iconY, 182, 161, 11, 9);
    }

    public static void renderTSHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int x,
                                      float flashAlpha, float otherFlashAlpha, boolean isTSEntity, Font font) {
        int l;
        int k;
        int v;
        l = scaledHeight - 32 + 3;
        int z;
        int y;
        StandUser standUser = ((StandUser) playerEntity);
        if (isTSEntity || standUser.roundabout$getStandPowers().getIsTsCharging()){
            v = 50;
            k = (int) Math.floor((182 /((double) standUser.roundabout$getStandPowers().getMaxChargeTSTime() /20))*((double) standUser.roundabout$getStandPowers().getChargedTSTicks() /20));
            z =  (int) Math.min((Math.floor(((double) standUser.roundabout$getStandPowers().getChargedTSTicks()+19)/20)),((double) standUser.roundabout$getStandPowers().getMaxChargeTSTime() /20));
            y = 7654088;
        } else {
            v = 60;
            y = 7836819;
            TimeStopInstance tsi = ((TimeStop)playerEntity.level()).getTimeStopperInstanceClient(playerEntity.position());
            if (tsi != null) {
                k = (int) Math.floor((182 /((double) tsi.maxDuration /20))*((double) tsi.duration /20));
                z =  (int) Math.min((Math.floor(((double) tsi.duration+19)/20)),((double) tsi.maxDuration /20));

            } else {
                k = 0;
                z = 0;
            }
        }
        context.blit(StandIcons.JOJO_ICONS, x, l, 0, v, 182, 5);
        if (k > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, l, 0, v+5, k, 5);
        }

        String $$6 = z + "";
        int $$7 = (scaledWidth - font.width($$6)) / 2;
        int $$8 = scaledHeight - 31 - 4;
        context.drawString(font, $$6, $$7 + 1, $$8, 0, false);
        context.drawString(font, $$6, $$7 - 1, $$8, 0, false);
        context.drawString(font, $$6, $$7, $$8 + 1, 0, false);
        context.drawString(font, $$6, $$7, $$8 - 1, 0, false);
        context.drawString(font, $$6, $$7, $$8, y, false);
    }


    public static void renderClashHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int x,
                                      float flashAlpha, float otherFlashAlpha, float c) {

        int d = (int) (c * 183.0f);


        int e = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser) playerEntity);
        context.blit(StandIcons.JOJO_ICONS, x, e, 0, 20, 182, 5);
        if (d > 0) {
            context.blit(StandIcons.JOJO_ICONS, x, e, 0, 25, d, 5);
        }

        int f = scaledWidth / 2 - 5;
        int g = scaledHeight - 31 - 5;
        context.blit(StandIcons.JOJO_ICONS, f, g, 183, 20, 9, 9);

        LivingEntity clashOp = (((StandUser) client.player).roundabout$getStandPowers().getClashOp());
        if (clashOp != null) {
            int i = context.guiWidth();
            int j = 12;
            int k = i / 2 - 91;
            int l = j;

            context.blit(StandIcons.JOJO_ICONS, k, l, 0, 40, 182, 5);
            float q = (((StandUser) client.player).roundabout$getStandPowers().getClashOpProgress());
            int r = (int) (q * 183.0f);
            context.blit(StandIcons.JOJO_ICONS, k, l, 0, 45, r, 5);

            Component text = clashOp.getName();
            int m = client.font.width(text);
            int n = i / 2 - m / 2;
            int o = l - 9;
            context.drawString(client.font, text, n, o, 0xFFFFFF);

            context.blit(StandIcons.JOJO_ICONS, f, l+5, 183, 20, 9, 9);
        }
    }

/**
    public static void renderGuardHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity,
                                       int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                       float flashAlpha, float otherFlashAlpha) {
        if (playerEntity != null){

            int x = 0;
            int y = 0;

            int width = scaledWidth;
            int height = scaledHeight;
            int twidth = 51;
            x = width/2;
            y = height;
            boolean standOn = MyComponents.STAND_USER.get(playerEntity).getActive();
            NbtCompound pd = ((IEntityDataSaver) playerEntity).getPersistentData();
            if (standOn){
                int age = Math.toIntExact(pd.getLong("guard") - Math.round(playerEntity.getWorld().getTime()));
                if (age <=0 || age > 201){age=0;}
                //1000 -> 10
                //1200
                int twidth2 = (twidth*(200-age))/200;
                //Draws the empty bar
                context.drawTexture(GUARD_EMPTY,x-20,y-67,0, 0, twidth, 5, twidth, 5);
                //Draws the full bar over it. Scales to age.
                context.drawTexture(GUARD_FILLED,x-20,y-67,0, 0, twidth2, 5, twidth, 5);
                //Draws the little shield icon
                context.drawTexture(GUARD_ICON,x-30,y-68,0, 0, 7, 7, 7, 7);

                //TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                //drawContext.drawText(renderer, DEBUG_TEXT_1,x-50,y-50,0xffffff,true);
            } else {
                //TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                //drawContext.drawText(renderer, ""+StandData.isActive((IEntityDataSaver) MinecraftClient.getInstance().player),x-50,y-50,0xffffff,true);
            }



        }
    }*/

}

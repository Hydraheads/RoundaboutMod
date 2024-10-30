package net.hydra.jojomod.client.hud;


import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.gui.PowerInventoryMenu;
import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class StandHudRender {
    /** Renders the HUD for stand attacks/abilities.
     * Keep in mind it has to slide in so some of the code may look awkward.*/


    private static final int guiSize = 174;
    private static float animated = 0;
    public static void renderStandHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                      float flashAlpha, float otherFlashAlpha) {
        if (playerEntity != null) {
            int x = 0;
            int y = 0;

            int width = scaledWidth;
            int height = scaledHeight;
            int textureWidth = guiSize;
            int textureHeight = 30;


            int iconHeight = 18;
            int iconWidth = 18;
            x = (int) (-20-guiSize+animated);
            //x = (int) (-20);
            y = 5;
            Minecraft mc = Minecraft.getInstance();
            float tickDelta = mc.getDeltaFrameTime();

            boolean standOn = ((StandUser) playerEntity).roundabout$getActive();
            if (standOn || animated > 0.1){
                if (!standOn){
                    animated = Math.max(controlledLerp(tickDelta, animated,0,0.5f),0);
                } else {
                    if (animated < guiSize) {
                        animated++;
                        animated = Math.min(controlledLerp(tickDelta, animated,guiSize,0.5f),guiSize);
                    }
                }
                context.setColor(1.0f, 1.0f, 1.0f, 0.9f);
                //Draws the empty bar
                //context.drawTexture(ARROW_ICON,x,y-2,0, 0, textureWidth, textureHeight, textureWidth, textureHeight);



                ((StandUser) playerEntity).roundabout$getStandPowers().renderIcons(context, x, y);




                Font renderer = mc.font;
                Component special1Key = KeyInputRegistry.abilityOneKey.getTranslatedKeyMessage();
                Component special2Key = KeyInputRegistry.abilityTwoKey.getTranslatedKeyMessage();
                Component special3Key = KeyInputRegistry.abilityThreeKey.getTranslatedKeyMessage();
                Component special4Key = KeyInputRegistry.abilityFourKey.getTranslatedKeyMessage();
                special1Key = fixKey(special1Key);
                special2Key = fixKey(special2Key);
                special3Key = fixKey(special3Key);
                special4Key = fixKey(special4Key);
                context.drawString(renderer, special1Key,x+24,y+11,0xffffff,true);
                context.drawString(renderer, special2Key,x+49,y+11,0xffffff,true);
                context.drawString(renderer, special3Key,x+74,y+11,0xffffff,true);
                context.drawString(renderer, special4Key,x+99,y+11,0xffffff,true);

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
        }
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

    public static void renderExpHud(GuiGraphics context, Minecraft client, Player playerEntity,
                                           int scaledWidth, int scaledHeight, int ticks, int x,
                                           float flashAlpha, float otherFlashAlpha) {
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

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;

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

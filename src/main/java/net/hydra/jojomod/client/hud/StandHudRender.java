package net.hydra.jojomod.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.client.KeyInputHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StandHudRender {
    /** Renders the HUD for stand attacks/abilities.
     * Keep in mind it has to slide in so some of the code may look awkward.*/
    private static final Identifier JOJO_ICONS = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/jojo_icons.png");

    private static final Identifier SQUARE_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/move_square2.png");

    private static final Identifier SPECIAL_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/icons/the_world/stop_time.png");

    private static final Identifier SKILL2_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/icons/the_world/assault.png");

    private static final Identifier HASTE_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/icons/the_world/haste.png");


    private static final int guiSize = 174;
    private static float animated = 0;
    public static void renderStandHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                      float flashAlpha, float otherFlashAlpha) {
        if (playerEntity != null) {
            int x = 0;
            int y = 0;

            int width = scaledWidth;
            int height = scaledHeight;
            int textureWidth = guiSize;
            int textureHeight = 30;

            int squareHeight = 24;
            int squareWidth = 24;

            int iconHeight = 18;
            int iconWidth = 18;
            x = (int) (-20-guiSize+animated);
            //x = (int) (-20);
            y = 5;
            MinecraftClient mc = MinecraftClient.getInstance();
            float tickDelta = mc.getLastFrameDuration();

            boolean standOn = ((StandUser) playerEntity).getActive();
            if (standOn || animated > 0.1){
                if (!standOn){
                    animated = Math.max(controlledLerp(tickDelta, animated,0,0.5f),0);
                } else {
                    if (animated < guiSize) {
                        animated++;
                        animated = Math.min(controlledLerp(tickDelta, animated,guiSize,0.5f),guiSize);
                    }
                }
                context.setShaderColor(1.0f, 1.0f, 1.0f, 0.9f);
                //Draws the empty bar
                //context.drawTexture(ARROW_ICON,x,y-2,0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
                context.drawTexture(SQUARE_ICON,x+21,y-4,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
                context.drawTexture(SQUARE_ICON,x+47,y-4,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
                context.drawTexture(SQUARE_ICON,x+72,y-4,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
                context.drawTexture(SQUARE_ICON,x+97,y-4,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);

                context.drawTexture(SKILL2_ICON,x+50,y-1,0, 0, iconWidth, iconHeight, iconWidth, iconHeight);
                context.drawTexture(SPECIAL_ICON,x+100,y-1,0, 0, iconWidth, iconHeight, iconWidth, iconHeight);



                TextRenderer renderer = mc.textRenderer;
                Text dashKey = KeyInputHandler.abilityOneKey.getBoundKeyLocalizedText();
                Text special1Key = KeyInputHandler.abilityTwoKey.getBoundKeyLocalizedText();
                Text special2Key = KeyInputHandler.abilityThreeKey.getBoundKeyLocalizedText();
                Text ultimateKey = KeyInputHandler.abilityFourKey.getBoundKeyLocalizedText();
                dashKey = fixKey(dashKey);
                special1Key = fixKey(special1Key);
                special2Key = fixKey(special2Key);
                ultimateKey = fixKey(ultimateKey);
                context.drawText(renderer, special1Key,x+24,y+11,0xffffff,true);
                context.drawText(renderer, special2Key,x+49,y+11,0xffffff,true);
                context.drawText(renderer, dashKey,x+74,y+11,0xffffff,true);
                context.drawText(renderer, ultimateKey,x+99,y+11,0xffffff,true);

                context.setShaderColor(1.0f, 1.0f, 1.0f, 1f);
            }
        }
    }


    public static Text fixKey(Text textIn){

        String X = textIn.getString();
        if (X.length() > 1){
        String[] split = X.split("\\s");
        if (split.length > 1){
            return Text.of(""+split[0].charAt(0)+split[1].charAt(0));
        } else {
            if (split[0].length() > 1){
                return Text.of(""+split[0].charAt(0)+split[0].charAt(1));
            } else {
                return Text.of(""+split[0].charAt(0));
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
    public static void renderAttackHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity,
                                       int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                       float flashAlpha, float otherFlashAlpha){
        if (playerEntity != null) {
            StandUser standUser = ((StandUser) playerEntity);
            boolean standOn = standUser.getActive();
            int j = scaledHeight / 2 - 7 - 4;
            int k = scaledWidth / 2 - 8;

            float attackTimeDuring = standUser.getAttackTimeDuring();
            if (standOn && standUser.isClashing()) {
                int ClashTime = 15 - Math.round((attackTimeDuring / 60)*15);
                context.drawTexture(JOJO_ICONS, k, j, 193, 6, 15, 6);
                context.drawTexture(JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

            } else if (standOn && standUser.getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
                int ClashTime = 15 - Math.round((attackTimeDuring / standUser.getStandPowers().getBarrageLength())*15);
                context.drawTexture(JOJO_ICONS, k, j, 193, 6, 15, 6);
                context.drawTexture(JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

            } else if (standOn && standUser.getStandPowers().isBarrageCharging()) {
                int ClashTime = Math.round((attackTimeDuring / standUser.getStandPowers().getBarrageWindup())*15);
                context.drawTexture(JOJO_ICONS, k, j, 193, 6, 15, 6);
                context.drawTexture(JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

            } else {
                int barTexture = 0;
                Entity TE = standUser.getTargetEntity(playerEntity, -1);
                float attackTimeMax = standUser.getAttackTimeMax();
                if (attackTimeMax > 0) {
                    float attackTime = standUser.getAttackTime();
                    float finalATime = attackTime / attackTimeMax;
                    if (finalATime <= 1) {


                        if (standUser.getActivePowerPhase() == standUser.getActivePowerPhaseMax()) {
                            barTexture = 24;
                        } else {
                            if (TE != null) {
                                barTexture = 12;
                            } else {
                                barTexture = 18;
                            }
                        }


                        context.drawTexture(JOJO_ICONS, k, j, 193, 6, 15, 6);
                        int finalATimeInt = Math.round(finalATime * 15);
                        context.drawTexture(JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                    }
                }
                if (standOn) {
                    if (TE != null) {
                        if (barTexture == 0) {
                            context.drawTexture(JOJO_ICONS, k, j, 193, 0, 15, 6);
                        }
                    }
                }
            }
        }
    }

    private static int getFinalATimeInt(StandUser standUser) {
        int barrageWindup = standUser.getStandPowers().getBarrageWindup();
        int barrageLength = standUser.getStandPowers().getBarrageLength();
        float attackTimeDuring = standUser.getAttackTimeDuring();
        int finalATimeInt;
        if (attackTimeDuring <= barrageWindup){
            finalATimeInt = Math.round((attackTimeDuring / barrageWindup)*15);
        } else {
            finalATimeInt = 15 - Math.round(((attackTimeDuring-barrageWindup) / barrageLength)*15);
        }
        return finalATimeInt;
    }

    public static void renderGuardHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int x,
                                      float flashAlpha, float otherFlashAlpha) {
        int l;
        int k;
        int v;
        l = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser) playerEntity);
        if (standUser.getGuardBroken() || !standUser.shieldNotDisabled()){
            v = 10;
        } else {
            v = 0;
        }
        k = (int) Math.floor((182/standUser.getMaxGuardPoints())*standUser.getGuardPoints());
        context.drawTexture(JOJO_ICONS, x, l, 0, v, 182, 5);
        if (k > 0) {
           context.drawTexture(JOJO_ICONS, x, l, 0, v+5, k, 5);
        }

        int u = 183;
        k = scaledWidth/2 - 5;
        l = scaledHeight - 31 - 5;
        context.drawTexture(JOJO_ICONS, k, l, u, v, 9, 9);
    }


    public static void renderClashHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity,
                                      int scaledWidth, int scaledHeight, int ticks, int x,
                                      float flashAlpha, float otherFlashAlpha, float c) {

        int d = (int) (c * 183.0f);


        int e = scaledHeight - 32 + 3;
        StandUser standUser = ((StandUser) playerEntity);
        context.drawTexture(JOJO_ICONS, x, e, 0, 20, 182, 5);
        if (d > 0) {
            context.drawTexture(JOJO_ICONS, x, e, 0, 25, d, 5);
        }

        int f = scaledWidth / 2 - 5;
        int g = scaledHeight - 31 - 5;
        context.drawTexture(JOJO_ICONS, f, g, 183, 20, 9, 9);

        LivingEntity clashOp = (((StandUser) client.player).getStandPowers().getClashOp());
        if (clashOp != null) {
            int i = context.getScaledWindowWidth();
            int j = 12;
            int k = i / 2 - 91;
            int l = j;

            context.drawTexture(JOJO_ICONS, k, l, 0, 40, 182, 5);
            float q = (((StandUser) client.player).getStandPowers().getClashOpProgress());
            int r = (int) (q * 183.0f);
            context.drawTexture(JOJO_ICONS, k, l, 0, 45, r, 5);

            Text text = clashOp.getName();
            int m = client.textRenderer.getWidth(text);
            int n = i / 2 - m / 2;
            int o = l - 9;
            context.drawTextWithShadow(client.textRenderer, text, n, o, 0xFFFFFF);

            context.drawTexture(JOJO_ICONS, f, l+5, 183, 20, 9, 9);
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

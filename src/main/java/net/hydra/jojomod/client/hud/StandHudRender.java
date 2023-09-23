package net.hydra.jojomod.client.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.KeyInputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class StandHudRender {
    private static final Identifier GUARD_EMPTY = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/guard_meter.png");
    private static final Identifier GUARD_FILLED = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/guard_filled.png");
    private static final Identifier GUARD_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/guard_icon.png");

    private static final Identifier ARROW_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/stand_hud.png");

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

            boolean standOn = ((IEntityDataSaver) playerEntity).getStandOn();
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

            NbtCompound pd = ((IEntityDataSaver) playerEntity).getPersistentData();
            if (((IEntityDataSaver) playerEntity).getStandOn()){
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
    }
}

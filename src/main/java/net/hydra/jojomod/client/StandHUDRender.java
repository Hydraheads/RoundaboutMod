package net.hydra.jojomod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.util.IEntityDataSaver;
import net.hydra.jojomod.util.StandData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class StandHUDRender implements HudRenderCallback {

    public static final String DEBUG_TEXT_1 = "hud.roundabout.standout";
    private static final Identifier GUARD_EMPTY = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/guard_meter.png");
    private static final Identifier GUARD_FILLED = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/guard_filled.png");
    private static final Identifier GUARD_ICON = new Identifier(RoundaboutMod.MOD_ID,
            "textures/gui/guard_icon.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = 0;
        int y = 0;
        if (client != null){
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int twidth = 51;
            x = width/2;
            y = height;
            //RoundaboutMod.LOGGER.info("LMAO " + client.player.age);
            NbtCompound pd = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData();
            if (pd.getBoolean("active_stand")){

                int age = pd.getInt("guard") - client.player.age;
                if (age <=0 || age > 200){age=0;}
                int twidth2 = (twidth*(200-age))/200;

                //Draws the empty bar
                    drawContext.drawTexture(GUARD_EMPTY,x-20,y-67,0, 0, twidth, 5, twidth, 5);
                //Draws the full bar over it. Scales to age.
                    drawContext.drawTexture(GUARD_FILLED,x-20,y-67,0, 0, twidth2, 5, twidth, 5);
                //Draws the little shield icon
                    drawContext.drawTexture(GUARD_ICON,x-30,y-68,0, 0, 7, 7, 7, 7);

                //TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                //drawContext.drawText(renderer, DEBUG_TEXT_1,x-50,y-50,0xffffff,true);
            } else {
                //TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                //drawContext.drawText(renderer, ""+StandData.isActive((IEntityDataSaver) MinecraftClient.getInstance().player),x-50,y-50,0xffffff,true);
            }


        }

        //drawContext.drawText(renderer, DEBUG_TEXT_1,0,0,0xffffff,true);
        //drawContext.drawText(renderer, "hi",10,10,0xffffff,true);
        //renderer.draw(DEBUG_TEXT_1, 0, 0, 0xffffff, true, matrixStack);

    }

   // @Override
    //public void onHudRender(float tickDelta) {
}
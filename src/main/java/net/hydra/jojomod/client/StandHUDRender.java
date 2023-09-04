package net.hydra.jojomod.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class StandHUDRender implements HudRenderCallback {

    public static final String DEBUG_TEXT_1 = "hud.roundabout.standout";

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = 0;
        int y = 0;
        if (client != null){
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width/2;
            y = height;
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
           drawContext.drawText(renderer, DEBUG_TEXT_1,x-50,y-50,0xffffff,true);

        }

        //drawContext.drawText(renderer, DEBUG_TEXT_1,0,0,0xffffff,true);
        //drawContext.drawText(renderer, "hi",10,10,0xffffff,true);
        //renderer.draw(DEBUG_TEXT_1, 0, 0, 0xffffff, true, matrixStack);

    }

   // @Override
    //public void onHudRender(float tickDelta) {
}

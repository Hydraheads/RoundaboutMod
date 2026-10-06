package net.hydra.jojomod.chocolatedisco.client;

import net.hydra.jojomod.chocolatedisco.client.screens.ChocolateDiscoSelectionScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ChocolateDiscoHud {

    public static void open() {

        Minecraft minecraft =
                Minecraft.getInstance();

        Player player =
                minecraft.player;

        if (player == null) {
            return;
        }

        minecraft.setScreen(
                new ChocolateDiscoSelectionScreen(
                        player
                )
        );
    }
}
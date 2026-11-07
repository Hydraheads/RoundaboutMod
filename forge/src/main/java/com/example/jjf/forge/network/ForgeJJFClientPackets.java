package com.example.jjf.forge.network;

import com.example.jjf.zero_gravity.ZeroGravitySync;
import net.minecraft.client.Minecraft;

public final class ForgeJJFClientPackets {

    private ForgeJJFClientPackets() {
    }

    public static void apply(ZeroGravitySync.Snapshot snapshot) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            ZeroGravitySync.applyClient(client.player, snapshot);
        }
    }
}

package net.hydra.jojomod.client;

import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

public class GERNetherIllusionHelper {
    public static boolean active = false;
    public static boolean endActive = false;

    public static void update(Player player) {
        boolean inGerPlane = player.isAlive() && PowerTypes.getPlaneOfExisting(player) == 13;

        boolean shouldBeNether;
        boolean shouldBeEnd;
        if (!inGerPlane) {
            shouldBeNether = false;
            shouldBeEnd = false;
        } else {
            shouldBeNether = active || !player.level().getEntitiesOfClass(
                    Blaze.class, player.getBoundingBox().inflate(16.0D)
            ).isEmpty();
            shouldBeEnd = endActive || !player.level().getEntitiesOfClass(
                    EnderMan.class, player.getBoundingBox().inflate(16.0D)
            ).isEmpty();
        }

        if (shouldBeNether != active || shouldBeEnd != endActive) {
            active = shouldBeNether;
            endActive = shouldBeEnd;
            Minecraft mc = Minecraft.getInstance();
            int x = player.getBlockX();
            int y = player.getBlockY();
            int z = player.getBlockZ();
            mc.levelRenderer.setBlocksDirty(x - 64, y - 32, z - 64, x + 64, y + 32, z + 64);
            mc.levelRenderer.allChanged();
        }
    }
}
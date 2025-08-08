package net.hydra.jojomod.util.gravity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;

//https://github.com/qouteall/GravityChanger/tree/1.20.1-Fabric/src/main/java/gravity_changer/util
//credit to quoteall

public class GCUtil {
    public static MutableComponent getLinkText(String link) {
        return Component.literal(link).withStyle(
                style -> style.withClickEvent(new ClickEvent(
                        ClickEvent.Action.OPEN_URL, link
                )).withUnderlined(true)
        );
    }

    public static MutableComponent getDirectionText(Direction gravityDirection) {
        return Component.translatable("direction." + gravityDirection.getName());
    }

    public static double distanceToRange(double value, double rangeStart, double rangeEnd) {
        if (value < rangeStart) {
            return rangeStart - value;
        }

        if (value > rangeEnd) {
            return value - rangeEnd;
        }

        return 0;
    }

    public static boolean isClientPlayer(Entity entity) {
        if (entity.level().isClientSide()) {
            return entity instanceof LocalPlayer;
        }
        return false;
    }

    public static boolean isRemotePlayer(Entity entity) {
        if (entity.level().isClientSide()) {
            return entity instanceof RemotePlayer;
        }
        return false;
    }
}

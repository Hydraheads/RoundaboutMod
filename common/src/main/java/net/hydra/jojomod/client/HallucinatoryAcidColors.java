package net.hydra.jojomod.client;

import net.hydra.jojomod.block.HallucinatoryAcidBlock;
import net.hydra.jojomod.block.HallucinatoryAcidWallBlock;
import net.hydra.jojomod.entity.projectile.HallucinatoryAcidProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class HallucinatoryAcidColors {
    private static final float OVERLAY_OPACITY = 0.6F;
    private static final int[] SKIN_COLORS = {
            0xB0B2ED, 0xDAD9EB, 0xC5A5F7, 0xA9F3AA, 0xECEBC8,
            0xB9F7FB, 0xA386AE, 0xB48082, 0xFFE0B6, 0xDADADA,
            0xDFC7ED, 0xDAD9EB, 0x62858D, 0x676793, 0xCFEBCD,
            0xB0B2ED, 0xB0B2ED
    };
    private static final int ANIME_COLOR = SKIN_COLORS[0];

    private HallucinatoryAcidColors() {
    }

    public static int blockColor(BlockState state, int tintIndex) {
        if (tintIndex != 0) return 0xFFFFFF;
        if (state.hasProperty(HallucinatoryAcidBlock.SKIN)) {
            return tint(state.getValue(HallucinatoryAcidBlock.SKIN));
        }
        if (state.hasProperty(HallucinatoryAcidWallBlock.SKIN)) {
            return tint(state.getValue(HallucinatoryAcidWallBlock.SKIN));
        }
        return 0xFFFFFF;
    }

    public static int itemColor(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? tint(HallucinatoryAcidProjectile.itemSkin(stack)) : 0xFFFFFF;
    }

    public static int tint(int skin) {
        int color = SKIN_COLORS[Math.max(0, Math.min(skin, SKIN_COLORS.length - 1))];
        return channelTint(color >> 16, ANIME_COLOR >> 16) << 16
                | channelTint(color >> 8, ANIME_COLOR >> 8) << 8
                | channelTint(color, ANIME_COLOR);
    }

    public static int displayColor(int skin) {
        int color = SKIN_COLORS[Math.max(0, Math.min(skin, SKIN_COLORS.length - 1))];
        return blendChannel(color >> 16) << 16 | blendChannel(color >> 8) << 8 | blendChannel(color);
    }

    private static int channelTint(int target, int anime) {
        float targetOverlay = blendChannel(target);
        float animeOverlay = blendChannel(anime);
        return Math.min(255, Math.round(targetOverlay / animeOverlay * 255.0F));
    }

    private static int blendChannel(int channel) {
        return Math.round(255.0F * (1.0F - OVERLAY_OPACITY)
                + (channel & 255) * OVERLAY_OPACITY);
    }
}

package net.hydra.jojomod.networking;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerPlayer;
import net.zetalasis.networking.message.impl.IMessageEvent;
import org.jetbrains.annotations.Nullable;

public class ServerToClientPackets {
    public static class S2CPackets implements IMessageEvent {
        public enum MESSAGES {
            Rewind("rewind"),
            Interpolate("unskip_interpolation"),
            Chrono("chrono_vision_player"),
            MANDOM_PENALTY("mandom_penalty"),
            TRUE_INVISIBILITY("invis"),
            SyncDaze("sync_daze");

            public final String value;

            MESSAGES(String value)
            {
                this.value = value;
            }
        }

        @Override
        public void INVOKE(String message, @Nullable ServerPlayer sender, Object... vargs) {
            if (MainUtil.isClient()) {
                /**Try Power Packet*/
                ClientUtil.handleGeneralPackets(message,vargs);
            }
        }
    }
}

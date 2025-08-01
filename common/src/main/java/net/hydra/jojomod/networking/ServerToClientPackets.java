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
            SyncDaze("sync_daze"),
            SyncGuard("sync_guard"),
            UpdateBarrageClash("update_barrage_clash_s2c"),
            SendConfig("send_config"),
            PlaySound("play_sound"),
            StopSound("stop_sound"),
            Blip("blip"),
            SyncCooldown("sync_cooldown"),
            SyncCooldownMax("sync_cooldown_max"),
            SyncActivePower("sync_active_power"),
            SyncPowerInventory("sync_power_inventory"),
            IntPowerData("int_power_data"),
            IntToClient("generic_int_to_client");

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

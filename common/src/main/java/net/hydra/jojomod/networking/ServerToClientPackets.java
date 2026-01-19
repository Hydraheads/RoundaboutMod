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
            SyncActivePowerFate("sync_active_power_fate"),
            SyncActivePowerPowers("sync_active_power_powers"),
            SyncPowerInventory("sync_power_inventory"),
            IntPowerData("int_power_data"),
            IntToClient("generic_int_to_client"),
            SimpleByteToClient("simple_byte_to_client"),
            ByteBundleToClient("byte_bundle_to_client"),
            AddTSEntity("add_ts_entity"),
            RemoveTSEntity("remove_ts_entity"),
            AddPCEntity("add_pc_entity"),
            RemovePCEntity("remove_pc_entity"),
            ResumeTileEntityTS("resume_tile_ent_ts"),
            SendNewDynamicWorld("send_new_dynamic_world"),
            EjectPRunning("eject_p_running"),
            HeelExtend("heel_extend"),
            RefreshAllCooldowns("refresh_cooldowns"),
            AffirmAllCooldowns("affirm_cooldown_sync"),
            CreamUpdateTimer("cream_update_timer"),
            CreamUpdateTransformTimer("cream_update_transform_timer"),
            CreamUpdateTransformDirection("cream_update_transform_direction"),
            VampireMessage("vampire_message"),
            UpdateVampireData("update_vampire_data"),
            UpdateVampireData2("update_vampire_data_2"),
            UpdateVampireData3("update_vampire_data_3"),
            GunRecoil("gun_recoil"),
            SyncPossessor("sync_possessor"),
            ShatterIce("shatter_ice");

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

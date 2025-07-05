package net.hydra.jojomod.networking;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.networking.message.impl.IMessageEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ClientToServerPackets {
    public static class StandPowerPackets implements IMessageEvent {
        public enum MESSAGES {
            TryPower("try_power"),
            TryPosPower("try_pos_power"),
            TryBlockPosPower("try_block_pos_power"),
            TryHitResultPosPower("try_hit_result_pos_power"),
            TryIntPower("try_int_power");

            public final String value;

            MESSAGES(String value)
            {
                this.value = value;
            }
        }

        public StandUser basicChecks(ServerPlayer sender){
            if (sender == null)
                throw new RuntimeException("\"sender\" was null on a C2S packet!");
            return ((StandUser)sender);
        }
        @Override
        public void INVOKE(String message, @Nullable ServerPlayer sender, Object... vargs) {
            /**Try Power Packet*/
            if (message.equals(MESSAGES.TryPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                    byte b = (byte)vargs[0];
                    powers.roundabout$tryPower(b,true);
                });
            }
            /**Try Power Packet*/
            if (message.equals(MESSAGES.TryPosPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                        byte b = (byte)vargs[0];
                        Vector3f c = (Vector3f)vargs[1];
                        powers.roundabout$tryPosPower(b,true,new Vec3(c.x,c.y,c.z));
                });
            }
            /**Try Block Pos Power Packet*/
            if (message.equals(MESSAGES.TryBlockPosPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                        byte b = (byte)vargs[0];
                        BlockPos c = (BlockPos)vargs[1];
                        powers.roundabout$tryBlockPosPower(b,true, c);
                });
            }
            /**Try Block Pos Power Packet*/
            if (message.equals(MESSAGES.TryHitResultPosPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                    byte b = (byte)vargs[0];
                    BlockPos c = (BlockPos)vargs[1];
                    BlockHitResult d = (BlockHitResult) vargs[2];
                    powers.roundabout$tryBlockPosPower(b,true, c,d);
                });
            }
            /**Try Power Packet*/
            if (message.equals(MESSAGES.TryIntPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                        byte b = (byte)vargs[0];
                        int c = (int)vargs[1];
                        powers.roundabout$tryIntPower(b,true,c);
                });
            }
        }
    }
}

package net.hydra.jojomod.networking;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.advancement.criteria.ModCriteria;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.zetalasis.networking.message.impl.IMessageEvent;
import net.zetalasis.world.DynamicWorld;
import org.jetbrains.annotations.Nullable;

public class ClientToServerPackets {
    public static class StandPowerPackets implements IMessageEvent {
        public enum MESSAGES {
            TryPower("try_power"),
            TryPosPower("try_pos_power"),
            TryBlockPosPower("try_block_pos_power");

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
                    Roundabout.LOGGER.info("b+"+b);
                    powers.roundabout$tryPower(b,true);
                });
            }
            /**Try Power Packet*/
            if (message.equals(MESSAGES.TryPosPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                    for (Object v : vargs)
                    {
                        byte b = (byte)vargs[0];
                        powers.roundabout$tryPower(b,true);
                    }
                });
            }
            /**Try Power Packet*/
            if (message.equals(MESSAGES.TryBlockPosPower.value))
            {
                MinecraftServer server = sender.server;

                server.execute(()->{
                    StandUser powers = basicChecks(sender);
                    for (Object v : vargs)
                    {
                        byte b = (byte)vargs[0];
                        BlockPos c = (BlockPos)vargs[0];
                        powers.roundabout$tryPosPower(b,true, c);
                    }
                });
            }
        }
    }
}

package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePosPowerPacket {
        private final byte power;
        private final BlockPos blockPos;

        public ForgePosPowerPacket(byte power, BlockPos blockPos){
            this.power = power;
            this.blockPos = blockPos;
        }
        public ForgePosPowerPacket(FriendlyByteBuf buf){
            this.power = buf.readByte();
            this.blockPos = buf.readBlockPos();
        }
        public void toBytes(FriendlyByteBuf buf){
            buf.writeByte(power);
            buf.writeBlockPos(blockPos);
        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier){
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(()-> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    ServerLevel level = (ServerLevel) player.level();
                    ((StandUser) player).tryPosPower(power, true, blockPos);
                }
            });
            return true;
        }
}

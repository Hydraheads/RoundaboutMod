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
        private final BlockHitResult blockHit;

        public ForgePosPowerPacket(byte power, BlockPos blockPos, BlockHitResult blockhit){
            this.power = power;
            this.blockPos = blockPos;
            this.blockHit = blockhit;
        }
        public ForgePosPowerPacket(byte power, BlockPos blockPos){
            this.power = power;
            this.blockPos = blockPos;
        }
        public ForgePosPowerPacket(FriendlyByteBuf buf){
            this.power = buf.readByte();
            this.blockPos = buf.readBlockPos();
            try{
                this.blockHit = buf.readBlockHitResult();
            } catch (Exception ignored) {

            }
        }
        public void toBytes(FriendlyByteBuf buf){
            buf.writeByte(power);
            buf.writeBlockPos(blockPos);
            if(blockHit != null){
                buf.writeBlockHitResult(blockHit);
            }
        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier){
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(()-> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    ServerLevel level = (ServerLevel) player.level();
                    if(blockHit != null){
                        ((StandUser) player).roundabout$tryBlockPosPower(power, true, blockPos,blockHit);
                    } else {
                        ((StandUser) player).roundabout$tryBlockPosPower(power, true, blockPos);
                    }
                }
            });
            return true;
        }
}

package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeBundlePacket {

        private final byte cont;
    private final byte one;
    private final byte two;
    private final byte three;

        public ForgeBundlePacket(byte context,byte one, byte two, byte three){
            this.cont = context;
            this.one = one;
            this.two = two;
            this.three = three;
        }
        public ForgeBundlePacket(FriendlyByteBuf buf){
            this.cont = buf.readByte();
            this.one = buf.readByte();
            this.two = buf.readByte();
            this.three = buf.readByte();
        }
        public void toBytes(FriendlyByteBuf buf){
            buf.writeByte(cont);
            buf.writeByte(one);
            buf.writeByte(two);
            buf.writeByte(three);
        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier){
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(()-> {
                ClientUtil.handleBundlePacketS2C(cont,one,two,three);
            });
            return true;
        }

}

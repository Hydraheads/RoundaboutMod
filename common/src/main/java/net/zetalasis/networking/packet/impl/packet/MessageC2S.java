package net.zetalasis.networking.packet.impl.packet;

import net.hydra.jojomod.Roundabout;
import net.minecraft.network.FriendlyByteBuf;
import net.zetalasis.networking.message.api.ModMessageEvents;
import net.zetalasis.networking.message.impl.IMessageEvent;
import net.zetalasis.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.zetalasis.networking.packet.api.args.c2s.PacketArgsC2S;

import java.util.Arrays;

public class MessageC2S extends AbstractBaseC2SPacket {
    @Override
    public void handle(PacketArgsC2S args, FriendlyByteBuf buf) {
        Object[] vargs = args.vargs;

        if (vargs.length == 0 || !(vargs[0] instanceof String messageName)) {
            Roundabout.LOGGER.warn("Invalid message data received: {}", Arrays.toString(vargs));
            return;
        }

        for (IMessageEvent event : ModMessageEvents.REGISTRAR) {
            // Arrays.copyOfRange for removing the metadata element
            event.INVOKE(messageName, args.player, Arrays.copyOfRange(vargs, 1, vargs.length));
        }
    }
}
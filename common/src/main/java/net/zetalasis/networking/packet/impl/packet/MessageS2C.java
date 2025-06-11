package net.zetalasis.networking.packet.impl.packet;

import net.hydra.jojomod.Roundabout;
import net.minecraft.network.FriendlyByteBuf;
import net.zetalasis.networking.message.api.ModMessageEvents;
import net.zetalasis.networking.message.impl.IMessageEvent;
import net.zetalasis.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.zetalasis.networking.packet.api.args.s2c.PacketArgsS2C;

import java.util.Arrays;

public class MessageS2C extends AbstractBaseS2CPacket {
    @Override
    public void handle(PacketArgsS2C args, FriendlyByteBuf buf) {
        Object[] vargs = args.vargs;

        if (vargs.length == 0 || !(vargs[0] instanceof String messageName)) {
            Roundabout.LOGGER.warn("Invalid message data received: {}", Arrays.toString(vargs));
            return;
        }

        for (IMessageEvent event : ModMessageEvents.REGISTRAR) {
            // Arrays.copyOfRange for removing the metadata element
            event.INVOKE(messageName, null, Arrays.copyOfRange(vargs, 1, vargs.length));
        }
    }
}
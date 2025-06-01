package net.hydra.jojomod.networking.packet.api.args.c2s;

import org.jetbrains.annotations.Nullable;

/** Structure class for cross-loader C2S packets (as each loader passes diff. arguments to its packets) */
public class PacketArgsC2S {
    /** Arguments passed by Fabric. May be null if the packet originates from Forge. */
    public @Nullable FabricPacketArgsC2S fabricArgs;
    /** Arguments passed by Forge. May be null if the packet originates from Fabric. */
    public @Nullable ForgePacketArgsC2S forgeArgs;

    /** Varying arguments passed by packet
     * examples: entity id, dimension name */
    public Object[] vargs;
}
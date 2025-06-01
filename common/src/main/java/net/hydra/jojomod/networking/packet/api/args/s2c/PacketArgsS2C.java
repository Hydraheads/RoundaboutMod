package net.hydra.jojomod.networking.packet.api.args.s2c;

import org.jetbrains.annotations.Nullable;

/** Structure class for cross-loader S2C packets (as each loader passes diff. arguments to its packets) */
public class PacketArgsS2C {
    /** Arguments passed by Fabric. May be null if the packet originates from Forge. */
    public @Nullable FabricPacketArgsS2C fabricArgs;
    /** Arguments passed by Forge. May be null if the packet originates from Fabric. */
    public @Nullable ForgePacketArgsS2C forgeArgs;

    /** Varying arguments passed by packet
     * examples: entity id, dimension name */
    public Object[] vargs;
}
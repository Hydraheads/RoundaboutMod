package net.hydra.jojomod.networking;

import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.platform.Services;
import net.minecraft.client.multiplayer.chat.report.AbuseReportSender;

public class ModPacketHandler {
    /**A service loader that lets common code run forge and Fabric functions through
     * IPacketAccess interface. */
    public static final IPacketAccess PACKET_ACCESS = Services.load(IPacketAccess.class);
}

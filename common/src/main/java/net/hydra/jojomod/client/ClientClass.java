package net.hydra.jojomod.client;

import net.hydra.jojomod.platform.ClientServices;

public class ClientClass {
    public static void init(){
        ClientServices.PACKET_HELPER_CLIENT.registerPackets();
    }
}

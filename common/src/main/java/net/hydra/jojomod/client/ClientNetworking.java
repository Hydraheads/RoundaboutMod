package net.hydra.jojomod.client;


import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.Networking;

public class ClientNetworking {
    private static boolean isConnectedToDedicated = false;
    private static boolean displayNewConfigToast = false;
    private static boolean refreshScreen = false;

    public static Config getAppropriateConfig(){
        if (isConnectedToDedicated
                //|| Networking.isDedicated()
        ){
            return Config.getServerInstance();
        }
        return Config.getLocalInstance();
    }

    public static void initialize(String ser){
        isConnectedToDedicated = true;
        ConfigManager.deserializeConfig(ser);
    }
    public static void sendHandshake() {
        C2SPacketUtil.handshakePacket();
    }

    private static void requestConfigFromServer() {
        sendHandshake();
    }

    public static boolean isConnectedToDedicated() {
        return isConnectedToDedicated;
    }
}

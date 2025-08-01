package net.hydra.jojomod.util;

import net.hydra.jojomod.networking.ClientToServerPackets;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.networking.message.api.ModMessageEvents;
import org.joml.Vector3f;

/**it is okay to have client only classes here
 *  This is where we send packets from the client to the server :O
 *  See other class, ClientToServerPackets
 *  */
public class C2SPacketUtil {
    /**sending packets to the server*/
    public static void tryPowerPacket(byte packet){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TryPower.value,
                packet
        );
    }

    public static void tryIntPowerPacket(byte packet, int integer){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TryIntPower.value,
                packet,
                integer
        );
    }

    public static void intToServerPacket(byte packet, int integer){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.IntToServer.value,
                packet,
                integer
        );
    }

    public static void floatToServerPacket(byte packet, float ft){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.FloatToServer.value,
                packet,
                ft
        );
    }

    public static void byteToServerPacket(byte packet, byte value){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.ByteToServer.value,
                packet,
                value
        );
    }


    public static void tryTripleIntPacket(byte packet, int in1, int in2, int in3){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TryTripleIntPower.value,
                packet,
                in1,
                in2,
                in3
        );
    }

    public static void tryBlockPosPowerPacket(byte packet, BlockPos pos){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TryBlockPosPower.value,
                packet,
                pos
        );
    }
    public static void tryBlockPosPowerPacket(byte packet, BlockPos pos, HitResult hitResult){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TryHitResultPosPower.value,
                packet,
                pos,
                hitResult
        );
    }
    public static void tryPosPowerPacket(byte packet, Vec3 pos){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TryPosPower.value,
                packet,
                pos.toVector3f()
        );
    }

    public static void trySingleBytePacket(byte packet){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.SingleByteToServer.value,
                packet
        );
    }

    public static void timeStopHoveringPacket(boolean hover){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.TimeStopHovering.value,
                hover
        );
    }
    public static void glaiveHitPacket(int entityId, ItemStack glaiveStack){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.GlaiveHit.value,
                entityId,
                glaiveStack
        );
    }
    public static void standSummonPacket(){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.StandSummon.value
        );
    }

    public static void updatePilot(LivingEntity pilotStand){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.UpdatePilot.value,
                (float)pilotStand.getX(),
                (float)pilotStand.getY(),
                (float)pilotStand.getZ(),
                (float)pilotStand.getYRot(),
                (float)pilotStand.getXRot(),
                pilotStand.getId()
        );
    }

    public static void moveSyncPacket(byte forward, byte sideways){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.MoveSync.value,
                forward,
                sideways
        );
    }
    public static void standPunchPacket(int targetID, byte activePowerPhase){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.StandPunch.value,
                targetID,
                activePowerPhase
        );
    }
    public static void standBarrageHitPacket(int targetID, int attackTimeDuring){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.StandBarrageHit.value,
                targetID,
                attackTimeDuring
        );
    }
    /**During a clash, updates the progress of your clash bar in real time.*/
    public static void barrageClashUpdatePacket(float clashProgress, boolean clashDone){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.BarrageClashUpdate.value,
                clashProgress,
                clashDone
        );
    }

    /**The client asks the server for the config essentially.*/
    public static void handshakePacket(){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.Handshake.value
        );
    }

    /**Sending anything in the inventory to the server*/
    public static void inventoryToServerPacket(int slotNum, ItemStack stack, byte context){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.Inventory.value,
                slotNum,
                stack,
                context
        );
    }
    /**Generic Item packet management for the server*/
    public static void itemContextToServerPacket(byte context, ItemStack stack){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.ItemContext.value,
                context,
                stack
        );
    }


    /**Release right click to stop guarding*/
    public static void guardCancelPacket(){
        ModMessageEvents.sendToServer(
                ClientToServerPackets.StandPowerPackets.MESSAGES.GuardCancel.value
        );
    }
    /**reading packets from the server*/

}

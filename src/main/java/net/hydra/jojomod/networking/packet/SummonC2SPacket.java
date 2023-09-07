package net.hydra.jojomod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.IEntityDataSaver;
import net.hydra.jojomod.util.StandData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class SummonC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();

        //playsound ModSounds.SUMMON_SOUND
        //SoundEvents.ENTITY_GENERIC_DRINK

        boolean active;
        if (!StandData.isActive((IEntityDataSaver) player)) {
            world.playSound(null, player.getBlockPos(), ModSounds.SUMMON_SOUND_EVENT, SoundCategory.PLAYERS, 1F, 1F);
            //EntityType.COW.spawn((ServerWorld) player.getWorld(), player.getBlockPos(), SpawnReason.TRIGGERED);
            active=true;
        } else {
            active=false;
        }
        ((IEntityDataSaver) player).getPersistentData().putInt("guard",player.age+200);
        StandData.setActive((IEntityDataSaver) player,active);
        StandData.syncStandActive(active, (ServerPlayerEntity) player);
    }
}
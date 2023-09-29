package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.stand.NBTData;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

public class SummonPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();
        StandUserComponent userData = MyComponents.STAND_USER.get(player);

        //playsound ModSounds.SUMMON_SOUND
        //SoundEvents.ENTITY_GENERIC_DRINK

        boolean active;
        if (!NBTData.isActive((IEntityDataSaver) player)) {

            //world.getEntity
            StandEntity stand = ModEntities.THE_WORLD.create(world);
            if (stand != null) {
                Vec3d spos = stand.getStandOffsetVector(player);
                stand.updatePosition(spos.getX(), spos.getY(), spos.getZ());
                ((IEntityDataSaver) player).getPersistentData().putUuid("active_stand",stand.getUuid());

                world.spawnEntity(stand);
                stand.playSummonSound();

                userData.standMount(stand);
            }

            //ModEntities.THE_WORLD.spawn((ServerWorld) player.getWorld(), player.getBlockPos(), SpawnReason.TRIGGERED);
            active=true;
        } else {

            RoundaboutMod.LOGGER.info("Hi ");
            userData.setStand(null);
            RoundaboutMod.LOGGER.info("Hi 2");
            ((IEntityDataSaver) player).getPersistentData().remove("active_stand");
            active=false;
        }
        ((IEntityDataSaver) player).getPersistentData().putLong("guard",(player.getWorld().getTime()+200));
        NBTData.setActive((IEntityDataSaver) player,active);
        NBTData.syncModNbt((ServerPlayerEntity) player);
    }
}

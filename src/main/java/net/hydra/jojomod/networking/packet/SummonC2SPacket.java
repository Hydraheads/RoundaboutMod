package net.hydra.jojomod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.stand.StandData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

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
            //world.getEntity
            StandEntity stand = ModEntities.THE_WORLD.create(world);
            if (stand != null) {
                stand.updatePosition(player.getX(), player.getY(), player.getZ());
                stand.setOwnerUuid(player.getUuid());
                ((IEntityDataSaver) player).getPersistentData().putUuid("active_stand",stand.getUuid());
                world.spawnEntity(stand);
                stand.setOwnerID(player.getId());
                //((IStandUser) (PlayerEntity) player).startStandRiding(stand, true);
                //StandData.syncRidingID((ServerPlayerEntity) player,stand.getId());
               // player.startRiding(stand,true);
            }

            //ModEntities.THE_WORLD.spawn((ServerWorld) player.getWorld(), player.getBlockPos(), SpawnReason.TRIGGERED);
            active=true;
        } else {
            ((IEntityDataSaver) player).getPersistentData().remove("active_stand");
            active=false;
        }
        ((IEntityDataSaver) player).getPersistentData().putLong("guard",(player.getWorld().getTime()+200));
        StandData.setActive((IEntityDataSaver) player,active);
        StandData.syncStandActive((ServerPlayerEntity) player);
    }
}

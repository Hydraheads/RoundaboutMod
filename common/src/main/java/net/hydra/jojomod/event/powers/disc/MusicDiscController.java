package net.hydra.jojomod.event.powers.disc;

import net.hydra.jojomod.access.DiscBearer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.hydra.jojomod.sound.ModSounds;

import java.util.Map;
import java.util.WeakHashMap;

public final class MusicDiscController {
    private static final Map<LivingEntity, Item> PLAYING = new WeakHashMap<>();

    private MusicDiscController() {
    }

    public static boolean implant(ItemStack stack, LivingEntity target, LivingEntity thrower) {
        if (!(stack.getItem() instanceof RecordItem) || !(target instanceof Mob)
                || WhitesnakeDiscUtil.isDiscBlacklisted(target)) return false;
        if (target.level().isClientSide()) return true;
        DiscBearer bearer = (DiscBearer) target;
        if (!bearer.roundabout$getMusicDisc().isEmpty()) return false;
        ItemStack implanted = stack.copy();
        implanted.setCount(1);
        bearer.roundabout$setMusicDisc(implanted);
        target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_INSERT_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        start(target, implanted);
        return true;
    }

    public static void tick(LivingEntity bearerEntity) {
        if (bearerEntity.level().isClientSide()) return;
        ItemStack stack = ((DiscBearer) bearerEntity).roundabout$getMusicDisc();
        if (stack.isEmpty() || !(stack.getItem() instanceof RecordItem)) {
            PLAYING.remove(bearerEntity);
            return;
        }
        if (PLAYING.get(bearerEntity) != stack.getItem()) start(bearerEntity, stack);
    }

    public static void ejectOnDamage(LivingEntity bearerEntity) {
        if (bearerEntity.level().isClientSide()) return;
        DiscBearer bearer = (DiscBearer) bearerEntity;
        ItemStack stack = bearer.roundabout$getMusicDisc();
        if (stack.isEmpty()) return;
        stop(bearerEntity, stack);
        bearer.roundabout$setMusicDisc(ItemStack.EMPTY);
        PLAYING.remove(bearerEntity);
        bearerEntity.spawnAtLocation(stack.copy());
        bearerEntity.level().playSound(null, bearerEntity.blockPosition(), ModSounds.WHITESNAKE_DISC_EJECT_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static void start(LivingEntity entity, ItemStack stack) {
        if (!(stack.getItem() instanceof RecordItem record)) return;
        entity.level().playSound(null, entity, record.getSound(), SoundSource.RECORDS, 4.0F, 1.0F);
        PLAYING.put(entity, stack.getItem());
    }

    private static void stop(LivingEntity entity, ItemStack stack) {
        if (!(entity.level() instanceof ServerLevel server) || !(stack.getItem() instanceof RecordItem record)) return;
        SoundEvent sound = record.getSound();
        ResourceLocation soundId = BuiltInRegistries.SOUND_EVENT.getKey(sound);
        ClientboundStopSoundPacket packet = new ClientboundStopSoundPacket(soundId, SoundSource.RECORDS);
        server.players().forEach(player -> player.connection.send(packet));
    }
}

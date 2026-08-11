package net.hydra.jojomod.event.powers.disc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

public final class MemoryPersonality {
    public static final byte PASSIVE = 0;
    public static final byte NEUTRAL = 1;
    public static final byte HOSTILE = 2;
    public static final byte PLAYER = 3;
    public static final byte ZOMBIE = 4;
    public static final byte HORSE = 5;
    public static final byte CREEPER = 6;

    private MemoryPersonality() {
    }

    public static byte classify(LivingEntity entity) {
        if (entity instanceof Player) {
            return PLAYER;
        }
        if (entity instanceof Zombie) {
            return ZOMBIE;
        }
        if (entity instanceof Creeper) {
            return CREEPER;
        }
        if (entity instanceof AbstractHorse) {
            return HORSE;
        }
        if (entity instanceof Enemy) {
            return HOSTILE;
        }
        if (entity instanceof NeutralMob) {
            return NEUTRAL;
        }
        return PASSIVE;
    }

    public static String name(byte personality) {
        return switch (personality) {
            case NEUTRAL -> "Neutral";
            case HOSTILE -> "Hostile";
            case PLAYER -> "Player";
            case ZOMBIE -> "Zombie";
            case HORSE -> "Horse";
            case CREEPER -> "Creeper";
            default -> "Passive";
        };
    }
}

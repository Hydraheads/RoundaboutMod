package net.hydra.jojomod.event.powers.khnum;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

public final class KhnumMobDisguise {
    public static final byte NONE = 0;
    public static final byte ZOMBIE = 1;
    public static final byte SKELETON = 2;
    public static final byte CREEPER = 3;
    public static final byte SPIDER = 4;
    public static final byte VILLAGER = 5;
    public static final byte COD = 6;
    public static final byte SALMON = 7;
    public static final byte TROPICAL_FISH = 8;
    public static final byte PUFFERFISH = 9;

    private KhnumMobDisguise() { }

    public static byte chooseFor(Mob mob) {
        byte[] options;
        if (mob instanceof Drowned) options = new byte[]{COD, SALMON, TROPICAL_FISH, PUFFERFISH};
        else if (mob instanceof IronGolem) options = new byte[]{VILLAGER};
        else if (mob instanceof AbstractSkeleton) options = new byte[]{CREEPER, ZOMBIE};
        else if (mob instanceof Creeper) options = new byte[]{ZOMBIE, SPIDER};
        else if (mob instanceof Zombie) options = new byte[]{SPIDER, SKELETON, CREEPER, VILLAGER};
        else if (mob instanceof Spider) options = new byte[]{ZOMBIE, CREEPER};
        else return NONE;
        return options[mob.getRandom().nextInt(options.length)];
    }

    public static Mob create(byte disguise, Level level) {
        return switch (disguise) {
            case ZOMBIE -> EntityType.ZOMBIE.create(level);
            case SKELETON -> EntityType.SKELETON.create(level);
            case CREEPER -> EntityType.CREEPER.create(level);
            case SPIDER -> EntityType.SPIDER.create(level);
            case VILLAGER -> EntityType.VILLAGER.create(level);
            case COD -> EntityType.COD.create(level);
            case SALMON -> EntityType.SALMON.create(level);
            case TROPICAL_FISH -> EntityType.TROPICAL_FISH.create(level);
            case PUFFERFISH -> EntityType.PUFFERFISH.create(level);
            default -> null;
        };
    }
}

package net.hydra.jojomod.entity.navigation;

import net.hydra.jojomod.entity.visages.CloneEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ActiveCloneManager {

    public static final Set<WeakReference<CloneEntity>> ACTIVE_CLONES =
            ConcurrentHashMap.newKeySet();

    private ActiveCloneManager() {}

    public static void add(CloneEntity clone) {
        cleanup();
        ACTIVE_CLONES.add(new WeakReference<>(clone));
    }

    public static void remove(CloneEntity clone) {
        ACTIVE_CLONES.removeIf(ref -> {
            CloneEntity c = ref.get();
            return c == null || c == clone;
        });
    }

    private static void cleanup() {
        ACTIVE_CLONES.removeIf(ref -> {
            CloneEntity c = ref.get();
            return c == null || c.isRemoved() || !c.isAlive();
        });
    }

    @Nullable
    public static CloneEntity getNearest(Mob mob) {
        cleanup();
        if (ACTIVE_CLONES.isEmpty()){
            return null;
        }

        CloneEntity nearest = null;
        double bestDistance = Double.MAX_VALUE;

        for (WeakReference<CloneEntity> ref : ACTIVE_CLONES) {
            CloneEntity clone = ref.get();

            if (clone == null) continue;
            if (clone.isRemoved()) continue;
            if (clone.level() != mob.level()) continue;
            if (!clone.isAlive()) continue;

            double dist = mob.distanceToSqr(clone);

            if (dist < bestDistance) {
                bestDistance = dist;
                nearest = clone;
            }
        }

        return nearest;
    }
}

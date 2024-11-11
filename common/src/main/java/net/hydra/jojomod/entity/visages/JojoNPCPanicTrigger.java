package net.hydra.jojomod.entity.visages;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;

public class JojoNPCPanicTrigger extends Behavior<JojoNPC> {
        public JojoNPCPanicTrigger() {
            super(ImmutableMap.of());
        }

        protected boolean canStillUse(ServerLevel $$0, Villager $$1, long $$2) {
            return isHurt($$1) || hasHostile($$1);
        }

        protected void start(ServerLevel $$0, Villager $$1, long $$2) {
            if (isHurt($$1) || hasHostile($$1)) {
                Brain<?> $$3 = $$1.getBrain();
                if (!$$3.isActive(Activity.PANIC)) {
                    $$3.eraseMemory(MemoryModuleType.PATH);
                    $$3.eraseMemory(MemoryModuleType.WALK_TARGET);
                    $$3.eraseMemory(MemoryModuleType.LOOK_TARGET);
                    $$3.eraseMemory(MemoryModuleType.BREED_TARGET);
                    $$3.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
                }

                $$3.setActiveActivityIfPossible(Activity.PANIC);
            }
        }

        protected void tick(ServerLevel $$0, Villager $$1, long $$2) {
            if ($$2 % 100L == 0L) {
                $$1.spawnGolemIfNeeded($$0, $$2, 3);
            }
        }

        public static boolean hasHostile(LivingEntity $$0) {
            return $$0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
        }

        public static boolean isHurt(LivingEntity $$0) {
            return $$0.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
        }

}

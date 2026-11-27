package net.hydra.jojomod.access;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

/**This code lets me access the ZAbstractArrow Mixin externally, so I can call abstract arrow functions*/
public interface IAbstractArrowAccess {
    boolean sba$GetInGround();
    void sba$resetPiercedEntities();
    void sba$setLastState(BlockState last);
    void sba$SetInGround(boolean inGround);
    IntOpenHashSet rdbt$piercingIgnoreEntityIds();
    void rdbt$piercingIgnoreEntityIds(IntOpenHashSet mp);
    List<Entity> rdbt$piercedAndKilledEntities();
    void rdbt$piercedAndKilledEntities(List<Entity> ent);
    int rdbt$knockback();
    byte sba$GetPierceLevel();
    ItemStack sba$GetPickupItem();

    /**Manhattan Transfer Values*/
    boolean sba$GetIsManhattan();
    void sba$SetIsManhattan(boolean isManhattanProjectile);
    void sba$isArrowTooSlow(boolean mobAIForManhattan);
    float sba$getHattanDamage();
    void sba$setHattanDamage(float manhattanDmg);
    void sba$isMobAiArrow(boolean mobAIForManhattan);

    @Nullable
    EntityHitResult sba$FindHitEntity(Vec3 $$0, Vec3 $$1);

}

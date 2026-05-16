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
    boolean roundabout$GetInGround();
    void roundabout$resetPiercedEntities();
    void roundabout$setLastState(BlockState last);
    void roundabout$SetInGround(boolean inGround);
    IntOpenHashSet rdbt$piercingIgnoreEntityIds();
    void rdbt$piercingIgnoreEntityIds(IntOpenHashSet mp);
    List<Entity> rdbt$piercedAndKilledEntities();
    void rdbt$piercedAndKilledEntities(List<Entity> ent);
    int rdbt$knockback();
    byte roundabout$GetPierceLevel();
    ItemStack roundabout$GetPickupItem();

    /**Manhattan Transfer Values*/
    boolean roundabout$GetIsManhattan();
    void roundabout$SetIsManhattan(boolean isManhattanProjectile);
    float roundabout$getHattanDamage();
    void roundabout$setHattanDamage(float manhattanDmg);

    @Nullable
    EntityHitResult roundabout$FindHitEntity(Vec3 $$0, Vec3 $$1);

}

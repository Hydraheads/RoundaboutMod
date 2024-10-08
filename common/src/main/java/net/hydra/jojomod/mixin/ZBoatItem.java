package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBoatItemAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.swing.text.Position;
import java.util.function.Predicate;

@Mixin(BoatItem.class)
public class ZBoatItem implements IBoatItemAccess {

    @Shadow
    @Final
    private static Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    @Shadow
    @Final
    private Boat.Type type;
    @Shadow
    @Final
    private boolean hasChest;

    @Unique
    @Override
    public Boat.Type roundabout$getType(){
        return type;
    }

    @Unique
    @Override
    public Boat roundabout$getBoat(Level $$0, Vec3 $$1){

        return (Boat)(this.hasChest
                ? new ChestBoat($$0, $$1.x, $$1.y, $$1.z)
                : new Boat($$0, $$1.x, $$1.y, $$1.z));
    }

    @Shadow
    private Boat getBoat(Level $$0, HitResult $$1) {
        return (Boat)(this.hasChest
                ? new ChestBoat($$0, $$1.getLocation().x, $$1.getLocation().y, $$1.getLocation().z)
                : new Boat($$0, $$1.getLocation().x, $$1.getLocation().y, $$1.getLocation().z));
    }
}

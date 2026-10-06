    package net.hydra.jojomod.chocolatedisco;

    import net.hydra.jojomod.entity.stand.FollowingStandEntity;
    import net.minecraft.world.entity.EntityType;
    import net.minecraft.world.entity.Mob;
    import net.minecraft.world.level.Level;

    public class ChocolateDiscoEntity extends FollowingStandEntity {

        public ChocolateDiscoEntity(EntityType<? extends Mob> entityType, Level level) {
            super(entityType, level);
        }
    }
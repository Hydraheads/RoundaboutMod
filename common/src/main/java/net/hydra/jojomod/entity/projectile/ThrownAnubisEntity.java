package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandArrowItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownAnubisEntity extends ThrownObjectEntity {

    public ThrownAnubisEntity(LivingEntity living, Level $$1, ItemStack itemStack) {
        super(living, $$1, itemStack, false);
    }
    public ThrownAnubisEntity(EntityType<ThrownAnubisEntity> thrownAnubisEntityEntityType, Level level) {
        super(thrownAnubisEntityEntityType,level);
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if ($$0.getEntity() instanceof Cow C) {

            StandArrowItem.grantStand(ModItems.STAND_DISC_ANUBIS.getDefaultInstance(),C);
            C.hurt(ModDamageTypes.of(C.level(),ModDamageTypes.STAND),1);
            this.discard();
            return;
        } else {
            $$0.getEntity().hurt(ModDamageTypes.of($$0.getEntity().level(),ModDamageTypes.ANUBIS_SPIN,this.getOwner()),10);
        }
        super.onHitEntity($$0);
    }
}

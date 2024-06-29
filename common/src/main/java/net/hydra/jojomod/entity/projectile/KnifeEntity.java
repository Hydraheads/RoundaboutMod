package net.hydra.jojomod.entity.projectile;

import com.google.common.collect.Sets;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Set;

public class KnifeEntity extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(KnifeEntity.class, EntityDataSerializers.BOOLEAN);
    private final Set<MobEffectInstance> effects = Sets.newHashSet();

    private ItemStack knifeItem = new ItemStack(ModItems.KNIFE);

    public KnifeEntity(EntityType<? extends KnifeEntity> entity,  Level world) {
        super(entity, world);
    }
    public KnifeEntity(Level world, Player player, ItemStack itemStack) {
        super(ModEntities.THROWN_KNIFE, player, world);
        this.knifeItem = itemStack.copy();
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
    }

    public KnifeEntity(Level world, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.THROWN_KNIFE, p_36862_, p_36863_, p_36864_, world);
    }

    public KnifeEntity(Level world, LivingEntity entity) {
        super(ModEntities.THROWN_KNIFE, entity, world);
    }

    public void tick() {
        super.tick();
    }
    protected ItemStack getPickupItem() {
         return new ItemStack(ModItems.KNIFE);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }
    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }


    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }

}

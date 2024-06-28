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

}

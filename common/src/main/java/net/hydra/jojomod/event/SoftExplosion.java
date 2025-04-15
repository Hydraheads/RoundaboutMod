package net.hydra.jojomod.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SoftExplosion extends Explosion {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
    private static final int MAX_DROPS_PER_COMBINED_STACK = 16;
    private final boolean fire;
    private final BlockInteraction blockInteraction;
    private final RandomSource random;
    private final Level level;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity source;
    private final float radius;
    private final DamageSource damageSource;
    private final ExplosionDamageCalculator damageCalculator;
    private final ObjectArrayList<BlockPos> toBlow;
    private final Map<Player, Vec3> hitPlayers;
    public SoftExplosion(Level $$0, @javax.annotation.Nullable Entity $$1, double $$2, double $$3, double $$4, float $$5, List<BlockPos> $$6) {
        this($$0, $$1, $$2, $$3, $$4, $$5, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY, $$6);
    }

    public SoftExplosion(Level $$0, @javax.annotation.Nullable Entity $$1, double $$2, double $$3, double $$4, float $$5, boolean $$6, BlockInteraction $$7, List<BlockPos> $$8) {
        this($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
        this.toBlow.addAll($$8);
    }

    public SoftExplosion(Level $$0, @Nullable Entity $$1, double $$2, double $$3, double $$4, float $$5, boolean $$6, BlockInteraction $$7) {
        this($$0, $$1, (DamageSource)null, (ExplosionDamageCalculator)null, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    public SoftExplosion(Level $$0, @Nullable Entity $$1, @Nullable DamageSource $$2, @Nullable ExplosionDamageCalculator $$3, double $$4, double $$5, double $$6, float $$7, boolean $$8, BlockInteraction $$9) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
        this.random = RandomSource.create();
        this.toBlow = new ObjectArrayList();
        this.hitPlayers = Maps.newHashMap();
        this.level = $$0;
        this.source = $$1;
        this.radius = $$7;
        this.x = $$4;
        this.y = $$5;
        this.z = $$6;
        this.fire = $$8;
        this.blockInteraction = $$9;
        this.damageSource = $$2 == null ? $$0.damageSources().explosion(this) : $$2;
        this.damageCalculator = $$3 == null ? this.makeDamageCalculator($$1) : $$3;
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity $$0) {
        return (ExplosionDamageCalculator)($$0 == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator($$0));
    }
    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> $$0, ItemStack $$1, BlockPos $$2) {
        int $$3 = $$0.size();

        for(int $$4 = 0; $$4 < $$3; ++$$4) {
            Pair<ItemStack, BlockPos> $$5 = (Pair)$$0.get($$4);
            ItemStack $$6 = (ItemStack)$$5.getFirst();
            if (ItemEntity.areMergable($$6, $$1)) {
                ItemStack $$7 = ItemEntity.merge($$6, $$1, 16);
                $$0.set($$4, Pair.of($$7, (BlockPos)$$5.getSecond()));
                if ($$1.isEmpty()) {
                    return;
                }
            }
        }

        $$0.add(Pair.of($$1, $$2));
    }
    @Override
    public void explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
        Set<BlockPos> $$0 = Sets.newHashSet();

        int $$3;
        int $$4;
        for(int $$2 = 0; $$2 < 16; ++$$2) {
            for($$3 = 0; $$3 < 16; ++$$3) {
                for($$4 = 0; $$4 < 16; ++$$4) {
                    if ($$2 == 0 || $$2 == 15 || $$3 == 0 || $$3 == 15 || $$4 == 0 || $$4 == 15) {
                        double $$5 = (double)((float)$$2 / 15.0F * 2.0F - 1.0F);
                        double $$6 = (double)((float)$$3 / 15.0F * 2.0F - 1.0F);
                        double $$7 = (double)((float)$$4 / 15.0F * 2.0F - 1.0F);
                        double $$8 = Math.sqrt($$5 * $$5 + $$6 * $$6 + $$7 * $$7);
                        $$5 /= $$8;
                        $$6 /= $$8;
                        $$7 /= $$8;
                        float $$9 = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double $$10 = this.x;
                        double $$11 = this.y;
                        double $$12 = this.z;

                        for(float $$13 = 0.3F; $$9 > 0.0F; $$9 -= 0.22500001F) {
                            BlockPos $$14 = BlockPos.containing($$10, $$11, $$12);
                            BlockState $$15 = this.level.getBlockState($$14);
                            FluidState $$16 = this.level.getFluidState($$14);
                            if (!this.level.isInWorldBounds($$14)) {
                                break;
                            }

                            Optional<Float> $$17 = this.damageCalculator.getBlockExplosionResistance(this, this.level, $$14, $$15, $$16);
                            if ($$17.isPresent()) {
                                $$9 -= ((Float)$$17.get() + 0.3F) * 0.15F;
                            }

                            if ($$9 > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, $$14, $$15, $$9)) {
                                $$0.add($$14);
                            }

                            $$10 += $$5 * 0.30000001192092896;
                            $$11 += $$6 * 0.30000001192092896;
                            $$12 += $$7 * 0.30000001192092896;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll($$0);
    }
    @Override
    public void finalizeExplosion(boolean $$0) {

        boolean $$1 = this.interactsWithBlocks();

        if ($$1) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> $$2 = new ObjectArrayList();
            boolean $$3 = this.getIndirectSourceEntity() instanceof Player;
            Util.shuffle(this.toBlow, this.level.random);
            ObjectListIterator var5 = this.toBlow.iterator();

            while(var5.hasNext()) {
                BlockPos $$4 = (BlockPos)var5.next();
                BlockState $$5 = this.level.getBlockState($$4);
                net.minecraft.world.level.block.Block $$6 = $$5.getBlock();
                if (!$$5.isAir()) {
                    BlockPos $$7 = $$4.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    if ($$6.dropFromExplosion(this)) {
                        Level var11 = this.level;
                        if (var11 instanceof ServerLevel) {
                            ServerLevel $$8 = (ServerLevel)var11;
                            BlockEntity $$9 = $$5.hasBlockEntity() ? this.level.getBlockEntity($$4) : null;
                            LootParams.Builder $$10 = (new LootParams.Builder($$8)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf($$4)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, $$9).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                            if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                $$10.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                            }

                            $$5.spawnAfterBreak($$8, $$4, ItemStack.EMPTY, $$3);
                            $$5.getDrops($$10).forEach(($$2x) -> {
                                addBlockDrops($$2, $$2x, $$7);
                            });
                        }
                    }

                    this.level.setBlock($$4, Blocks.AIR.defaultBlockState(), 3);
                    $$6.wasExploded(this.level, $$4, this);
                    this.level.getProfiler().pop();
                }
            }

            var5 = $$2.iterator();

            while(var5.hasNext()) {
                Pair<ItemStack, BlockPos> $$11 = (Pair)var5.next();
                net.minecraft.world.level.block.Block.popResource(this.level, (BlockPos)$$11.getSecond(), (ItemStack)$$11.getFirst());
            }
        }

        if (this.fire) {
            ObjectListIterator var13 = this.toBlow.iterator();

            while(var13.hasNext()) {
                BlockPos $$12 = (BlockPos)var13.next();
                if (this.random.nextInt(3) == 0 && this.level.getBlockState($$12).isAir() && this.level.getBlockState($$12.below()).isSolidRender(this.level, $$12.below())) {
                    this.level.setBlockAndUpdate($$12, BaseFireBlock.getState(this.level, $$12));
                }
            }
        }
    }
}

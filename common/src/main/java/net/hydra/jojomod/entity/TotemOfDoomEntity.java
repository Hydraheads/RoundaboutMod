package net.hydra.jojomod.entity;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TotemOfDoomEntity extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK =
            SynchedEntityData.defineId(TotemOfDoomEntity.class, EntityDataSerializers.ITEM_STACK);

    private int life = 0;
    private static final int RADIUS = 30; // radius of the depression square
    private static final int MAX_LIFE = 200; // change this later, 10 seconds is only for testing

    public TotemOfDoomEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    // overload for the item constructor just so i can set pos in the item without calling setPos there
    // basically, what the eye of ender does
    public TotemOfDoomEntity(Level level, double x, double y, double z) {
        this(ModEntities.TOTEM_OF_DOOM, level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public void setItem(ItemStack stack) {
        this.getEntityData().set(DATA_ITEM_STACK, stack.copyWithCount(1));
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = this.getEntityData().get(DATA_ITEM_STACK);
        return item.isEmpty() ? new ItemStack(ModItems.TOTEM_OF_DOOM) : item;
    }

    /*
    Drew like a dark, messed up version of the totem of undying particles haha. Just a glimpse into my dark reality.
    A full stare into my twisted perspective would make most simply go insane lmao
     */
    public static final ParticleOptions[] DOOM_PALETTE = new ParticleOptions[] {
            new DustParticleOptions(Vec3.fromRGB24(0xE01613).toVector3f(), 1.0F), // red like eyes
            new DustParticleOptions(Vec3.fromRGB24(0x7B1FA2).toVector3f(), 1.0F), // purple
            new DustParticleOptions(Vec3.fromRGB24(0x2A1B19).toVector3f(), 1.0F)  // black-ish
    };

    @Override
    public void tick() {
        super.tick();
        // keeps it stationary
        this.setDeltaMovement(Vec3.ZERO);

        // bounding box that gives people depression
        if (!this.level().isClientSide) {
            //this is for the box particle
            drawMagicSymbol(this.level(), this.position());
            //this is for the constant particles it emits
            ParticleOptions particle = DOOM_PALETTE[level().random.nextInt(DOOM_PALETTE.length)];
            if (this.tickCount % 1 == 0) {
                ((ServerLevel) level()).sendParticles(
                        particle,
                        this.getX(), this.getY() + 0.1, this.getZ(),
                        1, 0.1, 0.1, 0.1, 0.02
                );
            }
            AABB box = this.getBoundingBox().inflate(RADIUS, RADIUS, RADIUS);
            List<Entity> entities = this.level().getEntities(null, box);
            for (Entity M : entities) {
                if (M instanceof LivingEntity LV) {
                    LV.addEffect(new MobEffectInstance(ModEffects.DOOMED, 10, 0, false, true));
                    // will eventually add the code here that shoots down pearls and stuff
                }
            }
        }

        // timer before despawning
        this.life++;
        if (this.life >= MAX_LIFE) {
            if (!this.level().isClientSide) {
                this.discard();
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Item", 10)) {
            this.setItem(ItemStack.of(tag.getCompound("Item")));
        }
        this.life = tag.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        ItemStack item = this.getEntityData().get(DATA_ITEM_STACK);
        if (!item.isEmpty()) {
            tag.put("Item", item.save(new CompoundTag()));
        }
        tag.putInt("Life", this.life);
    }

    // for making the square that tells where the box of the move is
    public static final Vector3f OCCULT_PARTICLE_COLOR = Vec3.fromRGB24(0x2A1B19).toVector3f();
    public static void drawLine(Level level, Vec3 start, Vec3 end, int points, ParticleOptions particle) {
        for (int i = 0; i <= points; i++) {
            double t = (double) i / points;

            double x = Mth.lerp(t, start.x, end.x);
            double y = Mth.lerp(t, start.y, end.y);
            double z = Mth.lerp(t, start.z, end.z);

            ((ServerLevel) level).sendParticles(particle,
                    x,y,z,
                    0, 0, 0, 0, 0);
        }
    }

    public static void drawSquare(Level level, Vec3 a, Vec3 b, Vec3 c, Vec3 d, int points, ParticleOptions particle) {
        drawLine(level, a, b, points, particle);
        drawLine(level, b, c, points, particle);
        drawLine(level, c, d, points, particle);
        drawLine(level, d, a, points, particle);
    }

    public static void drawMagicSymbol(Level level, Vec3 center) {
        ParticleOptions particle = new DustParticleOptions(OCCULT_PARTICLE_COLOR, 1.0f);

        // Triangle points (equilateral)
        Vec3 p1 = new Vec3(center.x + RADIUS, center.y, center.z + RADIUS);
        Vec3 p2 = new Vec3(center.x + RADIUS, center.y, center.z - RADIUS);
        Vec3 p3 = new Vec3(center.x - RADIUS, center.y, center.z - RADIUS);
        Vec3 p4 = new Vec3(center.x - RADIUS, center.y, center.z + RADIUS);

        drawSquare(level, p1, p2, p3, p4, 70, particle);
    }
}
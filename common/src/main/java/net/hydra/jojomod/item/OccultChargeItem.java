package net.hydra.jojomod.item;

import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class OccultChargeItem  extends Item {
    public OccultChargeItem(Item.Properties $$0) {
        super($$0);
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$0.playSound((Player)null, $$1.getX(), $$1.getY(), $$1.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / ($$0.getRandom().nextFloat() * 0.4F + 0.8F));
        $$1.getCooldowns().addCooldown(this, 20);
        if (!$$0.isClientSide) {
            drawMagicSymbol($$0,$$1.position());

        }

        $$1.awardStat(Stats.ITEM_USED.get(this));
        if (!$$1.getAbilities().instabuild) {
            $$3.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide());
    }

    public static void drawCircle(Level level, Vec3 center, double radius, int points, ParticleOptions particle) {
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;

            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            double y = center.y;
            ((ServerLevel) level).sendParticles(particle,
                    x,y,z,
                    0, 0, 0, 0, 0);
        }
    }

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

    public static void drawTriangle(Level level, Vec3 a, Vec3 b, Vec3 c, int points, ParticleOptions particle) {
        drawLine(level, a, b, points, particle);
        drawLine(level, b, c, points, particle);
        drawLine(level, c, a, points, particle);
    }
    public static final Vector3f OCCULT_PARTICLE_COLOR = Vec3.fromRGB24(0xE667BE).toVector3f();
    public static void drawMagicSymbol(Level level, Vec3 center) {
        ParticleOptions particle = new DustParticleOptions(OCCULT_PARTICLE_COLOR, 1.0f);;

        // Outer circle
        drawCircle(level, center, 3.0, 90, particle);

        // Inner circle
        drawCircle(level, center, 2.5, 70, particle);

        // Triangle points (equilateral)
        double r = 2.2;
        Vec3 p1 = new Vec3(center.x, center.y, center.z + r);
        Vec3 p2 = new Vec3(center.x + r * Math.cos(Math.toRadians(210)), center.y, center.z + r * Math.sin(Math.toRadians(210)));
        Vec3 p3 = new Vec3(center.x + r * Math.cos(Math.toRadians(330)), center.y, center.z + r * Math.sin(Math.toRadians(330)));

        drawTriangle(level, p1, p2, p3, 30, particle);
    }
}

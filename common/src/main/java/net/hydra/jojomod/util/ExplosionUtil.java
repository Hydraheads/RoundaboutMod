package net.hydra.jojomod.util;

import java.util.ArrayList;
import java.util.List;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.joml.Vector3f;

import com.google.common.collect.Lists;

//import net.hydra.jojomod.client.ClientNetworking;
//import net.hydra.jojomod.event.ModParticles;
//import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExplosionUtil {
	
	//private static ArrayList<String> blowableBlocksBlackList = Lists.newArrayList("minecraft:bedrock", "minecraft:obsidian");
	
	public static boolean isBlockBlackListed(BlockState bs) {
		ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(bs.getBlock());

		return (MainUtil.standBlockExplosionBlacklist != null && !MainUtil.standBlockExplosionBlacklist.isEmpty() && rl != null && MainUtil.standBlockExplosionBlacklist.contains(rl.toString())) && !bs.isAir();
	}
	public static void explodeEffects(Vec3 pos, Level level, SimpleParticleType particle, float range) {
		explodeEffects(pos, level, particle, new Vec3(range, range+0.3f, range), 18);

	}

    public static void explodeEffects(Vec3 pos, Level level, SimpleParticleType particle, float range, int amount) {
		explodeEffects(pos, level, particle, new Vec3(range, range+0.3f, range), amount);
	}

	public static void explodeEffects(Vec3 pos, Level level, SimpleParticleType particle, Vec3 range, int amount) {

    	((ServerLevel) level).sendParticles(particle,
                pos.x,
                pos.y+1.0f,
                pos.z,
                amount, range.x, range.y, range.z, 1.0);
                //amount, range, range+0.3f, range, 1.0);

    	((ServerLevel) level).sendParticles(new DustParticleOptions(new Vector3f(0.02F, 0.02F, 0.04F), 2f),
    			pos.x,
                pos.y+1.0f,
                pos.z,
                (int)Math.floor(amount*0.66), range.x, range.y+0.6, range.z, 0.001);
                //(int)Math.floor(amount*0.66), range, range+1.2f, range, 0.001);

    }

	public static void explosionHurtWithMulti(Vec3 pos, DamageSource dmgSource, Level level, float damage, float knockBack, float range, float mobMult, float playerMult) {
		explosionHurtBaseWithMulti(false, pos, dmgSource, level, damage, knockBack, range, mobMult, playerMult);
	}

	public static void explosionHurtSneakyWithMulti(Vec3 pos, DamageSource dmgSource, Level level, float damage, float knockBack, float range, float mobMult, float playerMult) {
		explosionHurtBaseWithMulti(true, pos, dmgSource, level, damage, knockBack, range, mobMult, playerMult);
	}

	public static void explosionHurt(Vec3 pos, DamageSource dmgSource, Level level, float damage, float knockBack, float range) {
		explosionHurtBase(false, pos, dmgSource, level, damage, knockBack, range);
	}

	public static void explosionHurtSneaky(Vec3 pos, DamageSource dmgSource, Level level, float damage, float knockBack, float range) {
		explosionHurtBase(true, pos, dmgSource, level, damage, knockBack, range);
	}

	public static void explosionHurtBase(Boolean sneaky, Vec3 pos, DamageSource dmgSource, Level level, float damage, float knockBack, float range) {
		explosionHurtBaseWithMulti(sneaky, pos, dmgSource, level, damage, knockBack, range, 1.0f, 1.0f);
    }

	public static void explosionHurtBaseWithMulti(Boolean sneaky, Vec3 pos, DamageSource dmgSource, Level level, float damage, float knockBack, float range, float mobMult, float playerMult) {
		List<Entity> damages = MainUtil.genHitbox(level, pos.x(), pos.y(), pos.z(), range, range, range);

		Entity causer = dmgSource.getEntity();

		DamageSource notSeenDamage =  ModDamageTypes.of(level, DamageTypes.EXPLOSION, null);

		for(int j = 0;j<damages.size();j++) {
			Entity entity = damages.get(j);
			if (entity instanceof StandEntity || !entity.isAttackable()) {
				continue;
			}
			if (entity instanceof Player PL && (PL.isCreative() || PL.isSpectator())) {
				continue;
			}

			double dist = entity.distanceToSqr(pos);
			float percUnhand = ((float)dist/ (range * range * range));
			float perc = 1.0f - (percUnhand*0.75f);
			float percKnockback = 1.0f - (percUnhand*0.5f);

			boolean hasSeen = true;

			if (sneaky && entity instanceof LivingEntity LE && causer != null) {
				hasSeen = (LE.hasLineOfSight(causer));
			}

			if (hasSeen || MainUtil.isBossMob(entity)) {
				if (MainUtil.getReducedDamage(entity)) {
					entity.hurt(dmgSource, perc * damage * playerMult);
				}else {
					entity.hurt(dmgSource, perc * damage * mobMult);
				}
			} else {
				if (MainUtil.getReducedDamage(entity)) {
					entity.hurt(notSeenDamage, perc * damage * playerMult);
				}else {
					entity.hurt(notSeenDamage, perc * damage * mobMult);
				}
			}


			Vec3 knockbackUnhand = new Vec3(entity.getX() - pos.x(), entity.getY() - pos.y(), entity.getZ() - pos.z());
			Vec3 knockback = knockbackUnhand.normalize().scale(Math.min(knockBack, percKnockback*knockBack));

			MainUtil.takeLiteralUnresistableKnockbackWithY(entity, knockback.x, knockback.y, knockback.z);
		}
	}

	public static void explodeBlocks(BlockPos location, Level level, Float range) {
		explodeBlocksBase(location, level, range, false);
	}

	public static void explodeBlocksIgnoreOres(BlockPos location, Level level, Float range) {
		explodeBlocksBase(location, level, range, true);
	}

	public static void explodeBlocksBase(BlockPos location, Level level, Float range, boolean ignoreOres) {
		Vec3 center = new Vec3(location.getX(), location.getY(), location.getZ());

		int intSize = (int) Math.floor(range);

		double explosionDistanceMax = Math.pow(range + 0.5, 2);

		for (BlockPos pos : BlockPos.betweenClosed(location.offset(intSize, intSize, intSize), location.offset(-intSize, -intSize, -intSize))) {
			BlockState info = level.getBlockState(pos);
			if (isBlockBlackListed(info) || (MainUtil.confirmIsOre(info) && ignoreOres)
					|| info.isAir() || info.is(Blocks.BARRIER) || info.is(Blocks.BEDROCK)) {
				continue;
			}

			// Simulate natural explosions
			Double explosionDistance = explosionDistanceMax + ((double) level.getRandom().nextIntBetweenInclusive(-intSize * 2, intSize * 2) / 7.5);

			Double dist2 = center.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());

			if (dist2 <= explosionDistance) {
				boolean shouldDrop = !info.requiresCorrectToolForDrops();
				level.destroyBlock(pos, shouldDrop);
			}
		}
	}

}
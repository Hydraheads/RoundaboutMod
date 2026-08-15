package net.hydra.jojomod.mixin.pearl_jam;

import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.mixin.WorldTickServer;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(PotionItem.class)
public class EnergizedWaterMixin {
    @Inject(method = "appendHoverText", at = @At(value = "HEAD"), cancellable = true)
    public void appendWaterLore(ItemStack $$0, Level $$1, List<Component> $$2, TooltipFlag $$3, CallbackInfo ci) {
        if ($$0.hasTag()) {
            if ($$0.getTag().contains("pearljamfood")) {
                if (MainUtil.foodCuresThat.containsKey($$0.getItem()) || MainUtil.foodAddsThat.containsKey($$0.getItem()) || MainUtil.specialFoodRemoves.containsKey($$0.getItem())) {
                    if (MainUtil.foodCuresThat.containsKey($$0.getItem())) {
                        List<MobEffect> effects = MainUtil.foodCuresThat.get($$0.getItem());
                        for (MobEffect effect : effects) {
                            $$2.add(Component.translatable("text.roundabout.pearl_jam.cure_lore", Component.translatable(effect.getDescriptionId()).withStyle(ChatFormatting.GOLD)));
                        }
                    }
                    if (MainUtil.foodAddsThat.containsKey($$0.getItem())) {
                        List<MobEffect> effects = MainUtil.foodAddsThat.get($$0.getItem());
                        for (MobEffect effect : effects) {
                            $$2.add(Component.translatable("text.roundabout.pearl_jam.add_lore", Component.translatable(effect.getDescriptionId()).withStyle(ChatFormatting.GOLD)));
                        }
                    }
                    if (MainUtil.specialFoodRemoves.containsKey($$0.getItem())) {
                        List<String> conditions = MainUtil.specialFoodRemoves.get($$0.getItem());
                        for (String condition : conditions) {
                            $$2.add(Component.translatable("text.roundabout.pearl_jam.remove_lore", Component.translatable("text.roundabout.condition.".concat(condition)).withStyle(ChatFormatting.GOLD)));
                        }
                    }
                } else {
                    $$2.add(Component.translatable("text.roundabout.pearl_jam.cure_lore", Component.translatable(MainUtil.pearlJamFallback.getDescriptionId()).withStyle(ChatFormatting.GOLD)));
                }
                if ($$0.getItem().getName($$0).getString().equals("Water Bottle")){ci.cancel();}
            }
        }

    }
    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void energizedWaterEffect(ItemStack $$0, Level $$1, LivingEntity $$2, CallbackInfoReturnable<ItemStack> cir){
        if ($$0.hasTag()) {
            if ($$0.getOrCreateTag().contains("pearljamfood")) {
                Random random = new Random();
                Pair<ParticleOptions, String> pair;
                if (MainUtil.foodParticles.containsKey($$0.getItem())) {
                    pair = MainUtil.foodParticles.get($$0.getItem());
                } else {
                    pair = MainUtil.foodParticleFallback;
                }
                if (pair.getSecond().equals("trunk")) {
                    Vec3 trunk_pos = new Vec3(
                            $$2.position().x + $$2.getLookAngle().x * 0.5,
                            $$2.position().y + $$2.getBbHeight() / 2 + $$2.getLookAngle().y * 0.5,
                            $$2.position().z + $$2.getLookAngle().z * 0.5
                    );
                    ((ServerLevel) $$1).sendParticles(pair.getFirst(), trunk_pos.x, trunk_pos.y, trunk_pos.z, 20, 0.2, 0.2, 0.2, 0.3);
                }

                if (pair.getSecond().equals("face")) {
                    Vec3 forward = $$2.getLookAngle().normalize();
                    Vec3 side = forward.cross(new Vec3(0, 1, 0)).normalize();
                    Vec3 vertical = forward.cross(side).normalize();
                    Vec3 face_pos = $$2.getEyePosition().add(forward.scale(0.2));

                    for (int i = 0; i < 2; i++) {
                        Vec3 spawn_pos = face_pos.add(side.scale(random.nextDouble(-0.12, 0.12))).add(vertical.scale(random.nextDouble(-0.05, 0.05)));
                        double spread_max = Math.tan(Math.toRadians(25));
                        Vec3 spread = forward.add(side.scale(random.nextDouble(-spread_max, spread_max))).add(vertical.scale(random.nextDouble(-spread_max, spread_max))).normalize();
                        double speed = 0.3 * random.nextDouble(0.7, 1.3);
                        ((ServerLevel) $$1).sendParticles(pair.getFirst(), spawn_pos.x, spawn_pos.y, spawn_pos.z, 0, spread.x * speed, spread.y * speed, spread.z * speed, 0);

                    }

                }

            /*
                Vec3 funnyVec = new Vec3(0,($$2.getBbHeight()*0.65),0);
                Direction gd = ((IGravityEntity)$$2).roundabout$getGravityDirection();
                if (gd != Direction.DOWN){
                    funnyVec = RotationUtil.vecPlayerToWorld(funnyVec,gd);
                }
                Vec3 pos = new Vec3(
                        $$2.getRandomX(1)+funnyVec.x,
                        $$2.getY((2.0 * Math.random() - 1.0) *  0.33)+funnyVec.y,
                        $$2.getRandomZ(1)+funnyVec.z);
                ((ServerLevel)$$1).sendParticles(pair.getFirst(), pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 1);
             */

                if (pair.getSecond().equals("eyes")) {
                    Vec3 offset = $$2.getLookAngle().cross(new Vec3(0, 1, 0)).normalize().scale(0.15);
                    for (int i = 0; i < 5; i++) {
                        double rand = random.nextDouble(0.1, 0.5);
                        ((ServerLevel) $$1).sendParticles(pair.getFirst(), $$2.getEyePosition().x + $$2.getLookAngle().x * 0.2 + offset.x, $$2.getEyePosition().y - 0.1 - i * rand + $$2.getLookAngle().y * 0.2 + offset.y, $$2.getEyePosition().z + $$2.getLookAngle().z * 0.2 + offset.z, 1, 0, 0, 0, 0.3);
                        ((ServerLevel) $$1).sendParticles(pair.getFirst(), $$2.getEyePosition().x + $$2.getLookAngle().x * 0.2 + offset.scale(-1).x, $$2.getEyePosition().y - 0.2 - i * rand + $$2.getLookAngle().y * 0.2 + offset.scale(-1).y, $$2.getEyePosition().z + $$2.getLookAngle().z * 0.2 + offset.scale(-1).z, 1, 0, 0, 0, 0.05);
                    }
                }
                if (MainUtil.foodCuresThat.containsKey($$0.getItem()) || MainUtil.foodAddsThat.containsKey($$0.getItem()) || MainUtil.specialFoodRemoves.containsKey($$0.getItem())) {
                    if (MainUtil.foodCuresThat.containsKey($$0.getItem())) {
                        List<MobEffect> effects = MainUtil.foodCuresThat.get($$0.getItem());
                        for (MobEffect effect : effects) {
                            $$2.removeEffect(effect);
                        }
                    }
                    if (MainUtil.foodAddsThat.containsKey($$0.getItem())) {
                        List<MobEffect> effects = MainUtil.foodAddsThat.get($$0.getItem());
                        for (MobEffect effect : effects) {
                            MobEffectInstance effectInstance = new MobEffectInstance(effect, 200);
                            $$2.addEffect(effectInstance);
                        }
                    }
                    if (MainUtil.specialFoodRemoves.containsKey($$0.getItem())) {
                        List<String> conditions = MainUtil.specialFoodRemoves.get($$0.getItem());
                        for (String condition : conditions) {
                            if (condition.equals("insomnia")) {
                                if ($$2 instanceof ServerPlayer serverPlayer) {
                                    Player player = (Player) $$2;
                                    player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
                                }
                            }
                            if (condition.equals("fire")) {
                                Player player = (Player) $$2;
                                player.clearFire();
                            }
                            if (condition.equals("gasoline")) {
                                StandUser standUser = (StandUser) $$2;
                                standUser.roundabout$setGasolineTime(0);
                            }
                        }
                    }
                } else {
                    $$2.removeEffect(MainUtil.pearlJamFallback);
                }
            }
        }
    }
}

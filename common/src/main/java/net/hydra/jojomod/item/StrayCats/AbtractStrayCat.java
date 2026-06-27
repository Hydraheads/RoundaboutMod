package net.hydra.jojomod.item.StrayCats;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.entity.projectile.StrayCatAirBubble;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

abstract public class AbtractStrayCat extends Item {
    public AbtractStrayCat(Properties $$0) { super($$0); }

    @Override public int getUseDuration(ItemStack $$0) {
        return 2;
    }

    @Override public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    private static final float SPEED = 0.7f;

    @Override
    public ItemStack finishUsingItem(ItemStack $$0, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player P) {

            if (!level.isClientSide) {
                P.getCooldowns().addCooldown($$0.getItem(),13);

                StrayCatAirBubble bubble = ModEntities.STRAY_CAT_AIRBUBBLE.create(level);
                if (bubble != null) {
                    bubble.setSped(SPEED);
                    bubble.setOwner(P);



                    Vec3 addToPosition = new Vec3(0, P.getEyeHeight() * 0.8F, 0);
                    Direction direction = ((IGravityEntity) P).roundabout$getGravityDirection();
                    if (direction != Direction.DOWN) {
                        addToPosition = RotationUtil.vecPlayerToWorld(addToPosition, direction);
                    }
                    Vec3 pos = P.getPosition(1).add(addToPosition.x, addToPosition.y, addToPosition.z).add(P.getForward().scale(P.getBbWidth() * 1));
                    bubble.setPos(pos.x(), pos.y(), pos.z());
                    bubble.shootFromRotationDeltaAgnostic(P, P.getXRot(), P.getYRot(), 1.0F, SPEED, 0);

                    level.addFreshEntity(bubble);
                }
            }
        }
        return $$0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.fail($$3);
    }

    public float getCurrentPredicateValue() {
        return 0.0f;
    }

}

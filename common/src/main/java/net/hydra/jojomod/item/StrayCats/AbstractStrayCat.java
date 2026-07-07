package net.hydra.jojomod.item.StrayCats;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.projectile.StrayCatAirBubble;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

abstract public class AbstractStrayCat extends Item {
    public AbstractStrayCat(Properties $$0) { super($$0); }

    public static final String NAME_TAG = "name";
    public static final String OWNER_UUID_TAG = "owner";
    public static final String SKIN_TAG = "skin";

    private static final float SPEED = 0.4f;
    public byte getBubbleSkin() { return 0; }

    static public void saveStrayCatEntityInfo(ItemStack stack, StrayCatEntity stray) {
        CompoundTag tag = stack.getOrCreateTag();
        //tag.putString(NAME_TAG, stray.get);

        if (stray.isTame() && stray.getOwnerUUID() != null) {
            tag.putUUID(OWNER_UUID_TAG, stray.getOwnerUUID());
            tag.putByte(SKIN_TAG, stray.getBreed());
        }

        stack.setTag(tag);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack $$0, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player P) {

            if (!level.isClientSide && !isSleeping(level)) {
                P.getCooldowns().addCooldown($$0.getItem(),13);

                StrayCatAirBubble bubble = ModEntities.STRAY_CAT_AIRBUBBLE.create(level);
                if (bubble != null) {
                    bubble.setSped(SPEED);
                    bubble.setOwner(P);
                    bubble.setSkin(this.getBubbleSkin());
                    bubble.setFollowOwnerView(true);

                    Vec3 addToPosition = new Vec3(0, P.getEyeHeight() * 0.85f, 0);
                    Direction direction = ((IGravityEntity) P).roundabout$getGravityDirection();
                    if (direction != Direction.DOWN) {
                        addToPosition = RotationUtil.vecPlayerToWorld(addToPosition, direction);
                    }
                    Vec3 pos = P.getPosition(1).add(addToPosition.x, addToPosition.y, addToPosition.z).add(P.getForward().scale(P.getBbWidth() * 1));
                    bubble.setPos(pos.x(), pos.y(), pos.z());
                    bubble.shootFromRotationDeltaAgnostic(P, P.getXRot(), P.getYRot(), 1.0F, SPEED, 0);
                    //bubble.shootFromRotation(P, P.getXRot(), P.getYRot(), -0.5F, SPEED, 0.00f);

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

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        BlockPos abovePos = blockPos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!aboveState.isAir()) return InteractionResult.FAIL;

        if (!level.isClientSide) {
            Vec3 spawnPos = Vec3.atCenterOf(abovePos);

            StrayCatEntity stray = ModEntities.STRAY_CAT.create(level);
            if (stray != null) {
                ItemStack item = context.getItemInHand();
                CompoundTag tag = item.getOrCreateTag();
                if (tag.contains(OWNER_UUID_TAG)) {
                    stray.setOwnerUUID(tag.getUUID(OWNER_UUID_TAG));
                    stray.setTame(true);
                }
                if (tag.contains(SKIN_TAG)) {
                    stray.setBreed(tag.getByte(SKIN_TAG));
                }

                stray.setPotted(true);

                stray.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), 0.0F);
                level.addFreshEntity(stray);

                if (!player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    static boolean isSleeping(Level level) {
        long dayTime = level.getDayTime() % 24000;

        return dayTime >= 13000 && dayTime <= 23750;
    }

    public float getCurrentPredicateValue(Level level) {
        if (level != null) {
            if (isSleeping(level)) {
                return 0.2f;
            }
        }
        return 0.0f;
    }

    @Override public int getUseDuration(ItemStack $$0) {
        return 1;
    }
    @Override public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.NONE;
    }

}

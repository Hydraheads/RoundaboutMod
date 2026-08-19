package net.hydra.jojomod.item;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.entity.projectile.StrayCatAirBubble;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class StrayCatItem extends Item {
    public final byte breed;

    public StrayCatItem(Properties $$0) {
        super($$0);
        this.breed = 0;
    }

    public StrayCatItem(Properties $$0, byte variant) {
        super($$0);
        this.breed = variant;
    }

    public static final String OWNER_UUID_TAG = "owner";
    public static final String SKIN_TAG = "skin";
    public static final String HEALTH_TAG = "health";

    private static final float SPEED = 0.4f;
    public byte getBubbleSkin() {
        if (breed == 1) { return 1; }

        return 0;
    }

    private static final int COOLDOWN = 30;
    private static final int LIFE_SPAN = 180;

    static public void saveStrayCatEntityInfo(ItemStack stack, StrayCatEntity stray) {
        CompoundTag tag = stack.getOrCreateTag();
        //tag.putString(NAME_TAG, stray.get);

        if (stray.isTame() && stray.getOwnerUUID() != null) {
            tag.putUUID(OWNER_UUID_TAG, stray.getOwnerUUID());
            tag.putByte(SKIN_TAG, stray.getBreed());
        }
        tag.putFloat(HEALTH_TAG, stray.getHealth());

        if (stray.hasCustomName()) {
            stack.setHoverName(stray.getCustomName());
        }

        stack.setTag(tag);
    }


    static public boolean validateStrayCatOwner(ItemStack stack, LivingEntity attempt) {
        if (stack.getItem() instanceof StrayCatItem && stack.hasTag() && attempt != null) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(OWNER_UUID_TAG)) {
                UUID uuid = tag.getUUID(OWNER_UUID_TAG);
                return uuid.equals(attempt.getUUID());
            }
        }

        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack $$0, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player P) {
            if (!((TimeStop) P.level()).inTimeStopRange(P)) {

                if (!level.isClientSide && !isSleeping(level, livingEntity)) {
                    P.getCooldowns().addCooldown(ModItems.STRAY_CAT_MANGA, COOLDOWN);
                    P.getCooldowns().addCooldown(ModItems.STRAY_CAT_ANIME, COOLDOWN);

                    StrayCatAirBubble bubble = ModEntities.STRAY_CAT_AIRBUBBLE.create(level);
                    if (bubble != null) {
                        bubble.setSped(SPEED);
                        bubble.setOwner(P);
                        bubble.setSkin(this.getBubbleSkin());
                        bubble.setFollowOwnerView(true);
                        bubble.setLifeSpan(LIFE_SPAN);

                        Vec3 addToPosition = new Vec3(0, P.getEyeHeight() * 0.85f, 0);
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
        }
        return $$0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if ($$1 != null){
            if (!((TimeStop) $$1.level()).inTimeStopRange($$1) && !isSleeping($$0, $$1)) {
                $$1.startUsingItem($$2);
            } else {
                return InteractionResultHolder.fail($$3);
            }
        }
        return InteractionResultHolder.sidedSuccess($$3,$$0.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        BlockPos abovePos = blockPos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!aboveState.isAir() || !player.isCrouching()) return InteractionResult.PASS;

        if (!level.isClientSide && aboveState.isAir() && player.isCrouching()) {
            Vec3 spawnPos = Vec3.atCenterOf(abovePos);

            StrayCatEntity stray = ModEntities.STRAY_CAT.create(level);
            if (stray != null) {
                ItemStack item = context.getItemInHand();
                CompoundTag tag = item.getOrCreateTag();
                if (tag.contains(OWNER_UUID_TAG)) {
                    stray.setOwnerUUID(tag.getUUID(OWNER_UUID_TAG));
                    stray.setTame(true);
                }
                if (tag.contains(HEALTH_TAG)) {
                    stray.setHealth(tag.getFloat(HEALTH_TAG));
                }
                stray.setBreed(breed);
                if (item.hasCustomHoverName()) {
                    stray.setCustomName(item.getHoverName());
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

    static boolean isSleeping(Level level, LivingEntity user) {
        long dayTime = level.getDayTime() % 24000;
        boolean canSeeSun = true;
        if (user != null) {
            canSeeSun = level.canSeeSky(user.blockPosition());
        }

        return dayTime >= 13000 && dayTime <= 23750 || !canSeeSun;
    }


    public float getCurrentPredicateValue(Level level, ItemStack stack, LivingEntity user) {
        CompoundTag tag = stack.getOrCreateTag();
        float value = 0.0f;

        if (level != null) {
            if (isSleeping(level, user)) {
                value += 0.1f;
            }
        }

        return value;
    }

    @Override public int getUseDuration(ItemStack $$0) {
        return 1;
    }
    @Override public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.NONE;
    }

}

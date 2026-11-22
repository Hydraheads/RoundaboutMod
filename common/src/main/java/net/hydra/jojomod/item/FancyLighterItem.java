package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class FancyLighterItem extends BlockItem {

    public FancyLighterItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }

    private static final String IS_LIT_TAG = "IsLighterLit";

    private boolean getIsLit(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(IS_LIT_TAG);
    }

    public void setIsNotLit(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(IS_LIT_TAG, value);
    }

    public static ItemStack stuff(ItemStack $$0, Player p){
        if(!$$0.hasTag()){
            CompoundTag tag = $$0.getOrCreateTag();
            tag.putUUID("StuffOther", p.getUUID());
        }

        return $$0;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level,
                                                 @Nullable Player player, ItemStack stack, BlockState state) {

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof FancyLighterBlockEntity lighter) {
            lighter.setStoredStack(stack);
            if(player != null) {
                lighter.setValue(player.getUUID());
            }
        }

        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if(entity.isInWaterOrRain()){
            if(!getIsLit(stack)) {
                unlit(stack);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$0.playSound(null, $$1.getX(), $$1.getY(), $$1.getZ(), ModSounds.TURNING_ON_LIGHTER_EVENT, SoundSource.PLAYERS, 1F, (float)(0.96F+Math.random()*0.08f));
        $$1.getCooldowns().addCooldown(this, 30);
        if(!$$1.isInWaterOrRain()) {
            if ($$3.getItem() instanceof FancyLighterItem FI) {
                FI.lirOrUnlit($$0, $$1, $$3);
            }
        }
        InteractionResultHolder<ItemStack> placed = super.use($$0,$$1,$$2);
        return placed;
    }

    public void unlit(ItemStack stack){
        this.setIsNotLit(stack, true);
    }

    public void lirOrUnlit(Level $$0, Player $$1, ItemStack $$2){
        if(!$$1.isInWaterOrRain()) {
            if (!getIsLit($$2)) {
                setIsNotLit($$2, true);
            } else {
                setIsNotLit($$2, false);
                blackSabbathGetsMad($$0, $$1, $$2);
            }
        }
    }

    private void blackSabbathGetsMad(Level lv, Player $$0, ItemStack $$1){
        CompoundTag tag = $$1.getTag();
            if(lv instanceof ServerLevel $$8) {
                if (tag == null){
                    return;
                }
                if (!tag.hasUUID("StuffOther")){
                    return;
                }
                        Entity user = $$8.getEntity(tag.getUUID("StuffOther"));
                        if (user != null && $$0 != null) {
                            if(user != $$0){
                                if(((StandUser)user).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
                                    if(pbs.tickDown2 <= -9) {
                                        if(!((StandUser) user).roundabout$getActive()){
                                            ((StandUser) user).roundabout$setActive(true);
                                        }

                                        if (!pbs.blackSabbathTargets.contains($$0)) {
                                            pbs.selectTargetSecond($$0);
                                        }
                                            if(pbs.moveMode != 3) {
                                                pbs.setThree();
                                                pbs.setTickBeforeHunt(20);
                                                if (pbs.blackSelect != null) {
                                                    pbs.blackSelect.forceDespawnSet = true;
                                                }
                                                if (pbs.getStandEntity(pbs.self) != null) {
                                                    pbs.getStandEntity(pbs.self).forceDespawnSet = true;
                                                }
                                            }

                                            List<LivingEntity> lvent = $$0.level().getEntitiesOfClass(LivingEntity.class, $$0.getBoundingBox().inflate(ClientNetworking.getAppropriateConfig().blackSabbathSettings.lighterWitnessRange), (livingEntity) -> {
                                                return true;
                                            });
                                            if (lvent != null && !lvent.isEmpty()) {
                                                for (LivingEntity value : lvent) {
                                                    if (value.hasLineOfSight($$0)) {
                                                        if (!(value instanceof StandEntity || value instanceof RoadRollerEntity || value.is(pbs.self))) {
                                                                pbs.selectTargetSecond(value);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                }
                    }
                }
            }
    }

    public float getCurrentPredicateValue(Level level, ItemStack stack) {

    if(level != null) {
            return getIsLit(stack) ? 0.2f : 0.0f;
        }
          return  0.0f;
    }
}

package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.BlockHitResult;

import java.util.EnumSet;

public class CorpseBuildBreakGoal extends Goal {

    private final FallenMob fallenMob;
    private Player owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private final BlockPos useOn;
    private float oldWaterCost;
    private BlockHitResult blockHit;
    private final boolean canFly;
    private int diggingTime = -1;

    public CorpseBuildBreakGoal(FallenMob $$0, double $$1, BlockPos $$2, BlockHitResult $$3, boolean $$4) {
        this.fallenMob = $$0;
        this.level = $$0.level();
        this.speedModifier = $$1;
        this.navigation = $$0.getNavigation();
        this.useOn = $$2;
        this.blockHit = $$3;
        this.canFly = $$4;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!($$0.getNavigation() instanceof GroundPathNavigation) && !($$0.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
        if($$0.controller instanceof Player plr){
            this.owner = plr;
        } else{
            Roundabout.LOGGER.info("A corpse is not being controlled by a player???");

        }
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = null;
        if (this.fallenMob.controller instanceof LivingEntity LE){
            $$0 = LE;
        }
        if ($$0 == null) {
            return false;
        } else if ($$0.isSpectator()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else if (!(this.fallenMob.controller instanceof Player)){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return !this.unableToMove();
        }
    }

    private boolean unableToMove() {
        return this.fallenMob.isPassenger() || this.fallenMob.isLeashed();
    }

    @Override
    public void start() {
        Vec3i direction =  blockHit.getDirection().getNormal();
        BlockPos mineBlock = new BlockPos((useOn.getX() + direction.getX()*-1), (useOn.getY() + direction.getY()*-1), (useOn.getZ() + direction.getZ()*-1));
        BlockState bstate = this.fallenMob.level().getBlockState(mineBlock);
        this.oldWaterCost = this.fallenMob.getPathfindingMalus(BlockPathTypes.WATER);
        this.fallenMob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        if(!this.fallenMob.getMainHandItem().isCorrectToolForDrops(bstate) && !(this.fallenMob.getMainHandItem().getItem() instanceof BlockItem)){
            //Don't mine

            this.stop();
            this.fallenMob.removeBuildBreakGoal();
        }
    }

    @Override
    public void stop() {

        this.fallenMob.getNavigation().stop();
        //HACK ALERT
        this.fallenMob.getNavigation().moveTo(this.fallenMob,1);
        this.fallenMob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);

    }

    public int getEnchLevel(String enchTags){
        if(this.fallenMob.getMainHandItem().getEnchantmentTags().getAsString().indexOf(enchTags) == -1){
            return -1;
        }
        int enchtagsInd = this.fallenMob.getMainHandItem().getEnchantmentTags().getAsString().indexOf(enchTags)+6+enchTags.length();

        return Integer.valueOf(this.fallenMob.getMainHandItem().getEnchantmentTags().getAsString().charAt(enchtagsInd));

    }
    @Override
    public void tick() {
        //Get distance
        double distance = Math.sqrt(Math.pow(this.fallenMob.getBlockX() - useOn.getX(),2) + Math.pow(this.fallenMob.getBlockY() - useOn.getY(),2) + Math.pow(this.fallenMob.getBlockZ() - useOn.getZ(),2));
        if (distance <= 5) {
            this.fallenMob.getNavigation().stop();
            //HACK ALERT
            this.fallenMob.getNavigation().moveTo(this.fallenMob,1);

            if(this.fallenMob.getMainHandItem().getItem() instanceof BlockItem block){
                block.place(new BlockPlaceContext(this.owner,this.fallenMob.swingingArm,this.fallenMob.getMainHandItem(),blockHit));
                this.fallenMob.getMainHandItem().setCount(this.fallenMob.getMainHandItem().getCount() - 1);
                this.stop();
                this.fallenMob.removeBuildBreakGoal();
            } else if(diggingTime > 0) {
                //And now we go mining.
                //A check to ensure no one tries to change items during mining
                if(this.fallenMob.getMainHandItem().getItem() instanceof BlockItem block){
                    block.place(new BlockPlaceContext(this.owner,this.fallenMob.swingingArm,this.fallenMob.getMainHandItem(),blockHit));
                    this.fallenMob.getMainHandItem().setCount(this.fallenMob.getMainHandItem().getCount() - 1);
                    this.stop();
                    this.fallenMob.removeBuildBreakGoal();
                }
                diggingTime -= 1;
                Vec3i direction =  blockHit.getDirection().getNormal();
                BlockPos mineBlock = new BlockPos((useOn.getX() + direction.getX()*-1), (useOn.getY() + direction.getY()*-1), (useOn.getZ() + direction.getZ()*-1));
                BlockState bstate = this.fallenMob.level().getBlockState(mineBlock);
                this.fallenMob.level().playSound(null,mineBlock, bstate.getSoundType().getStepSound(),this.fallenMob.getSoundSource());
                if(diggingTime == 0){

                    bstate.getBlock().destroy(this.fallenMob.level(),mineBlock,bstate);
                    this.fallenMob.level().destroyBlock(mineBlock,true);
                    if(this.fallenMob.getMainHandItem().isDamageableItem()) {
                        if(getEnchLevel("minecraft:unbreaking") != -1){
                            if(this.fallenMob.getRandom().nextIntBetweenInclusive(1,100) <= 100/(getEnchLevel("minecraft:unbreaking")+1)){
                                this.fallenMob.getMainHandItem().setDamageValue(this.fallenMob.getMainHandItem().getDamageValue() + 1);

                            }
                        } else{this.fallenMob.getMainHandItem().setDamageValue(this.fallenMob.getMainHandItem().getDamageValue() + 1);}

                    }
                    this.stop();
                    this.fallenMob.removeBuildBreakGoal();
                }
            }
            else{
                //Another check to ensure blocks are placed
                if(this.fallenMob.getMainHandItem().getItem() instanceof BlockItem block){
                    block.place(new BlockPlaceContext(this.owner,this.fallenMob.swingingArm,this.fallenMob.getMainHandItem(),blockHit));
                    this.fallenMob.getMainHandItem().setCount(this.fallenMob.getMainHandItem().getCount() - 1);
                    this.stop();
                    this.fallenMob.removeBuildBreakGoal();
                }
                Vec3i direction =  blockHit.getDirection().getNormal();
                BlockPos mineBlock = new BlockPos((useOn.getX() + direction.getX()*-1), (useOn.getY() + direction.getY()*-1), (useOn.getZ() + direction.getZ()*-1));
                BlockState bstate = this.fallenMob.level().getBlockState(mineBlock);

                if(bstate.getBlock().defaultDestroyTime() == -1){
                    //Unbreakable
                    this.stop();
                    this.fallenMob.removeBuildBreakGoal();

                } else {

                    double digTimebuilder;
                    digTimebuilder = this.fallenMob.getMainHandItem().getDestroySpeed(bstate);

                    if(this.fallenMob.getEffect(MobEffects.DIG_SPEED) != null){
                       digTimebuilder *= 0.2 * this.fallenMob.getEffect(MobEffects.DIG_SPEED).getAmplifier() + 1;
                    }
                    if(this.fallenMob.getEffect(MobEffects.DIG_SLOWDOWN) != null){
                        digTimebuilder *= Math.pow(0.3, Math.min(this.fallenMob.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier() + 1,4) );
                    }

                    digTimebuilder = ((digTimebuilder / bstate.getBlock().defaultDestroyTime()) / 30);

                    if(digTimebuilder > 1){
                        //Instantly break
                        bstate.getBlock().destroy(this.fallenMob.level(),mineBlock,bstate);
                        this.fallenMob.level().destroyBlock(mineBlock,true);
                        if(this.fallenMob.getMainHandItem().isDamageableItem()) {
                            if(getEnchLevel("minecraft:unbreaking") != -1){
                                if(this.fallenMob.getRandom().nextIntBetweenInclusive(1,100) <= 100/(getEnchLevel("minecraft:unbreaking")+1)){
                                    this.fallenMob.getMainHandItem().setDamageValue(this.fallenMob.getMainHandItem().getDamageValue() + 1);

                                }
                            } else{this.fallenMob.getMainHandItem().setDamageValue(this.fallenMob.getMainHandItem().getDamageValue() + 1);}

                        }
                        this.stop();
                        this.fallenMob.removeBuildBreakGoal();

                    } else{
                        diggingTime = (int) (1 / digTimebuilder);
                    }


                }
            }








        } else{
            this.fallenMob.getNavigation().moveTo(this.fallenMob.getNavigation().createPath(useOn, 0), 1);
        }
    }




}

package net.hydra.jojomod.mixin.scissors;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public class ScissorsBeehiveBlock {
    /**This mixin is for scissors being usuable on beehive blocks
     * For context, shears are usable in this regard, and
     * scissors double as shears in a lot of ways.
     *
     * The code is mostly just a faithful use of vanilla code so if it updates check this out.*/

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack $$6 = $$3.getItemInHand($$4);
        if ($$6.is(ModItems.SCISSORS)) {
            ItemStack itemStack = $$3.getItemInHand($$4);
            int i = $$0.getValue(HONEY_LEVEL);
            boolean bl = false;
            if (i >= 5) {
                    $$1.playSound($$3, $$3.getX(), $$3.getY(), $$3.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0f, 1.0f);
                    BeehiveBlock.dropHoneycomb($$1, $$2);
                    itemStack.hurtAndBreak(2, $$3, player -> player.broadcastBreakEvent($$4));
                    bl = true;
                    $$1.gameEvent((Entity) $$3, GameEvent.SHEAR, $$2);


                if (!CampfireBlock.isSmokeyPos($$1, $$2)) {
                    if (this.hiveContainsBees($$1, $$2)) {
                        this.angerNearbyBees($$1, $$2);
                    }
                    this.releaseBeesAndResetHoneyLevel($$1, $$0, $$2, $$3, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                } else {
                    this.resetHoneyLevel($$1, $$0, $$2);
                }
                cir.setReturnValue(InteractionResult.sidedSuccess($$1.isClientSide));

            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    @Final
    public static IntegerProperty HONEY_LEVEL;
    @Shadow
    public void resetHoneyLevel(Level level, BlockState blockState, BlockPos blockPos){
    }
    @Shadow
    private boolean hiveContainsBees(Level level, BlockPos blockPos){
        return false;
    }

    @Shadow
    private void angerNearbyBees(Level level, BlockPos blockPos){
    }

    @Shadow
    public void releaseBeesAndResetHoneyLevel(Level level, BlockState blockState, BlockPos blockPos, @Nullable Player player, BeehiveBlockEntity.BeeReleaseStatus beeReleaseStatus) {
    }
}

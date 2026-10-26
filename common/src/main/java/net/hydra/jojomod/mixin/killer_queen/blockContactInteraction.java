package net.hydra.jojomod.mixin.killer_queen;


import net.hydra.jojomod.access.IBlockAccess;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = BlockBehaviour.class)
public abstract class blockContactInteraction {

    //protected blockContactInteraction(Properties $$0) { super($$0); }

    //@SuppressWarnings("deprecation")
    /*@Inject(method = "use", at = @At("TAIL"), cancellable = true)
    public void roundabout$interaction(BlockState $$0, Level level, BlockPos pos, Player $$3, InteractionHand $$4, BlockHitResult $$5, CallbackInfoReturnable<InteractionResult> cir) {
        if (level != null && !level.isClientSide) {
            for (Entity ent : MainUtil.getEntitiesInRange(level, pos, 1.0f)) {
                if (ent instanceof BlockBombEntity BBE) {
                    if (BBE.getUser() != null && BBE.getBlockPos() != null && BBE.getBlockPos().equals(pos)) {
                        if (((StandUser)BBE.getUser()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ) {
                            PKQ.blockContact($$3);
                        }
                    }
                }
            }
        }
    }*/
}

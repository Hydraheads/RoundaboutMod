package net.hydra.jojomod.item.paintings;

import net.hydra.jojomod.entity.paintings.RoundaboutPainting;
import net.hydra.jojomod.entity.paintings.VanGoughPainting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class VanGoughPaintingItem  extends Item {
    private static final Component TOOLTIP_RANDOM_VARIANT = Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);

    public VanGoughPaintingItem(Item.Properties p_41325_) {
        super(p_41325_);
    }

    public InteractionResult useOn(UseOnContext p_41331_) {
        BlockPos blockpos = p_41331_.getClickedPos();
        Direction direction = p_41331_.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player player = p_41331_.getPlayer();
        ItemStack itemstack = p_41331_.getItemInHand();
        if (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)) {
            return InteractionResult.FAIL;
        } else {
            Level level = p_41331_.getLevel();
            HangingEntity hangingentity;
            Optional<RoundaboutPainting> optional = VanGoughPainting.create(level, blockpos1, direction);
            if (optional.isEmpty()) {
                return InteractionResult.CONSUME;
            }

            hangingentity = optional.get();

            CompoundTag compoundtag = itemstack.getTag();
            if (compoundtag != null) {
                EntityType.updateCustomEntityTag(level, player, hangingentity, compoundtag);
            }

            if (hangingentity.survives()) {
                if (!level.isClientSide) {
                    hangingentity.playPlacementSound();
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, hangingentity.position());
                    level.addFreshEntity(hangingentity);
                }

                itemstack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player p_41326_, Direction p_41327_, ItemStack p_41328_, BlockPos p_41329_) {
        return !p_41327_.getAxis().isVertical() && p_41326_.mayUseItemAt(p_41329_, p_41327_, p_41328_);
    }

}
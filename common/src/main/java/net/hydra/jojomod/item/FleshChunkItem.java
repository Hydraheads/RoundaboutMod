package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class FleshChunkItem extends BlockItem {


    public FleshChunkItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 40;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack $$3 = super.finishUsingItem(itemStack,level,entity);
        if ( (((StandUser)entity )).roundabout$getStandPowers() instanceof PowersRatt) {
            entity.removeEffect(MobEffects.HUNGER);
            FoodData f = ((Player)entity).getFoodData();
            f.setSaturation(f.getSaturationLevel()+0.6F);
            f.setFoodLevel(f.getFoodLevel()+4);
        }
        return $$3;
    }
}

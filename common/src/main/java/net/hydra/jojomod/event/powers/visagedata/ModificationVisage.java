package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModificationVisage extends NonCharacterVisage {
    @Override
    public VisageData generateVisageData(LivingEntity entity){
        return new ModificationVisage(entity);
    }
    public ModificationVisage(LivingEntity self) {
        super(self);
    }

    public boolean rendersPlayerBreastPart() {
        if (self instanceof Player pl) {
            ItemStack stack = ((IPlayerEntity) pl).roundabout$getMaskSlot();
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ModificationMaskItem) {
                    return stack.getOrCreateTagElement("modifications").getInt("chest") == 2;
            }
        }
        return false;
    }
    public boolean rendersPlayerSmallBreastPart() {
        if (self instanceof Player pl) {
            ItemStack stack = ((IPlayerEntity) pl).roundabout$getMaskSlot();
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ModificationMaskItem) {
                return stack.getOrCreateTagElement("modifications").getInt("chest") == 1;
            }
        }
        return false;
    }
    public boolean rendersArmor(){
        if (self instanceof Player pl) {
            ItemStack stack = ((IPlayerEntity) pl).roundabout$getMaskSlot();
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ModificationMaskItem) {
                return stack.getOrCreateTagElement("modifications").getInt("chest") == 3;
            }
        }
        return false;
    }

    /**
     *     public boolean rendersPlayerBreastPart() {
     *         if (self instanceof Player pl) {
     *             return ((IPlayerEntity)pl).roundabout$getModChest() == 2;
     *             //                //return stack.getOrCreateTagElement("modifications").getInt("chest") == 2;
     *         }
     *         return false;
     *     }
     *
     * */
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.MODIFIED_NPC.create(pl.level());
    }
}

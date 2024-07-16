package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class NewLocacacaItem extends LocacacaItem{

    /**32 is the default eating duration of a food. New Locaca takes notably longer to eat.*/
    @Override
    public int getUseDuration(ItemStack $$0) {
            return 64;
    }
    public NewLocacacaItem(Properties $$0) {
        super($$0);
    }


    @Override
    public void onUseTick(Level $$0, LivingEntity $$1, ItemStack $$2, int $$3) {

    }
}

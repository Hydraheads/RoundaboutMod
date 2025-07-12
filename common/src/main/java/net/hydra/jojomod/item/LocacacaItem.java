package net.hydra.jojomod.item;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LocacacaItem extends Item {
    public LocacacaItem(Properties $$0) {
        super($$0);
    }

    public void randomizeStone(LivingEntity entity){
        byte curse = ((StandUser)entity).roundabout$getLocacacaCurse();
        if (curse < 0){
            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            health += (maxHealth*1F);
            if (health > maxHealth){
                health=maxHealth;
            }
            entity.setHealth(health);
        }


        if (entity.hasEffect(ModEffects.HEX)){
            ((StandUser)entity).roundabout$setLocacacaCurse(LocacacaCurseIndex.HEART);
        }

        double random = Math.random();
        byte newCurse = -1;
        if (random <= 0.15F){
            newCurse= LocacacaCurseIndex.LEFT_LEG;
        } else if (random <= 0.3F){
            newCurse= LocacacaCurseIndex.RIGHT_LEG;
        } else if (random <= 0.45F){
            newCurse= LocacacaCurseIndex.LEFT_HAND;
        } else if (random <= 0.6F){
            newCurse= LocacacaCurseIndex.RIGHT_HAND;
        } else if (random <= 0.75F){
            newCurse= LocacacaCurseIndex.CHEST;
        } else if (random <= 0.9F){
            newCurse= LocacacaCurseIndex.HEAD;
        } else {
            newCurse= LocacacaCurseIndex.HEART;
        }
        if (curse != newCurse){
            curse = newCurse;
        } else {
            curse++;
            if (curse > LocacacaCurseIndex.HEART){
                curse=LocacacaCurseIndex.LEFT_LEG;
            }
        }
        ((StandUser)entity).roundabout$setLocacacaCurse(curse);

    }
    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack $$3 = super.finishUsingItem(itemStack, level, entity);
        if (!level.isClientSide && entity.isAlive()) {
            randomizeStone(entity);
            SoundEvent $$6 = ModSounds.LOCACACA_PETRIFY_EVENT;
            level.playSound(null,entity,$$6, SoundSource.PLAYERS, 1.0F, 1F);
        }

        return $$3;
    }
}

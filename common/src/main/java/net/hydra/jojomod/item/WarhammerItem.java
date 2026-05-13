package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WarhammerItem extends DiggerItem  {
    /**The hammer joseph uses against wamuu in the chariot fight
     * inputevents contains its code to vault up blocks and entities*/
    public WarhammerItem(Tier $$0, float $$1, float $$2, Item.Properties $$3) {
        super($$1, $$2, $$0, BlockTags.MINEABLE_WITH_PICKAXE, $$3);
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        float spd = super.getDestroySpeed(itemStack,blockState);
        if (spd > 1){
            spd*=3f;
        }
        return spd;
    }
}

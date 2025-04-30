package net.hydra.jojomod.entity.npcs;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersCinderella;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Aesthetician extends StandUsingNPC {
    public Aesthetician(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }

    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem) ModItems.STAND_DISC_CINDERELLA);
    }

    public List<Player> interactingWith = new ArrayList<>();

    public void initInteractCheck(){
        if (interactingWith == null) {interactingWith = new ArrayList<>();}
    }

    public void addPlayerToList(Player pl){
        initInteractCheck();
        if (!interactingWith.contains(pl)){
            interactingWith.add(pl);
        }
    }

    public void removePlayerFromList(Player pl){
        initInteractCheck();
        interactingWith.remove(pl);
    }

    public InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$0.level().isClientSide() && ((StandUser)this).roundabout$getStandPowers() instanceof PowersCinderella) {

            ClientUtil.setCinderellaUI(true,this.getId());
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract($$0, $$1);
        }
    }

    @Override
    public boolean isUsingBrain(){
        initInteractCheck();
        if (this.getTarget() == null){
            if (!interactingWith.isEmpty()) {
                return false;
            }
        } else {
            if (!interactingWith.isEmpty()) {
                interactingWith = new ArrayList<>();
            }
        }
        return true;
    }
    @Override
    public void useNotBrain(){
        initInteractCheck();
        if (!interactingWith.isEmpty()){

        }
    }
}

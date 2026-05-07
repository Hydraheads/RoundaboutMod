package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import javax.annotation.Nullable;
import java.util.List;

public class FateDeathScreen extends Screen {
    private int delayTicker;
    private final Component causeOfDeath;
    private final boolean hardcore;
    private Component deathScore;
    private final List<Button> exitButtons = Lists.newArrayList();
    @Nullable
    private Button exitToTitleButton;

    public FateDeathScreen(@Nullable Component $$0, boolean $$1) {
        super(Component.translatable($$1 ? "deathScreen.title.hardcore" : "deathScreen.title"));
        this.causeOfDeath = $$0;
        this.hardcore = $$1;
    }

    protected void respawnPacket(){
        this.minecraft.player.respawn();
    }
    protected void respawnHumanPacket(){
        this.minecraft.player.respawn();
    }

    @Override
    protected void init() {
        this.delayTicker = 0;
        this.exitButtons.clear();
        Component $$0 = this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");


        this.exitButtons.add(this.addRenderableWidget(Button.builder($$0, $$0x -> {
            respawnPacket();
            $$0x.active = false;
        }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));


        if (!hardcore) {
            Component humanRespawn = Component.translatable(Roundabout.MOD_ID,"text.roundabout.respawn_as_human");
            this.exitButtons.add(this.addRenderableWidget(Button.builder(humanRespawn, $$0x -> {
                respawnPacket();
                $$0x.active = false;
            }).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build()));

            this.exitToTitleButton = this.addRenderableWidget(
                    Button.builder(
                                    Component.translatable("deathScreen.titleScreen"),
                                    $$0x -> this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true)
                            )
                            .bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20)
                            .build()
            );
        } else {
            this.exitToTitleButton = this.addRenderableWidget(
                    Button.builder(
                                    Component.translatable("deathScreen.titleScreen"),
                                    $$0x -> this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true)
                            )
                            .bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20)
                            .build()
            );
        }
        this.exitButtons.add(this.exitToTitleButton);
        this.setButtonsActive(false);
        this.deathScore = Component.translatable("deathScreen.score")
                .append(": ")
                .append(Component.literal(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void handleExitToTitleScreen() {
        if (this.hardcore) {
            this.exitToTitleScreen();
        } else {
            ConfirmScreen $$0 = new DeathScreen.TitleConfirmScreen(
                    $$0x -> {
                        if ($$0x) {
                            this.exitToTitleScreen();
                        } else {
                            this.minecraft.player.respawn();
                            this.minecraft.setScreen(null);
                        }
                    },
                    Component.translatable("deathScreen.quit.confirm"),
                    CommonComponents.EMPTY,
                    Component.translatable("deathScreen.titleScreen"),
                    Component.translatable("deathScreen.respawn")
            );
            this.minecraft.setScreen($$0);
            $$0.setDelay(20);
        }
    }

    private void exitToTitleScreen() {
        if (this.minecraft.level != null) {
            this.minecraft.level.disconnect();
        }

        this.minecraft.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        this.minecraft.setScreen(new TitleScreen());
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        $$0.fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
        $$0.pose().pushPose();
        $$0.pose().scale(2.0F, 2.0F, 2.0F);
        $$0.drawCenteredString(this.font, this.title, this.width / 2 / 2, 30, 16777215);
        $$0.pose().popPose();
        if (this.causeOfDeath != null) {
            $$0.drawCenteredString(this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
        }

        $$0.drawCenteredString(this.font, this.deathScore, this.width / 2, 100, 16777215);
        if (this.causeOfDeath != null && $$2 > 85 && $$2 < 85 + 9) {
            Style $$4 = this.getClickedComponentStyleAt($$1);
            $$0.renderComponentHoverEffect(this.font, $$4, $$1, $$2);
        }

        super.render($$0, $$1, $$2, $$3);
        if (this.exitToTitleButton != null && this.minecraft.getReportingContext().hasDraftReport()) {
            $$0.blit(
                    AbstractWidget.WIDGETS_LOCATION,
                    this.exitToTitleButton.getX() + this.exitToTitleButton.getWidth() - 17,
                    this.exitToTitleButton.getY() + 3,
                    182,
                    24,
                    15,
                    15
            );
        }
    }

    @Nullable
    private Style getClickedComponentStyleAt(int $$0) {
        if (this.causeOfDeath == null) {
            return null;
        } else {
            int $$1 = this.minecraft.font.width(this.causeOfDeath);
            int $$2 = this.width / 2 - $$1 / 2;
            int $$3 = this.width / 2 + $$1 / 2;
            return $$0 >= $$2 && $$0 <= $$3 ? this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, $$0 - $$2) : null;
        }
    }

    @Override
    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        if (this.causeOfDeath != null && $$1 > 85.0 && $$1 < (double)(85 + 9)) {
            Style $$3 = this.getClickedComponentStyleAt((int)$$0);
            if ($$3 != null && $$3.getClickEvent() != null && $$3.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                this.handleComponentClicked($$3);
                return false;
            }
        }

        return super.mouseClicked($$0, $$1, $$2);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.delayTicker++;
        if (this.delayTicker == 20) {
            this.setButtonsActive(true);
        }
    }

    private void setButtonsActive(boolean $$0) {
        for (Button $$1 : this.exitButtons) {
            $$1.active = $$0;
        }
    }

    public static class TitleConfirmScreen extends ConfirmScreen {
        public TitleConfirmScreen(BooleanConsumer $$0, Component $$1, Component $$2, Component $$3, Component $$4) {
            super($$0, $$1, $$2, $$3, $$4);
        }
    }
}

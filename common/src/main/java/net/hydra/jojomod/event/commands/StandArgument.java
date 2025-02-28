package net.hydra.jojomod.event.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.hydra.jojomod.Roundabout;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class StandArgument implements ArgumentType<StandType> {

        private static final StandType[] VALUES = StandType.values();

        @Override
        public StandType parse(StringReader reader) throws CommandSyntaxException {
            String s = reader.readUnquotedString();
            StandType standType = StandType.byName(s, (StandType)null);
            if (standType == null) {
                return StandType.INVALID;
            } else {
                return standType;
            }
        }

    @Override
    public Collection<String> getExamples() {
        Roundabout.LOGGER.info("5");
        return Collections.emptyList();
    }
    @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_259767_, SuggestionsBuilder p_259515_) {
            Roundabout.LOGGER.info("2");
            return p_259767_.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(Arrays.stream(VALUES).map(StandType::getName), p_259515_) : Suggestions.empty();
        }

        public static StandArgument gameMode() {
            Roundabout.LOGGER.info("3");
            return new StandArgument();
        }

        public static StandType getStandType(CommandContext<CommandSourceStack> p_259927_, String name) {
            Roundabout.LOGGER.info("4");
            return p_259927_.getArgument(name, StandType.class);
        }
}

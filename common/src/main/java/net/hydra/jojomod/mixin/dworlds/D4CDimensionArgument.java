package net.hydra.jojomod.mixin.dworlds;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Mixin(DimensionArgument.class)
public class D4CDimensionArgument {
    @Inject(method = "listSuggestions", at = @At("HEAD"), cancellable = true)
    private <S> void onListSuggestions(CommandContext<S> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        if (context.getSource() instanceof SharedSuggestionProvider source) {
            Stream<ResourceLocation> filteredStream = source.levels().stream()
                    .map(ResourceKey::location)
                    .filter(loc -> !loc.toString().startsWith("roundabout:d4c-"));

            cir.setReturnValue(SharedSuggestionProvider.suggestResource(filteredStream, builder));
        } else {
            cir.setReturnValue(Suggestions.empty());
        }
    }
}

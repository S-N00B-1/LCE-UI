package net.kyrptonaught.lceui.whatsThis;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ViewableDescriptionArgumentType implements ArgumentType<ViewableDescription> {
    public static final DynamicCommandExceptionType UNKNOWN_VIEWED_DESCRIPTION_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.translatable("lceui.viewedBlock.notFound", id));

    private final boolean viewed;

    private ViewableDescriptionArgumentType(boolean viewed) {
        this.viewed = viewed;
    }

    public static ViewableDescriptionArgumentType viewedDescriptionArgumentType() {
        return new ViewableDescriptionArgumentType(true);
    }

    public static ViewableDescriptionArgumentType viewableDescriptionArgumentType() {
        return new ViewableDescriptionArgumentType(false);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(WhatsThisInit.descriptionManager.viewedDescriptions, builder);
    }

    public static ViewableDescription getViewableDescriptionArgumentType(CommandContext<FabricClientCommandSource> context, String name) {
        return context.getArgument(name, ViewableDescription.class);
    }

    @Override
    public ViewableDescription parse(StringReader stringReader) throws CommandSyntaxException {
        boolean tag = false;
        if (stringReader.canRead() && stringReader.peek() == '#') {
            tag = true;
            stringReader.skip();
        }
        Identifier identifier = Identifier.fromCommandInput(stringReader);
        ViewableDescription viewableDescription = new ViewableDescription(identifier, tag);
        if (this.viewed && !WhatsThisInit.descriptionManager.viewedDescriptions.contains(viewableDescription.toString())) throw UNKNOWN_VIEWED_DESCRIPTION_EXCEPTION.createWithContext(stringReader, viewableDescription.toString());
        return viewableDescription;
    }
}

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

public class ViewedBlockArgumentType implements ArgumentType<ViewedBlock> {
    public static final DynamicCommandExceptionType UNKNOWN_VIEWED_BLOCK_OR_TAG_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.translatable("lceui.viewedBlock.notFound", id));

    public static ViewedBlockArgumentType viewedBlockArgumentType() {
        return new ViewedBlockArgumentType();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(WhatsThisInit.descriptionManager.viewedDescriptions, builder);
    }

    public static ViewedBlock getViewedBlockArgumentType(CommandContext<FabricClientCommandSource> context, String name) {
        return context.getArgument(name, ViewedBlock.class);
    }

    @Override
    public ViewedBlock parse(StringReader stringReader) throws CommandSyntaxException {
        boolean tag = false;
        if (stringReader.canRead() && stringReader.peek() == '#') {
            tag = true;
            stringReader.skip();
        }
        Identifier identifier = Identifier.fromCommandInput(stringReader);
        ViewedBlock viewedBlock = new ViewedBlock(identifier, tag);
        if (!WhatsThisInit.descriptionManager.viewedDescriptions.contains(viewedBlock.toString())) throw UNKNOWN_VIEWED_BLOCK_OR_TAG_EXCEPTION.createWithContext(stringReader, viewedBlock.toString());
        return viewedBlock;
    }
}

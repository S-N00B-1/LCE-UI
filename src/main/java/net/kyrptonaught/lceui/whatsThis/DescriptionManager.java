package net.kyrptonaught.lceui.whatsThis;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.ClientTagHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import java.util.*;

public class DescriptionManager {
    public HashMap<Identifier, ItemDescription> itemDescriptions = new HashMap<>();
    public HashSet<String> viewedDescriptions = new HashSet<>();

    public void clearDescriptions() {
        itemDescriptions.clear();
    }

    public static Optional<String> findTagForID(Identifier itemID) {
        itemID = WhatsThisInit.getCleanIdentifier(itemID);
        Map<Identifier, Collection<Identifier>> tags = ClientTagHelper.getTagsInPath(new Identifier(LCEUIMod.MOD_ID, "descriptions"));
        for (Identifier tagID : tags.keySet()) {
            if (tags.get(tagID).contains(itemID))
                return Optional.of("#" + tagID.toString());
        }
        return Optional.empty();
    }

    public Optional<ItemDescription> getDescriptionForEntity(EntityType<?> entity) {
        Identifier id = Registries.ENTITY_TYPE.getId(entity);
        id = new Identifier(id.getNamespace(), "entity/" + id.getPath());
        ItemDescription blockDescription = itemDescriptions.getOrDefault(id, new ItemDescription());

        return getDescription(id, blockDescription, entity.getTranslationKey(), false);
    }

    public Optional<ItemDescription> getDescriptionForBlock(BlockState blockState) {
        Identifier id = Registries.BLOCK.getId(blockState.getBlock());
        id = new Identifier(id.getNamespace(), "block/" + id.getPath());
        ItemDescription blockDescription = itemDescriptions.getOrDefault(id, new ItemDescription());

        return getDescription(id, blockDescription, blockState.getBlock().getTranslationKey(), true);
    }

    public Optional<ItemDescription> getDescriptionForItem(ItemStack itemStack) {
        ItemStack itemStack1 = itemStack.copy();
        Identifier id = Registries.ITEM.getId(itemStack1.getItem());
        id = new Identifier(id.getNamespace(), "item/" + id.getPath());
        ItemDescription itemDescription = itemDescriptions.getOrDefault(id, new ItemDescription()).copy();

        if (itemStack.hasCustomName()) {
            itemDescription.text.name = itemStack.getName().copy();
        }

        return getDescription(id, itemDescription, itemStack1.getTranslationKey(), true);
    }

    private Optional<ItemDescription> getDescription(Identifier itemID, ItemDescription itemDescription, String defaultKey, Boolean defaultIconDisplay) {
        tryInherit(itemID, itemDescription, new HashSet<>());
        if (itemDescription.isFieldBlank(itemDescription.text.description) && !Language.getInstance().hasTranslation(defaultKey + ".description")) return Optional.empty();
        boolean isXof = false;
        Identifier foxId = EntityType.getId(EntityType.FOX);
        if (itemID.equals(new Identifier(foxId.getNamespace(), "entity/" + foxId.getPath())) && new Random().nextFloat() < 0.01f) {
            isXof = true;
        }
        if (itemDescription.isFieldBlank(itemDescription.text.name)) {
            String reverse = new StringBuilder(Text.translatable(defaultKey).getString()).reverse().toString();
            itemDescription.text.name = isXof ? Text.literal(reverse) : Text.translatable(defaultKey);
        }
        if (itemDescription.isFieldBlank(itemDescription.text.description)) {
            Style fontStyle = Style.EMPTY.withFont(new Identifier(LCEUIMod.MOD_ID, "icons"));
            String reverse = new StringBuilder(Text.translatable(defaultKey + ".description", Text.literal("\u0001").setStyle(fontStyle), Text.literal("\u0002").setStyle(fontStyle), Text.literal("\u0003").setStyle(fontStyle)).getString()).reverse().toString();
            itemDescription.text.description = isXof ? Text.literal(reverse) : Text.translatable(defaultKey + ".description", Text.literal("\u0001").setStyle(fontStyle), Text.literal("\u0002").setStyle(fontStyle), Text.literal("\u0003").setStyle(fontStyle));
        }
        if (itemDescription.displaysicon == null)
            itemDescription.displaysicon = defaultIconDisplay;

        if (itemDescription.isFieldBlank(itemDescription.group)) {
            itemDescription.group = findTagForID(itemID).orElse(itemID.toString());
        }
        return Optional.of(itemDescription);
    }

    public void tryInherit(Identifier id, ItemDescription itemDescription, HashSet<Identifier> trace) {
        if (trace.contains(id)) {
            System.out.println("Loop detected! " + trace);
            return;
        }
        trace.add(id);
        if (!itemDescription.isFieldBlank(itemDescription.parent)) {
            Identifier parentID = itemDescription.getParent();
            ItemDescription parent = itemDescriptions.get(parentID);
            if (parent == null) {
                if (id.getPath().contains("block")) {
                    Optional<ItemDescription> optional = getDescriptionForBlock(Registries.BLOCK.get(WhatsThisInit.getCleanIdentifier(parentID)).getDefaultState());
                    if (optional.isPresent()) {
                        parent = optional.get();
                    }
                } else if (id.getPath().contains("item")) {
                    Optional<ItemDescription> optional = getDescriptionForItem(Registries.ITEM.get(WhatsThisInit.getCleanIdentifier(parentID)).getDefaultStack());
                    if (optional.isPresent()) {
                        parent = optional.get();
                    }
                } else if (id.getPath().contains("entity")) {
                    Optional<ItemDescription> optional = getDescriptionForEntity(Registries.ENTITY_TYPE.get(WhatsThisInit.getCleanIdentifier(id)));
                    if (optional.isPresent()) {
                        parent = optional.get();
                    }
                }
            }
            if (parent != null) {
                tryInherit(parentID, parent, trace);
                itemDescription.copyFrom(parent);
            } else {
                System.out.println("Parent does not exist for Item " + id + " : " + parentID);
            }
        }
    }
}

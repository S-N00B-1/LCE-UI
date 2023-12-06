package net.kyrptonaught.lceui.whatsThis;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class DescriptionManager {
    public HashMap<Identifier, ItemDescription> itemDescriptions = new HashMap<>();
    public HashSet<String> viewedDescriptions = new HashSet<>();

    public HashMap<Identifier, Collection<Identifier>> tags = new HashMap<>();

    public void clearDescriptions() {
        itemDescriptions.clear();
    }

    public void clearTags() {
        tags.clear();
    }

    public void setTags(Map<Identifier, Collection<Identifier>> tags) {
        this.tags = new HashMap<>(tags);
    }

    public Optional<String> findTagForID(Identifier itemID) {
        itemID = WhatsThisInit.getCleanIdentifier(itemID);
        for (Identifier tagID : tags.keySet()) {
            if (tags.get(tagID).contains(itemID))
                return Optional.of("#" + tagID.toString());

        }
        return Optional.empty();
    }

    public ItemDescription getDescriptionForEntity(EntityType<?> entity) {
        Identifier id = Registry.ENTITY_TYPE.getId(entity);
        id = new Identifier(id.getNamespace(), "entity/" + id.getPath());
        ItemDescription blockDescription = itemDescriptions.getOrDefault(id, new ItemDescription());

        return getDescription(id, blockDescription, entity.getTranslationKey(), false);
    }

    public ItemDescription getDescriptionForBlock(BlockState blockState) {
        Identifier id = Registry.BLOCK.getId(blockState.getBlock());
        id = new Identifier(id.getNamespace(), "block/" + id.getPath());
        ItemDescription blockDescription = itemDescriptions.getOrDefault(id, new ItemDescription());

        return getDescription(id, blockDescription, blockState.getBlock().getTranslationKey(), true);
    }

    public ItemDescription getDescriptionForItem(ItemStack itemStack) {
        ItemStack itemStack1 = itemStack.copy();
        Identifier id = Registry.ITEM.getId(itemStack1.getItem());
        id = new Identifier(id.getNamespace(), "item/" + id.getPath());
        ItemDescription itemDescription = itemDescriptions.getOrDefault(id, new ItemDescription()).copy();

        if (itemStack.hasCustomName()) {
            itemDescription.text.name = itemStack.getName().copy();
        }

        return getDescription(id, itemDescription, itemStack1.getTranslationKey(), true);
    }

    private ItemDescription getDescription(Identifier itemID, ItemDescription itemDescription, String defaultKey, Boolean defaultIconDisplay) {
        tryInherit(itemID, itemDescription, new HashSet<>());
        boolean isXof = false;
        Identifier foxId = EntityType.getId(EntityType.FOX);
        if (itemID.equals(new Identifier(foxId.getNamespace(), "entity/" + foxId.getPath())) && new Random().nextFloat() < 1f) {
            isXof = true;
        }
        if (itemDescription.isFieldBlank(itemDescription.text.name)) {
            String reverse = new StringBuilder(Text.translatable(defaultKey).getString()).reverse().toString();
            itemDescription.text.name = isXof ? Text.literal(reverse) : Text.translatable(defaultKey);
        }
        if (itemDescription.isFieldBlank(itemDescription.text.description)) {
            String reverse = new StringBuilder(Text.translatable(defaultKey + ".description").getString()).reverse().toString();
            itemDescription.text.description = isXof ? Text.literal(reverse) : Text.translatable(defaultKey + ".description");
        }
        if (itemDescription.displaysicon == null)
            itemDescription.displaysicon = defaultIconDisplay;

        if (itemDescription.isFieldBlank(itemDescription.group)) {
            itemDescription.group = findTagForID(itemID).orElse(itemID.toString());
        }
        return itemDescription;
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
                if (id.getPath().contains("block"))
                    parent = getDescriptionForBlock(Registry.BLOCK.get(WhatsThisInit.getCleanIdentifier(parentID)).getDefaultState());
                else if (id.getPath().contains("item"))
                    parent = getDescriptionForItem(Registry.ITEM.get(WhatsThisInit.getCleanIdentifier(parentID)).getDefaultStack());
                else if (id.getPath().contains("entity"))
                    parent = getDescriptionForEntity(Registry.ENTITY_TYPE.get(WhatsThisInit.getCleanIdentifier(id)));
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

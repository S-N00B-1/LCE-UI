package net.kyrptonaught.lceui.tags;

import net.minecraft.util.Identifier;

import java.util.*;

public class ClientTagHelper {
    private static final Map<Identifier, Collection<Identifier>> clientTags = new HashMap<>();

    public static void setClientTags(Map<Identifier, Collection<Identifier>> tags) {
        clientTags.clear();
        clientTags.putAll(tags);
    }

    public static Collection<Identifier> getTag(Identifier id) {
        return clientTags.get(id);
    }

    public static boolean isInTag(Identifier tagId, Identifier item) {
        Collection<Identifier> tag = getTag(tagId);
        return tag != null && tag.contains(item);
    }

    public static Map<Identifier, Collection<Identifier>> getTagsInPath(Identifier id) {
        Map<Identifier, Collection<Identifier>> tags = new HashMap<>();
        for (Map.Entry<Identifier, Collection<Identifier>> entry : clientTags.entrySet()) {
            if (entry.getKey().getNamespace().equals(id.getNamespace()) && entry.getKey().getPath().startsWith(id.getPath())) {
                tags.put(entry.getKey(), entry.getValue());
            }
        }
        return tags;
    }
}

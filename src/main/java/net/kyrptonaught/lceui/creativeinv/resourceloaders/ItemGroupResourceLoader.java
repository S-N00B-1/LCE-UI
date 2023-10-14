package net.kyrptonaught.lceui.creativeinv.resourceloaders;

import com.google.gson.*;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.creativeinv.CustomItemGroup;
import net.kyrptonaught.lceui.whatsThis.resourceloaders.TagResourceLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ItemGroupResourceLoader implements SimpleSynchronousResourceReloadListener {
    public static final Identifier PARENT_ID = new Identifier(LCEUIMod.MOD_ID, "creative_tabs");
    public static final Identifier ID = new Identifier(PARENT_ID.getNamespace(), PARENT_ID.getPath() + "/tabs");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return Collections.singleton(TagResourceLoader.ID);
    }

    @Override
    public void reload(ResourceManager manager) {
        CustomItemGroup.ITEM_GROUPS.clear();

        try {
            Identifier orderId = new Identifier(PARENT_ID.getNamespace(), PARENT_ID.getPath() + "/creative_tabs.json");
            Resource orderResource = manager.getResourceOrThrow(orderId);

            JsonObject jsonObj = (JsonObject) JsonParser.parseReader(new InputStreamReader(orderResource.getInputStream()));
            JsonArray entries = JsonHelper.getArray(jsonObj, "order");
            for (JsonElement entry : entries) {
                final Identifier entryId = new Identifier(entry.getAsString());
                final Identifier entryIdPath = new Identifier(entryId.getNamespace(), ID.getPath() + "/" + entryId.getPath() + ".json");
                registerGroups(Map.of(entryIdPath, manager.getResourceOrThrow(entryIdPath)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerGroups(Map<Identifier, Resource> resources) {
        for (Identifier id : resources.keySet()) {
            if (id.getNamespace().equals(ID.getNamespace()))
                try {
                    JsonObject jsonObj = (JsonObject) JsonParser.parseReader(new InputStreamReader(resources.get(id).getInputStream()));
                    Identifier groupId = new Identifier(JsonHelper.getString(jsonObj, "id"));
                    JsonArray entries = JsonHelper.getArray(jsonObj, "entries");
                    List<ItemStack> itemStackList = new ArrayList<>();
                    for (JsonElement entry : entries) {
                        if (entry.isJsonObject()) {
                            try {
                                NbtCompound nbtCompound = StringNbtReader.parse(String.valueOf(entry));
                                itemStackList.add(ItemStack.fromNbt(nbtCompound));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Item item = Registry.ITEM.get(new Identifier(entry.getAsString()));
                            itemStackList.add(new ItemStack(item));
                        }
                    }

                    CustomItemGroup.createAndRegister(groupId, itemStackList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
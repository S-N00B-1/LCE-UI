package net.kyrptonaught.lceui.whatsThis.resourceloaders;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.creativeinv.CustomItemGroup;
import net.kyrptonaught.lceui.whatsThis.ItemDescription;
import net.kyrptonaught.lceui.whatsThis.WhatsThisInit;
import net.minecraft.data.dev.NbtProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ItemGroupResourceLoader implements SimpleSynchronousResourceReloadListener {
    public static final Identifier ID = new Identifier(LCEUIMod.MOD_ID, "creative_tabs");
    private static final Gson GSON = (new GsonBuilder()).create();

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
        CustomItemGroup.LCE_ITEM_GROUPS.clear();
        Map<Identifier, Resource> resources = manager.findResources(ID.getPath(), (string) -> string.getPath().endsWith(".json"));
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
                            try {
                                NbtCompound nbtCompound = new NbtCompound();
                                nbtCompound.putString("id", entry.getAsString());
                                nbtCompound.putByte("Count", (byte)1);
                                itemStackList.add(ItemStack.fromNbt(nbtCompound));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    new CustomItemGroup(groupId, itemStackList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
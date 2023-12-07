package net.kyrptonaught.lceui.whatsThis.resourceloaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.whatsThis.ItemDescription;
import net.kyrptonaught.lceui.whatsThis.WhatsThisInit;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class DescriptionResourceLoader implements SimpleSynchronousResourceReloadListener {
    public static final Identifier ID = new Identifier(LCEUIMod.MOD_ID, "descriptions");
    private static final Gson GSON = (new GsonBuilder()).create();

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        WhatsThisInit.descriptionManager.clearDescriptions();
        Map<Identifier, Resource> resources = manager.findResources(ID.getPath(), (string) -> string.getPath().endsWith(".json"));
        for (Identifier id : resources.keySet()) {
            if (id.getNamespace().equals(ID.getNamespace()) && id.getPath().contains("/block") || id.getPath().contains("/item") || id.getPath().contains("/entity"))
                try {
                    JsonObject jsonObj = (JsonObject) JsonParser.parseReader(new InputStreamReader(resources.get(id).getInputStream()));
                    ItemDescription itemDescription = new ItemDescription();
                    if (jsonObj.get("parent") != null) {
                        itemDescription.parent = jsonObj.get("parent").getAsString();
                    }
                    if (jsonObj.get("model") != null) {
                        itemDescription.model = jsonObj.get("model").getAsString();
                    }
                    if (jsonObj.get("group") != null) {
                        itemDescription.group = jsonObj.get("group").getAsString();
                    }
                    if (jsonObj.get("displaysicon") != null) {
                        itemDescription.displaysicon = jsonObj.get("displaysicon").getAsBoolean();
                    }
                    if (jsonObj.getAsJsonObject("text") != null) {
                        if (jsonObj.getAsJsonObject("text").get("name") != null) {
                            itemDescription.text.name = Text.translatable(jsonObj.getAsJsonObject("text").get("name").getAsString());
                        }
                        if (jsonObj.getAsJsonObject("text").get("description") != null) {
                            itemDescription.text.description = Text.translatable(jsonObj.getAsJsonObject("text").get("description").getAsString());
                        }
                    }
                    String fileName = id.getPath().substring(id.getPath().lastIndexOf("/") + 1).replace(".json", "");
                    if (id.getPath().contains("/block"))
                        fileName = "block/" + fileName;
                    else if (id.getPath().contains("/item"))
                        fileName = "item/" + fileName;
                    else if (id.getPath().contains("/entity"))
                        fileName = "entity/" + fileName;

                    Identifier name = new Identifier(fileName);
                    WhatsThisInit.descriptionManager.itemDescriptions.put(name, itemDescription);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
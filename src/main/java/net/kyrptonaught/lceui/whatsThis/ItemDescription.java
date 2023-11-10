package net.kyrptonaught.lceui.whatsThis;

import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

public class ItemDescription {
    public String parent;
    public String model;
    public String group;
    public Boolean displaysicon;

    public TextTranslations text = new TextTranslations();

    public boolean isFieldBlank(MutableText field) {
        return field == null || isFieldBlank(field.getContent().toString());
    }

    public boolean isFieldBlank(String field) {
        return field == null || field.isEmpty() || field.isBlank();
    }

    public Identifier getParent() {
        return new Identifier(parent);
    }

    public void copyFrom(ItemDescription other) {
        if (isFieldBlank(model))
            model = other.model;

        if (isFieldBlank(text.name))
            text.name = other.text.name;

        if (isFieldBlank(text.description))
            text.description = other.text.description;

        if (isFieldBlank(group))
            group = other.group;

        if (displaysicon == null)
            displaysicon = other.displaysicon;
    }

    public ItemDescription copy() {
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.parent = this.parent;
        itemDescription.model = this.model;
        itemDescription.group = this.group;
        itemDescription.displaysicon = this.displaysicon;
        itemDescription.text = new TextTranslations();
        itemDescription.text.name = this.text.name == null ? null : this.text.name.copy();
        itemDescription.text.description = this.text.description == null ? null : this.text.description.copy();
        return itemDescription;
    }

    public static class TextTranslations {
        public MutableText name;
        public MutableText description;
    }
}

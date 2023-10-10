package net.kyrptonaught.lceui.whatsThis;

import net.minecraft.util.Identifier;

public class ViewedBlock {
    Identifier id;
    boolean isTag;

    protected ViewedBlock(Identifier id, boolean isTag) {
        this.id = id;
        this.isTag = isTag;
    }

    public Identifier getId() {
        return id;
    }

    public boolean isTag() {
        return isTag;
    }

    @Override
    public String toString() {
        return (this.isTag ? "#" : "") + this.id.toString();
    }
}

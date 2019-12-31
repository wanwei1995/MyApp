package com.example.myapp.ui.main2.adapter.node.section.entity;

import com.chad.library.adapter.base.entity.node.BaseNode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemNode extends BaseNode {

    private String img;
    private String name;

    public ItemNode(String img, String name) {
        this.img = img;
        this.name = name;
    }

    public ItemNode(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}

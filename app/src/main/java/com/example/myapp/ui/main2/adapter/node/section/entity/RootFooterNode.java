package com.example.myapp.ui.main2.adapter.node.section.entity;

import com.chad.library.adapter.base.entity.node.BaseNode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootFooterNode extends BaseNode {

    private String title;

    public RootFooterNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}

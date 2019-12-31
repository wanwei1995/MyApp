package com.example.myapp.ui.main2.adapter.node.section;


import android.content.Context;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.example.myapp.ui.main2.adapter.node.section.entity.ItemNode;
import com.example.myapp.ui.main2.adapter.node.section.entity.RootFooterNode;
import com.example.myapp.ui.main2.adapter.node.section.entity.RootNode;
import com.example.myapp.ui.main2.adapter.node.section.provider.RootFooterNodeProvider;
import com.example.myapp.ui.main2.adapter.node.section.provider.RootNodeProvider;
import com.example.myapp.ui.main2.adapter.node.section.provider.SecondNodeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NodeSectionAdapter extends BaseNodeAdapter {

    public NodeSectionAdapter(Context context) {
        super();
        addFullSpanNodeProvider(new RootNodeProvider());
        addNodeProvider(new SecondNodeProvider(context));
        addFooterNodeProvider(new RootFooterNodeProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof RootNode) {
            return 0;
        } else if (node instanceof ItemNode) {
            return 1;
        } else if (node instanceof RootFooterNode) {
            return 2;
        }
        return -1;
    }
}

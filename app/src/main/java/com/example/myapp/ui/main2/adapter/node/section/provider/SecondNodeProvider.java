package com.example.myapp.ui.main2.adapter.node.section.provider;

import android.view.View;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myapp.R;
import com.example.myapp.ui.main2.adapter.node.section.entity.ItemNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SecondNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_section_content;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        if (data == null) {
            return;
        }

        ItemNode entity = (ItemNode) data;
        helper.setImageResource(R.id.iv, entity.getImg());
    }


    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
    }
}

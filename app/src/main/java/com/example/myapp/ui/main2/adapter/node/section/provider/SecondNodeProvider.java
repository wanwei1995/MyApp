package com.example.myapp.ui.main2.adapter.node.section.provider;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myapp.R;
import com.example.myapp.ui.main2.adapter.node.section.entity.ItemNode;
import com.example.myapp.util.GlideUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SecondNodeProvider extends BaseNodeProvider {

    private Context context;

    public SecondNodeProvider(Context context){
        this.context = context;
    }

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

        ItemNode itemNode = (ItemNode) data;

        Glide.with(context)
                .load(itemNode.getImg())
                .apply(GlideUtil.getCrop())
                .into((ImageView)helper.getView(R.id.iv));
    }


    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
    }
}

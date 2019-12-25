package com.example.myapp.ui.main2.adapter.node.section.provider;

import android.view.View;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myapp.R;
import com.example.myapp.ui.main2.adapter.node.section.entity.RootFooterNode;
import com.example.myapp.util.ToastUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RootFooterNodeProvider extends BaseNodeProvider {

    public RootFooterNodeProvider() {
        addChildClickViewIds(R.id.footerTv);
    }

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.node_footer;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        RootFooterNode entity = (RootFooterNode) data;
        helper.setText(R.id.footerTv, entity.getTitle());
    }

    @Override
    public void onChildClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        if (view.getId() == R.id.footerTv) {
            ToastUtils.show("Footer Node Click : " + position);
        }
    }
}

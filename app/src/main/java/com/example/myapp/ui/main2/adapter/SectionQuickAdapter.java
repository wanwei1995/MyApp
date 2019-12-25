/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.myapp.ui.main2.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Bakumon https://bakumon.me
 */
public abstract class SectionQuickAdapter extends BaseSectionQuickAdapter<MySectionEntity, BaseViewHolder> {



    public SectionQuickAdapter(int layoutResId, int sectionHeadResId, List<MySectionEntity> data) {
        super(sectionHeadResId, data);
        // 设置普通item布局（如果item类型只有一种，使用此方法）
        setNormalLayout(layoutResId);
      /*  // 注册需要点击的子view id
        addChildClickViewIds(R.id.more);*/
    }

    /**
     * 设置header数据
     */
    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @Nullable MySectionEntity item) {
        if (item != null && item.getObject() instanceof String) {
            reConvertHeader(helper,item);
        }
    }

    public abstract void reConvertHeader(BaseViewHolder helper, MySectionEntity item);

    public abstract void reConvertItem(BaseViewHolder helper, MySectionEntity item);

    @Override
    protected void convert(BaseViewHolder helper, MySectionEntity item) {
        reConvertItem(helper,item);
    }
}

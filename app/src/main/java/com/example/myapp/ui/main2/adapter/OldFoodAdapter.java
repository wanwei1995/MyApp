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

import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapp.R;
import com.example.myapp.ui.main2.Fragment.setting.SettingSectionEntity;
import com.example.myapp.util.GlideUtil;

import java.util.List;

/**
 * @author Bakumon https://bakumon.me
 */
public class OldFoodAdapter extends BaseSectionQuickAdapter<OldFoodSectionEntity, BaseViewHolder> {

    public OldFoodAdapter(List<OldFoodSectionEntity> data) {
        super(R.layout.item_layout_foodbook_small, R.layout.item_setting_head, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, OldFoodSectionEntity item) {
        helper.setText(R.id.tv_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, OldFoodSectionEntity item) {
        helper.setText(R.id.food_title, item.t.foodBook.getName())
                .setText(R.id.food_introduction, item.t.foodBook.getIntroduction())
                .addOnClickListener(R.id.switch_item);
        Glide.with(mContext).load(item.t.foodBook.getPicUrl())
                .apply(GlideUtil.getCrop()).into((ImageView) helper.getView(R.id.food_pic));
    }
}

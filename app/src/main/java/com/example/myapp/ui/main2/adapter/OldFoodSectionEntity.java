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

import com.chad.library.adapter.base.entity.SectionEntity;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;

/**
 * 分组布局实体
 *
 * @author Bakumon https://bakumon.me
 */
public class OldFoodSectionEntity extends SectionEntity<OldFoodSectionEntity.Item> {
    public OldFoodSectionEntity(String header) {
        super(true, header);
    }

    public OldFoodSectionEntity(Item item) {
        super(item);
    }

    public static class Item {
        public Item(FoodBook foodBook) {
            this.foodBook = foodBook;
        }
        public FoodBook foodBook;
    }
}

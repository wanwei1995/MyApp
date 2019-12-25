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

import com.chad.library.adapter.base.entity.JSectionEntity;

/**
 * 分组布局实体
 *
 * @author Bakumon https://bakumon.me
 */
public class MySectionEntity extends JSectionEntity{

    // 你的数据内容
    private boolean isHeader;
    private Object object;

    public MySectionEntity(boolean isHeader, Object object) {
        this.isHeader = isHeader;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    /**
     * 重写此方法，返回 boolen 值，告知是否是header
     */
    @Override
    public boolean isHeader() {
        return isHeader;
    }
}

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

package com.example.myapp.datebase;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.myapp.AppContext;
import com.example.myapp.datebase.converters.Converters;
import com.example.myapp.datebase.dao.FoodBookDao;
import com.example.myapp.datebase.dao.MyFoodBookDao;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;

/**
 * 数据库
 *
 * @author Bakumon https:bakumon.me
 */
@Database(entities = {FoodBook.class, MyFoodBook.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "MyApp.db";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(AppContext.getINSTANCE(),
                            AppDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取菜谱类
     *
     * @return RecordTypeDao 菜谱类
     */
    public abstract FoodBookDao foodBookDao();

    /**
     * 获取选中菜谱类
     *
     * @return RecordDao 选中菜谱类
     */
    public abstract MyFoodBookDao myFoodBookDao();

}

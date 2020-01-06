package com.example.myapp.util;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;

public class TableUtil {

    //基础参数设置(适合排版紧凑的)
    public static void showList(SmartTable mTable, int contentSize, int titleSize, int ColumnTitleHorizontalPadding) {
        mTable.getConfig().setHorizontalPadding(10);
        mTable.getConfig().setVerticalPadding(5);
        mTable.getConfig().setSequenceHorizontalPadding(4);
        mTable.getConfig().setColumnTitleVerticalPadding(5);
        mTable.getConfig().setSequenceHorizontalPadding(50);
        showListDefault(mTable,contentSize,titleSize,ColumnTitleHorizontalPadding);
    }



    //不修改间距等默认参数(适合排版稀松的)
    public static void showListDefault(SmartTable mTable, int contentSize, int titleSize, int ColumnTitleHorizontalPadding) {
        mTable.getConfig().setHorizontalPadding(10);
        mTable.getConfig().setShowTableTitle(false);
        mTable.getConfig().setShowXSequence(false);
        mTable.getConfig().setShowYSequence(false);
        mTable.setZoom(false);
        if(contentSize != 0){
            FontStyle fontStyle = new FontStyle();
            fontStyle.setTextSize(contentSize);
            mTable.getConfig().setContentStyle(fontStyle);
        }
        if(titleSize != 0){
            FontStyle fontStyle = new FontStyle();
            fontStyle.setTextSize(titleSize);
            mTable.getConfig().setColumnTitleStyle(fontStyle);
        }
        mTable.getConfig().setColumnTitleHorizontalPadding(ColumnTitleHorizontalPadding);
    }

    //基础参数设置(设置高度)
    public static void showListVer(SmartTable mTable, int contentSize, int titleSize, int ColumnTitleHorizontalPadding, int verticalPadding) {
        mTable.getConfig().setHorizontalPadding(10);
        mTable.getConfig().setVerticalPadding(verticalPadding);
        mTable.getConfig().setSequenceHorizontalPadding(4);
        mTable.getConfig().setColumnTitleVerticalPadding(5);
        mTable.getConfig().setSequenceHorizontalPadding(50);
        showListDefault(mTable,contentSize,titleSize,ColumnTitleHorizontalPadding);
    }


    //  (设置宽度)
    public static void showListHor(SmartTable mTable, int contentSize, int titleSize, int horizontalPadding, int ColumnTitleHorizontalPadding) {
        mTable.getConfig().setHorizontalPadding(horizontalPadding);
        mTable.getConfig().setShowTableTitle(false);
        mTable.getConfig().setShowXSequence(false);
        mTable.getConfig().setShowYSequence(false);
        mTable.setZoom(false);
        if(contentSize != 0){
            FontStyle fontStyle = new FontStyle();
            fontStyle.setTextSize(contentSize);
            mTable.getConfig().setContentStyle(fontStyle);
        }
        if(titleSize != 0){
            FontStyle fontStyle = new FontStyle();
            fontStyle.setTextSize(titleSize);
            mTable.getConfig().setColumnTitleStyle(fontStyle);
        }
        mTable.getConfig().setColumnTitleHorizontalPadding(ColumnTitleHorizontalPadding);
    }
}

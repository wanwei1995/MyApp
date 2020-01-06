package com.example.myapp.ui.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.ui.main.dto.HanZiDto;
import com.example.myapp.util.HanZiToPinYin;
import com.example.myapp.util.StringUtil;
import com.example.myapp.util.TableUtil;

import java.util.ArrayList;
import java.util.List;

public class HanZiActivity extends BaseActivity {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.list)
    SmartTable list;

    List<HanZiDto> hanZiDtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_han_zi);

        //
        getData();

        showTable(hanZiDtos);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //变化前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化中
                String text = s.toString();
                if (StringUtil.isEmpty(text)) {
                    //输入为空，则显示所有结果
                    list.setTableData(getTableData(hanZiDtos));
                    return;
                }
                List<HanZiDto> hanZiDtoNew = new ArrayList<>();
                for (HanZiDto hanZiDto : hanZiDtos) {
                    if (hanZiDto.getValue().contains(text)) {
                        hanZiDtoNew.add(hanZiDto);
                    }
                }
                list.setTableData(getTableData(hanZiDtoNew));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //变化后
            }
        });

    }

    private TableData<HanZiDto> getTableData(List<HanZiDto> hanZiDtoList) {
        final Column<String> t1Column = new Column<>("中文", "text");
        t1Column.setWidth(200);
        final Column<String> t2Column = new Column<>("英文", "value");
        t2Column.setWidth(200);
        return new TableData<>("", hanZiDtoList, t1Column, t2Column);
    }

    private void showTable(List<HanZiDto> hanZiDtoList) {
        list.setTableData(getTableData(hanZiDtoList));
        TableUtil.showListDefault(list, 18, 20, 15);
    }

    private void getData() {
        hanZiDtos = new ArrayList<>();

        hanZiDtos.add(new HanZiDto("悟了长生理"));

        hanZiDtos.add(new HanZiDto("秋莲处处开"));

        hanZiDtos.add(new HanZiDto("金童登锦帐"));

        hanZiDtos.add(new HanZiDto("玉女下香阶"));

        hanZiDtos.add(new HanZiDto("虎啸天魂住"));

        hanZiDtos.add(new HanZiDto("龙吟地魄来"));

        hanZiDtos.add(new HanZiDto("有人明此道"));

        hanZiDtos.add(new HanZiDto("立使返婴孩"));

        hanZiDtos.add(new HanZiDto("姹女住南方"));

        hanZiDtos.add(new HanZiDto("身边产太阳"));

        hanZiDtos.add(new HanZiDto("蟾宫烹玉液"));

        hanZiDtos.add(new HanZiDto("坎户炼琼浆"));

        hanZiDtos.add(new HanZiDto("过去神仙饵"));

        hanZiDtos.add(new HanZiDto("今来到我尝"));

        hanZiDtos.add(new HanZiDto("一杯延万纪"));

        hanZiDtos.add(new HanZiDto("物外任翱翔"));

        hanZiDtos.add(new HanZiDto("测试ce"));

        hanZiDtos.add(new HanZiDto("测试,^#$%@@&$*&**(#"));

        //将所有汉字转化为拼音

        for (HanZiDto hanZiDto : hanZiDtos) {
            hanZiDto.setValue(HanZiToPinYin.getFirstSpell(hanZiDto.getText()));
        }

    }


}

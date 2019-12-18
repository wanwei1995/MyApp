package com.example.myapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.disposables.CompositeDisposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {

    Unbinder unbinder;

    public FragmentActivity mContext;

    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    public void butterKnife(View view){
        unbinder = ButterKnife.bind(this, view);
    }

    public List<MenuDto> loadMenus(int id) {
        InputStreamReader inputReader = new InputStreamReader(getResources().openRawResource(id));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line = "";
        String jsonStr = "";
        List<MenuDto> menuDTOs = new ArrayList<>();
        try {
            while ((line = bufReader.readLine()) != null)
                jsonStr += line;
            menuDTOs = new Gson().fromJson(jsonStr, new TypeToken<List<MenuDto>>() {
            }.getType());
        } catch (IOException e) {
            Log.e("normal", "parseJson", e);

            Toast.makeText(mContext, "Render Main Menu Eror", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                inputReader.close();
                bufReader.close();
            } catch (IOException e) {
                Log.e("normal", "parseJson", e);

                Toast.makeText(mContext, "Render Main Menu Eror", Toast.LENGTH_SHORT).show();
            }
        }
        return menuDTOs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}

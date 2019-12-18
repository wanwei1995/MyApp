package com.example.myapp.ui.main2.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.common.Router;
import com.example.myapp.datebase.entity.BackupBean;
import com.example.myapp.myView.BackupFliesDialog;
import com.example.myapp.ui.main2.Fragment.setting.SettingAdapter;
import com.example.myapp.ui.main2.Fragment.setting.SettingSectionEntity;
import com.example.myapp.util.BackupUtil;
import com.example.myapp.util.ToastUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.floo.Floo;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends BaseFragment {


    @BindView(R.id.setting_list)
    RecyclerView settingList;

    private SettingAdapter mAdapter;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        butterKnife(view);

        mAdapter = new SettingAdapter(null);

        List<SettingSectionEntity> list = new ArrayList<>();
        list.add(new SettingSectionEntity(getString(R.string.text_setting_backup)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_go_backup), getString(R.string.text_setting_go_backup_content))));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_setting_restore), getString(R.string.text_setting_restore_content))));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_setting_auto_backup), getString(R.string.text_setting_auto_backup_content))));
        mAdapter.setNewData(list);


        mAdapter.setOnItemClickListener((adapter1, view1, position) -> {
            switch (position) {
                case 1:
                    //手动备份
                    showBackupDialog();
                    break;
                case 2:
                    //读取备份
                    showRestoreDialog();
                    break;
                case 3:
                    //自动备份
                    break;
                default:
                    break;
            }
        });

        settingList.setAdapter(mAdapter);
        settingList.setLayoutManager(new LinearLayoutManager(mContext));
        return view;
    }

    private void showRestoreDialog() {
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            restore();
            return;
        }
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 13, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setRationale(R.string.text_storage_content)
                        .setPositiveButtonText(R.string.text_affirm)
                        .setNegativeButtonText(R.string.text_button_cancel)
                        .build());
    }

    private void restore() {
        Flowable<List<BackupBean>> listFlowable = Flowable.create(e -> {
            e.onNext(BackupUtil.getBackupFiles());
            e.onComplete();
        }, BackpressureStrategy.BUFFER);

        mDisposable.add(listFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(backupBeans -> {
                            BackupFliesDialog dialog = new BackupFliesDialog(mContext, backupBeans);
                            dialog.setOnItemClickListener(file -> restoreDB(file.getPath()));
                            dialog.show();
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_backup_list_fail);
                        }));
    }


    private void restoreDB(String restoreFile) {
        mDisposable.add(Completable.create(e -> {
            boolean result = BackupUtil.restoreDB(restoreFile);
            if (result) {
                e.onComplete();
            } else {
                e.onError(new Exception());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Floo.stack(mContext)
                                .target(Router.IndexKey.INDEX_KEY_HOME)
                                .result("refresh")
                                .start(),
                        throwable -> {
                            ToastUtils.show(R.string.toast_restore_fail);
                        }));
    }


    private void showBackupDialog() {
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            backupDB();
            return;
        }
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 12, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setRationale(R.string.text_storage_content)
                        .setPositiveButtonText(R.string.text_affirm)
                        .setNegativeButtonText(R.string.text_button_cancel)
                        .build());
    }

    private void backupDB() {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.text_backup)
                .setMessage(R.string.text_backup_save)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_affirm, (dialog, which) -> {
                    mDisposable.add(Completable.create(e -> {
                        boolean result = BackupUtil.userBackup();
                        if (result) {
                            e.onComplete();
                        } else {
                            e.onError(new Exception());
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> ToastUtils.show(R.string.toast_backup_success),
                                    throwable -> {
                                        ToastUtils.show(R.string.toast_backup_fail);
                                    }));
                })
                .create()
                .show();
    }


}

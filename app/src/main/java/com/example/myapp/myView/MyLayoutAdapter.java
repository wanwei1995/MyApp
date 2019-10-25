package com.example.myapp.myView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapp.R;
import com.example.myapp.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/9/22 0022.
 */
public abstract class MyLayoutAdapter<T> extends RecyclerView.Adapter<MyLayoutAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<T> mData;
    private int mLayoutRes;           //布局id
    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    public MyLayoutAdapter(List<T> mData, int mLayoutRes) {
        this.mData = mData;
        this.mLayoutRes = mLayoutRes;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.position = position;
        reBindViewHolder(holder,position,mData);
    }

    //将View的点击事件 转为此点击事件
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    //将View的点击事件 转为此点击事件
    public interface OnItemLongClickListener {
        void onItemLongClick(View view , int position);
    }

    //暴露给外面的监听方法
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    //暴露给外面的监听方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemLongClickListener.onItemLongClick(v,((ViewHolder)v.getTag()).position);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,((ViewHolder)v.getTag()).position);
        }
    }

    public abstract void reBindViewHolder(ViewHolder holder, int position,List<T> mData);

    //添加一个元素
    public void add(T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyItemInserted(mData.size());
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void add(int position, T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(position, data);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(int position) {
        if (mData != null) {
            mData.remove(position);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void clear() {
        int count = mData.size();
        if (mData != null) {
            mData.clear();
        }
        notifyItemRangeRemoved(0,count);
        notifyDataSetChanged();
    }

    public void reLoad(List data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        //将创建的View注册点击事件
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;   //存储ListView 的 item中的View
        private View item;                  //存放convertView
        private int position;

        //构造方法，完成相关初始化
        private ViewHolder(View v) {
            super(v);
            mViews = new SparseArray<>();
            v.setTag(this);
            item = v;
        }

        //绑定ViewHolder与item
        public static ViewHolder bind(View v) {
            ViewHolder holder;
            if (v == null) {
                holder = new ViewHolder(v);
            } else {
                holder = (ViewHolder) v.getTag();
                holder.item = v;
            }
            return holder;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            T t = (T) mViews.get(id);
            if (t == null) {
                t = (T) item.findViewById(id);
                mViews.put(id, t);
            }
            return t;
        }


        /**
         * 获取当前条目
         */
        public View getItemView() {
            return item;
        }

        /**
         * 设置文字
         */
        public ViewHolder setText(int id, CharSequence text) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageResource(int id, int drawableRes) {
            if(drawableRes == 0){
                drawableRes = R.mipmap.icon_tool;
            }
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageBackground(int id, Drawable drawable) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setBackground(drawable);
            }
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageBitmap(int id, Bitmap bitmap) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(bitmap);
            }
            return this;
        }


        public ViewHolder setImageBitmap(@NonNull Activity activity, int id, String url,RequestOptions options) {
            View view = getView(id);
            if (view instanceof ImageView) {
                if(options == null){
                    options = GlideUtil.getCircle();
                }
                Glide.with(activity)
                        .load(url)
                        .apply(options)
                        .into((ImageView)view);
            }
            return this;
        }

        public ViewHolder setImageBitmap(@NonNull Activity activity, int id, String url, RequestOptions options, View.OnClickListener listener) {
            View view = getView(id);
            ((ImageView)view).setOnClickListener(listener);
            if (view instanceof ImageView) {
                if(options == null){
                    options = GlideUtil.getCircle();
                }
                Glide.with(activity)
                        .load(url)
                        .apply(options)
                        .into((ImageView)view);
            }
            return this;
        }

        public ViewHolder setImageBitmap(@NonNull Activity activity, int id, String url) {
            View view = getView(id);
            if (view instanceof ImageView) {
                Glide.with(activity)
                        .load(url)
                        .into((ImageView)view);
            }
            return this;
        }




        /**
         * 设置点击监听
         */
        public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
            return this;
        }

        /**
         * 设置可见
         */
        public ViewHolder setVisibility(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        /**
         * 设置标签
         */
        public ViewHolder setTag(int id, Object obj) {
            getView(id).setTag(obj);
            return this;
        }

        //其他方法可自行扩展

    }

}

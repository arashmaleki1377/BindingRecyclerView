package com.arashmaleki.bindingrecycleradapter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import com.arashmaleki.bindingrecycleradapter.animations.ItemAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BindingRecyclerAdapter<Binding extends ViewDataBinding, Model> extends RecyclerView.Adapter<MyViewHolder<Binding>> {

    private final List<Model> items = new ArrayList<>();
    private List<Integer> onClickIds;
    private OnItemClickListener<Binding, Model> mOnItemClickListener;
    private BindViewHolderListener<Binding, Model> bindViewHolderListener;
    private LayoutInflater layoutInflater;
    private final int layout;
    private int emptyLayout = -1;
    private final int variableId;
    private int animationType = ItemAnimation.BOTTOM_UP;
    private boolean isSubmitList = false;
    private static final int VIEW_TYPE_MAIN = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private boolean isShowEmptyView = false;
    private boolean isEndlessScroll = false;

    public BindingRecyclerAdapter(int layout, int variableId) {
        this.layout = layout;
        this.variableId = variableId;
    }

    public BindingRecyclerAdapter(int layout, int variableId, int animationType) {
        this.layout = layout;
        this.variableId = variableId;
        this.animationType = animationType;
    }

    public BindingRecyclerAdapter(int layout, int variableId, boolean isShowEmptyView) {
        this.layout = layout;
        this.variableId = variableId;
        this.isShowEmptyView = isShowEmptyView;
    }

    public BindingRecyclerAdapter(int layout, int variableId, int animationType, boolean isShowEmptyView) {
        this.layout = layout;
        this.variableId = variableId;
        this.animationType = animationType;
        this.isShowEmptyView = isShowEmptyView;
    }

    public void setOnItemClickListener(List<Integer> onClickIds, final OnItemClickListener<Binding, Model> mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
        this.onClickIds = onClickIds;
    }

    public void setOnBindViewHolderListener(final BindViewHolderListener<Binding, Model> bindViewHolder) {
        this.bindViewHolderListener = bindViewHolder;
    }

    public void setEmptyLayout(@LayoutRes int emptyLayout) {
        this.emptyLayout = emptyLayout;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }


    public void setSubmitList(boolean submitList) {
        isSubmitList = submitList;
    }

    public void addItems(List<Model> items) {
        final int size = this.items.size() + 1;
        this.items.addAll(items);
        notifyItemRangeInserted(size, items.size());
        isSubmitList = true;
    }

    public void addItem(Model item) {
        this.items.add(item);
        notifyDataSetChanged();
        isSubmitList = true;
    }

    public void setItems(List<Model> items) {
        removeAllItem();
        this.items.addAll(items);
        notifyDataSetChanged();
        isSubmitList = true;
    }

    public void removeItem(Model item) {
        this.items.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItem() {
        if (items.isEmpty())
            return;
        this.items.clear();
        notifyDataSetChanged();
    }

    public List<Model> getList() {
        return items;
    }


    @NonNull
    @Override
    public MyViewHolder<Binding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_MAIN) {
            return new MyViewHolder<>(DataBindingUtil.inflate(layoutInflater, layout, parent, false));
        } else if (viewType == VIEW_TYPE_EMPTY) {
            return new MyViewHolder<>(DataBindingUtil.inflate(layoutInflater, emptyLayout, parent, false));
        }
        return new MyViewHolder<>(DataBindingUtil.inflate(layoutInflater, layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_EMPTY) {
            return;
        }
        holder.binding.setVariable(variableId, items.get(position));

        if (bindViewHolderListener != null)
            bindViewHolderListener.OnBindView(holder.itemView, (Binding) holder.binding, items.get(position), position);

        setAnimation(holder.itemView, position);

        setOnItemClickListener(holder.binding.getRoot(), (Binding) holder.binding, position);

    }

    private void setOnItemClickListener(View view, Binding viewDataBinding, int position) {
        if (onClickIds == null || mOnItemClickListener == null)
            return;
        for (int i : onClickIds) {
            if (view.findViewById(i) != null)
                view.findViewById(i).setOnClickListener(v -> mOnItemClickListener.onItemClick(v, viewDataBinding, position, items.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        if (isSubmitList && items.size() == 0)
            if (isShowEmptyView)
                return 1;
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0)
            if (isShowEmptyView)
                return VIEW_TYPE_EMPTY;
        return VIEW_TYPE_MAIN;
    }

    @Override
    public long getItemId(int position) {
        return position * new Random().nextLong(); // id
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animationType);
            lastPosition = position;
        }
    }


    public interface BindViewHolderListener<Binding extends ViewDataBinding, Model> {
        void OnBindView(View view, Binding binding, Model item, int position);
    }


    public interface OnItemClickListener<Binding, Model> {

        void onItemClick(View view, Binding binding, int position, Model model);

        default void onItemLongClick(View view, int position, Model model) {
        }
    }

}

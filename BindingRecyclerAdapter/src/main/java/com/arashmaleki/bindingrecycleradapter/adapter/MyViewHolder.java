package com.arashmaleki.bindingrecycleradapter.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected Binding binding;

    public MyViewHolder(@NonNull Binding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
}

package com.arashmaleki.bindingrecycleradapter.interfaces;

import android.view.View;

public interface OnItemClickListener<Binding, Model> {

    void onItemClick(View view, Binding viewHolderBinding, int position, Model model);

    default void onItemLongClick(View view, int position, Model model) {
    }
}
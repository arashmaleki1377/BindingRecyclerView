package com.arashmaleki.bindingrecycleradapter.interfaces;

import android.view.View;

import androidx.databinding.ViewDataBinding;

public interface BindViewHolderListener<Binding extends ViewDataBinding, Model> {
    void OnBindView(View view, Binding binding, Model item, int position);
}

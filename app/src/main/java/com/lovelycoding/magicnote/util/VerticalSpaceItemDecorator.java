package com.lovelycoding.magicnote.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {
    private final int varticalSpaceHeight;

    public VerticalSpaceItemDecorator(int varticalSpaceHeight) {
        this.varticalSpaceHeight = varticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.bottom=varticalSpaceHeight;
    }
}

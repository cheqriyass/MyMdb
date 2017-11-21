package com.example.yassine.mymdb.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yassine.mymdb.MovieListActiviy;
import com.example.yassine.mymdb.R;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (MovieListActiviy.layout == 2)
            return;
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int sides = 120;
        int topBottom = 15;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left + sides, top - topBottom, right - sides, bottom -topBottom);
            mDivider.draw(c);
        }
    }
}
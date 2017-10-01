package com.mobisquid.mobicash.wedget.drawer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mobisquid.mobicash.R;

/**
 * Created by andrew on 6/24/17.
 */

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private int verticalSpaceHeight;

    public SimpleDividerItemDecoration(Context context,int verticalSpaceHeight) {
        mDivider = ContextCompat.getDrawable(context,R.drawable.line_divider);
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        this.verticalSpaceHeight = verticalSpaceHeight;
        styledAttributes.recycle();

    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        //outRect.bottom = verticalSpaceHeight;
       // outRect.top = verticalSpaceHeight;
    }
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
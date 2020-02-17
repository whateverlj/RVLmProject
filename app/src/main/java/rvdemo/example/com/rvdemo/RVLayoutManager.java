package rvdemo.example.com.rvdemo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class RVLayoutManager extends RecyclerView.LayoutManager {
    private final String TAG = RVLayoutManager.class.getSimpleName();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "onLayoutChildren ");
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        //state.isPreLayout()是支持动画的
        if (getItemCount() == 0 && state.isPreLayout()) {
            return;
        }
        //将当前Recycler中的view全部移除并放到报废缓存里,之后优先重用缓存里的view
        detachAndScrapAttachedViews(recycler);

        for (int i = 0; i < getItemCount(); i++) {
            View scrap = recycler.getViewForPosition(i);
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            int width = getDecoratedMeasuredWidth(scrap);
            int height = getDecoratedMeasuredHeight(scrap);
            layoutDecoratedWithMargins(scrap, i * width, 0, (i + 1) * width, height);
            scaleItemView(recycler, scrap);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //界面向下滚动的时候,dy为正,向上滚动的时候dy为负
        //填充
        fill(dx, recycler, state);
        //滚动
        offsetChildrenHorizontal(dx * -1);
        //回收已经离开界面的
        recycleOut(dx, recycler, state);
        return dx;
    }


    private void fill(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //向左滚动
        if (dy > 0) {
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView == null) {
                return;
            }
            int lastPos = getPosition(lastView);
            if (lastView.getRight() < getWidth()) {
                View scrap;
                Log.e("lastPos", lastPos + "");
                if (lastPos == getItemCount() - 1) {
                    scrap = recycler.getViewForPosition(0);
                } else {
                    scrap = recycler.getViewForPosition(lastPos + 1);
                }
                if (scrap == null) {
                    return;
                }
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                addView(scrap);
                removeAndRecycleViewAt(0, recycler);
                layoutDecoratedWithMargins(scrap, lastView.getRight(), 0, lastView.getRight() + width, height);
            }
        } else {
            //向右滚动
            View firstView = getChildAt(0);
            int layoutPostion = getPosition(firstView);

            if (firstView.getLeft() >= 0) {
                View scrap;
                if (layoutPostion == 0) {
                    scrap = recycler.getViewForPosition(getItemCount() - 1);
                } else {
                    scrap = recycler.getViewForPosition(layoutPostion - 1);
                }
                addView(scrap, 0);
                removeAndRecycleViewAt(getChildCount() - 1, recycler);
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                layoutDecoratedWithMargins(scrap, firstView.getLeft() - width, 0, firstView.getLeft(), height);
            }
        }
        scaleItemAllView(recycler);
    }

    private void scaleItemAllView(RecyclerView.Recycler recycler) {
        // 缩放子view
        View item;
        for (int i = 0; i < getItemCount(); i++) {
            item = getChildAt(i);
            scaleItemView(recycler, item);
        }
    }

    private void scaleItemView(RecyclerView.Recycler recycler, View item) {
        if (item == null) {
            return;
        }
        // 缩放子view
        final float minScale = 0.5f;
        float parentCenterX = getWidth() / 2;
        float childCenterX = 0f;
        float currentScale = 0f;
        float distanceX = 0f;
        childCenterX = (getDecoratedLeft(item) + getDecoratedRight(item)) / 2;
        Log.e("childCenterX1", childCenterX + "");
        if (childCenterX < parentCenterX) {
            distanceX = (parentCenterX - childCenterX) / (getItemCount() * getDecoratedMeasuredWidth(item) / 2f);
        } else {
            distanceX = (childCenterX - parentCenterX) / (getItemCount() * getDecoratedMeasuredWidth(item) / 2f);
        }
        currentScale = Math.abs(1.0f - (1.0f - minScale) * distanceX);
        Log.e("childCenterX2", currentScale + "");
        item.setScaleX(currentScale);
        item.setScaleY(currentScale);
    }

    private void recycleOut(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (dy > 0) {
                if (view.getBottom() - dy < 0) {
                    removeAndRecycleView(view, recycler);
                }
            } else {
                if (view.getTop() - dy > getHeight()) {
                    removeAndRecycleView(view, recycler);
                }
            }
        }
    }
}

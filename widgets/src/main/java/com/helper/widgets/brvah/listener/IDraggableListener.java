package com.helper.widgets.brvah.listener;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 抽取拖拽功能
 */
public interface IDraggableListener {


    /**
     * @return boolean
     */
    boolean isItemSwipeEnable();

    /**
     * @return boolean
     */
    boolean isItemDraggable();

    /**
     * Is there a toggle view which will trigger drag event.
     *
     * @return boolean
     */
    boolean hasToggleView();

    /**
     * @param viewHolder viewHolder
     */
    void onItemDragStart(RecyclerView.ViewHolder viewHolder);

    /**
     * @param viewHolder viewHolder
     */
    void onItemDragEnd(RecyclerView.ViewHolder viewHolder);

    /**
     * @param viewHolder viewHolder
     */
    void onItemSwipeClear(RecyclerView.ViewHolder viewHolder);

    /**
     * @param source source
     * @param target target
     */
    void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

    /**
     * @param viewHolder viewHolder
     */
    void onItemSwiped(RecyclerView.ViewHolder viewHolder);

    /**
     * @param canvas            canvas
     * @param viewHolder        viewHolder
     * @param x                 x
     * @param y                 y
     * @param isCurrentlyActive isCurrentlyActive
     */
    void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float x, float y, boolean isCurrentlyActive);


    /**
     * @param viewHolder viewHolder
     */
    void onItemSwipeStart(RecyclerView.ViewHolder viewHolder);

}





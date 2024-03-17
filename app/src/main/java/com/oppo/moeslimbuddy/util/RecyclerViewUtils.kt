package com.oppo.moeslimbuddy.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.oppo.moeslimbuddy.util.ConvertUtils.dp

object RecyclerViewUtils {

    class ItemDecoration(
        private val paddingTop: Int = 0,
        private val paddingBottom: Int = 0,
        private val paddingSpace: Int = 0
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.top += paddingTop.dp
                }

                parent.adapter?.itemCount?.minus(1) ?: 0 -> {
                    outRect.top += paddingSpace.dp
                    outRect.bottom += paddingBottom.dp
                }

                else -> {
                    outRect.top += paddingSpace.dp
                }
            }
        }
    }

    class ItemDecorationHorizontal(
        private val paddingLeft: Int = 0,
        private val paddingRight: Int = 0,
        private val paddingSpace: Int = 0
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.left += paddingLeft.dp
                }

                parent.adapter?.itemCount?.minus(1) ?: 0 -> {
                    outRect.left += paddingSpace.dp
                    outRect.right += paddingRight.dp
                }

                else -> {
                    outRect.left += paddingSpace.dp
                }
            }
        }
    }

    class SimpleItemDecoration(
        private val paddingTop: Int = 0,
        private val paddingBottom: Int = 0,
        private val paddingLeft: Int = 0,
        private val paddingRight: Int = 0,
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.top = paddingTop.dp
            outRect.bottom = paddingBottom.dp
            outRect.left = paddingLeft.dp
            outRect.right = paddingRight.dp
        }
    }
}
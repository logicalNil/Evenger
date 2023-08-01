package com.nilay.evenger.utils

import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.onScrollChange(
    crossinline topScroll: (RecyclerView) -> Unit = {},
    crossinline bottomScroll: (RecyclerView) -> Unit = {}
) = this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) bottomScroll.invoke(recyclerView)
        else if (dy < 0) topScroll.invoke(recyclerView)
    }
})


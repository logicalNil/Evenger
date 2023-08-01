package com.nilay.evenger.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun LifecycleOwner.launchWhenStarted(block: suspend () -> Unit) = this.run {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun LifecycleOwner.launchWhenResumed(block: suspend () -> Unit) = this.run {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

fun LifecycleOwner.launchWhenCreated(block: suspend () -> Unit) = this.run {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

fun LifecycleOwner.launch(block: suspend () -> Unit) = this.run {
    lifecycleScope.launch {
        block()
    }
}

fun <T> runOnUiThread(block: () -> T) = runBlocking(Dispatchers.Main) {
    block()
}
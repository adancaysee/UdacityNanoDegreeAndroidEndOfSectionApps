package com.udacity.asteroidradar.util

import android.content.Context
import coil.imageLoader
import coil.util.CoilUtils

fun clearCache(context: Context, memory: Boolean = true, file: Boolean= true) {
    if (memory) {
        val imageLoader = context.imageLoader
        imageLoader.memoryCache.clear()
    }
    if (file) {
        val cache = CoilUtils.createDefaultCache(context)
        cache.directory().deleteRecursively()
    }
}
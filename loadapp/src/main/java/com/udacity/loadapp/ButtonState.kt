package com.udacity.loadapp


sealed class ButtonState {
    object None : ButtonState()
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}
package redl.bil.vblin.logic

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import redl.bil.vblin.R

fun animClickView(view: View, context: Context){

    (AnimatorInflater.loadAnimator(
        context,
        R.animator.animpulse
    ) as AnimatorSet).apply {
        setTarget(view)
        start()
    }

}


fun pulseBtnAnimation(view: View) {
    val animScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.95f, 1.1f)
    animScaleX.apply {
        duration = 700
        interpolator = AccelerateDecelerateInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
    }
    val animScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.95f, 1.1f)
    animScaleY.apply {
        duration = 700
        interpolator = AccelerateDecelerateInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
    }
    animScaleX.start()
    animScaleY.start()
}

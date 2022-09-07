package uz.gita.myapplication.util.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


class Animator {

    fun fadeOut(view: ImageView) {

        ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                view.alpha = animatedValue as Float
            }
            interpolator = FastOutSlowInInterpolator()
            duration = 1500L
            start()
        }

    }

    fun shake(view: View) {
        val rotate = ObjectAnimator.ofFloat(view, "translationX", 0f, 20f, 0f, -20f, 0f)
        rotate.repeatCount = 1
        rotate.duration = 200
        rotate.start()
    }

}
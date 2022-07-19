package uz.gita.myapplication.util.animation

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.BounceInterpolator
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

        ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                view.x += animatedValue as Float
            }
            interpolator = BounceInterpolator()
            duration = 100L
            start()
            reverse()
        }

    }

}
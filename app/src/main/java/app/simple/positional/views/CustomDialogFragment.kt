package app.simple.positional.views

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import app.simple.positional.R

open class CustomDialogFragment : DialogFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog!!.window ?: return

        window.attributes.windowAnimations = R.style.DialogAnimation
        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        window.attributes.gravity = Gravity.CENTER
        window.attributes.width = (displayMetrics.widthPixels * 1f / 100f * 85f).toInt()
    }
}
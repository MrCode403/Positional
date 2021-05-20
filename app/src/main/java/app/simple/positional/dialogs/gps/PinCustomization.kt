package app.simple.positional.dialogs.gps

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.SeekBar
import app.simple.positional.R
import app.simple.positional.decorations.ripple.DynamicRippleImageButton
import app.simple.positional.decorations.views.CustomBottomSheetDialogFragment
import app.simple.positional.preference.GPSPreferences

class PinCustomization : CustomBottomSheetDialogFragment() {

    private lateinit var opacity: SeekBar
    private lateinit var size: SeekBar
    private lateinit var reset: DynamicRippleImageButton

    private var objectAnimator: ObjectAnimator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_pin_customization, container, false)

        opacity = view.findViewById(R.id.pin_opacity_seekbar)
        size = view.findViewById(R.id.pin_size_seekbar)
        reset = view.findViewById(R.id.reset_pin_customization)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        opacity.progress = GPSPreferences.getPinOpacity()
        size.progress = GPSPreferences.getPinSize()

        opacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    GPSPreferences.setPinOpacity(5)
                } else {
                    GPSPreferences.setPinOpacity(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                /* no-op */
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                /* no-op */
            }
        })

        size.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    GPSPreferences.setPinSize(40)
                } else {
                    GPSPreferences.setPinSize(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                /* no-op */
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                /* no-op */
            }
        })

        reset.setOnClickListener {
            updateSeekbar(opacity, 255)
            updateSeekbar(size, 400)
        }
    }

    private fun updateSeekbar(seekBar: SeekBar, value: Int) {
        objectAnimator = ObjectAnimator.ofInt(seekBar, "progress", seekBar.progress, value)
        objectAnimator?.duration = 1000L
        objectAnimator?.interpolator = DecelerateInterpolator()
        objectAnimator?.setAutoCancel(true)
        objectAnimator?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        objectAnimator?.cancel()
        opacity.clearAnimation()
        size.clearAnimation()
        if (!requireActivity().isDestroyed) {
            // TODO - fix menus here
            //GPSMenu().show(parentFragmentManager, "gps_menu")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    companion object {
        fun newInstance(): PinCustomization {
            val args = Bundle()
            val fragment = PinCustomization()
            fragment.arguments = args
            return fragment
        }
    }
}
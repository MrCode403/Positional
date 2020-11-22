package app.simple.positional.dialogs.compass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.simple.positional.R
import app.simple.positional.preference.CompassPreference
import app.simple.positional.ui.Compass
import app.simple.positional.views.CustomBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_compass_bloom.*
import java.lang.ref.WeakReference

class CompassBloom(private val weakReference: WeakReference<Compass>) : CustomBottomSheetDialog() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_compass_bloom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtons(CompassPreference().getFlowerBloomTheme(requireContext()))

        bloom_orange.setOnClickListener {
            setValue(0)
        }

        bloom_purple.setOnClickListener {
            setValue(1)
        }

        bloom_flower.setOnClickListener {
            setValue(2)
        }

        bloom_petals.setOnClickListener {
            setValue(3)
        }
    }

    private fun setValue(value: Int) {
        weakReference.get()?.setFlowerTheme(value)
        setButtons(value)
    }

    private fun setButtons(value: Int) {
        CompassPreference().setFlowerBloom(value, requireContext())

        bloom_orange.isChecked = value == 0
        bloom_purple.isChecked = value == 1
        bloom_flower.isChecked = value == 2
        bloom_petals.isChecked = value == 3
    }
}
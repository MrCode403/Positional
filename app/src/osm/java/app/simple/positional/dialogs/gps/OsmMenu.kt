package app.simple.positional.dialogs.gps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import app.simple.positional.R
import app.simple.positional.decorations.ripple.DynamicRippleLinearLayout
import app.simple.positional.decorations.ripple.DynamicRippleTextView
import app.simple.positional.decorations.switchview.SwitchView
import app.simple.positional.decorations.views.CustomBottomSheetDialogFragment
import app.simple.positional.dialogs.settings.HtmlViewer
import app.simple.positional.preference.GPSPreferences

class OsmMenu : CustomBottomSheetDialogFragment() {

    private lateinit var toggleAutoCenter: SwitchView
    private lateinit var toggleVolumeKeys: SwitchView
    private lateinit var toggleUseBearing: SwitchView
    private lateinit var togglePinCustomization: DynamicRippleTextView

    private lateinit var toggleAutoCenterContainer: DynamicRippleLinearLayout
    private lateinit var toggleVolumeKeysContainer: DynamicRippleLinearLayout
    private lateinit var toggleUseBearingContainer: DynamicRippleLinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_gps_menu, container, false)

        toggleAutoCenter = view.findViewById(R.id.toggle_auto_center)
        toggleVolumeKeys = view.findViewById(R.id.toggle_use_volume_keys)
        togglePinCustomization = view.findViewById(R.id.gps_pin_customization)
        toggleUseBearing = view.findViewById(R.id.toggle_bearing_rotation)

        toggleAutoCenterContainer = view.findViewById(R.id.gps_menu_auto_center_container)
        toggleVolumeKeysContainer = view.findViewById(R.id.gps_menu_volume_keys_container)
        toggleUseBearingContainer = view.findViewById(R.id.gps_menu_bearing_rotation)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleAutoCenter.isChecked = GPSPreferences.getMapAutoCenter()
        toggleVolumeKeys.isChecked = GPSPreferences.isUsingVolumeKeys()
        toggleUseBearing.isChecked = GPSPreferences.isBearingRotationOn()

        toggleAutoCenter.setOnCheckedChangeListener {
            GPSPreferences.setMapAutoCenter(it)
        }

        toggleVolumeKeys.setOnCheckedChangeListener {
            GPSPreferences.setUseVolumeKeys(it)
        }

        togglePinCustomization.setOnClickListener {
            PinCustomization.newInstance()
                    .show(requireActivity().supportFragmentManager, "pin_customization")
            dismiss()
        }

        toggleUseBearing.setOnCheckedChangeListener {
            GPSPreferences.setUseBearingRotation(it)
        }

        toggleAutoCenterContainer.setOnClickListener {
            toggleAutoCenter.invertCheckedStatus()
        }

        toggleVolumeKeysContainer.setOnClickListener {
            toggleVolumeKeys.invertCheckedStatus()
        }

        toggleVolumeKeysContainer.setOnLongClickListener {
            HtmlViewer.newInstance("Media Keys").show(childFragmentManager, "html_viewer")
            true
        }

        toggleUseBearingContainer.setOnClickListener {
            toggleUseBearing.invertCheckedStatus()
        }
    }
}

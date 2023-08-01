package com.nilay.evenger.utils

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nilay.evenger.R

open class BaseBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            BottomSheetDialog(requireContext(), theme)

    override fun getTheme(): Int = R.style.ThemeOverlay_App_BottomSheetDialog
}
package com.nilay.evenger.ui.fragments.attendance.change_percentage

import android.app.Dialog
import android.os.Bundle
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nilay.evenger.R
import com.nilay.evenger.databinding.DialogChangePercentageBinding
import com.nilay.evenger.core.db.user_pref.DataStoreCases
import com.nilay.evenger.utils.launch
import com.nilay.evenger.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePercentageDialog : DialogFragment() {
    private lateinit var binding: DialogChangePercentageBinding
    private val args: ChangePercentageDialogArgs by navArgs()

    @Inject
    lateinit var prefCases: DataStoreCases

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogChangePercentageBinding.inflate(layoutInflater)
        binding.apply {
            binding.seekBar.progress = args.percentage
            tvProgress.text = seekBar.progress.toString()
            progressBar.progress = seekBar.progress
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvProgress.text = progress.toString()
                    progressBar.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.changePercentage))
            .setView(binding.root)
            .setPositiveButton(resources.getString(R.string.done)) { _, _ ->
                if (binding.seekBar.progress == 0) {
                    toast("Percentage can't be 0")
                    return@setPositiveButton
                }
                if (binding.seekBar.progress == 100) {
                    toast("Percentage can't be 100")
                    return@setPositiveButton
                }
                if (binding.seekBar.progress == args.percentage) {
                    dismiss()
                    return@setPositiveButton
                }

                launch {
                    prefCases.updatePercentage(binding.seekBar.progress)
                }
                dismiss()
            }
            .setNegativeButton(resources.getString(R.string.cancel), null)

        return dialog.create()
    }
}
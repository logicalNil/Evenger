package com.nilay.evenger.ui.fragments.event.add_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.nilay.evenger.databinding.AddEditEventDialogBinding
import com.nilay.evenger.core.db.event.EventModel
import com.nilay.evenger.utils.BaseBottomSheet
import com.nilay.evenger.utils.convertToFormatDate
import com.nilay.evenger.utils.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddEditDialog : BaseBottomSheet() {
    private lateinit var binding: AddEditEventDialogBinding
    private val viewModel: AddEditViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddEditEventDialogBinding.inflate(layoutInflater)
        binding.apply {
            populateViews()
            pickData()
            validateInput()
        }
        return binding.root
    }

    private fun AddEditEventDialogBinding.pickData() = this.apply {
        textFieldDate.editText?.setText(viewModel.date.convertToFormatDate())
        textFieldDate.openDatePicker {
            viewModel.date = it.timeInMillis
        }
    }

    private fun AddEditEventDialogBinding.populateViews() = this.apply {
        textFieldTitle.editText?.setText(viewModel.event?.title)
        textFieldDescription.editText?.setText(viewModel.event?.description)
    }

    private fun AddEditEventDialogBinding.validateInput() = this.btEndButton.setOnClickListener {
        if (!textFieldTitle.validate()) return@setOnClickListener
        if (!textFieldDescription.validate()) return@setOnClickListener
        val title = binding.textFieldTitle.editText?.text.toString()
        val description = binding.textFieldDescription.editText?.text.toString()
        save(
            title = title, description = description
        )
    }

    private fun save(
        title: String, description: String
    ) = launchWhenCreated {
        if (viewModel.event == null) {
            viewModel.saveEvent(
                EventModel(
                    title = title,
                    description = description,
                    created = viewModel.date
                ),
                false,
                action = action
            )
            return@launchWhenCreated
        }
        viewModel.saveEvent(
            viewModel.event!!.copy(
                title = title,
                description = description,
                created = viewModel.date
            ),
            false,
            action = action
        )
    }

    private val action: () -> Unit = {
        dismiss()
    }

    private fun TextInputLayout.validate(): Boolean {
        val text = editText?.text.toString()
        return if (text.isEmpty()) {
            error = "Field can't be empty"
            false
        } else {
            error = null
            true
        }
    }

    private fun TextInputLayout.openDatePicker(action: (Calendar) -> Unit = {}) {
        editText?.setOnClickListener {
            datePicker(editText!!) {
                action.invoke(it)
            }
        }
    }

    private fun datePicker(
        editText: EditText, validation: () -> Boolean = { false }, action: (Calendar) -> Unit = {}
    ) {
        if (validation.invoke()) return
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build().apply {
                this.addOnPositiveButtonClickListener {
                    editText.setText(this.headerText)
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it
                    action(calendar)
                }
            }

        datePicker.show(childFragmentManager, "DatePickForEvenger")
    }
}
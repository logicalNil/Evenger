package com.nilay.evenger.ui.fragments.attendance.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilay.evenger.R
import com.nilay.evenger.databinding.LayoutBottomSheetBinding
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.ui.fragments.attendance.AttendanceViewModel
import com.nilay.evenger.ui.fragments.attendance.adapter.AttendanceAdapter
import com.nilay.evenger.ui.fragments.attendance.adapter.AttendanceItem
import com.nilay.evenger.utils.BaseBottomSheet
import com.nilay.evenger.utils.BottomSheetItem
import com.nilay.evenger.utils.DialogModel
import com.nilay.evenger.utils.REQUEST_MENU_FROM_ARCHIVE
import com.nilay.evenger.utils.launchWhenCreated
import com.nilay.evenger.utils.navigate
import com.nilay.evenger.utils.setTopView
import com.nilay.evenger.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveBottomSheet : BaseBottomSheet() {

    private val args: ArchiveBottomSheetArgs by navArgs()
    private val viewModel: AttendanceViewModel by activityViewModels()
    private val defPercentage: Int by lazy { args.defPercentage }
    private lateinit var binding: LayoutBottomSheetBinding
    private lateinit var attendanceAdapter: AttendanceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetBinding.inflate(inflater)
        binding.apply {
            setToolbar()
            setRecyclerview()
        }
        observeData()
        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeData() = launchWhenCreated {
        viewModel.archive.collect { attendanceList ->
            val data: MutableList<AttendanceItem> = attendanceList.map { attendanceModel ->
                AttendanceItem.AttendanceData(attendanceModel)
            } as MutableList<AttendanceItem>
            attendanceAdapter.items = data
            binding.ivEmpty.isVisible = data.isEmpty()
        }
    }

    private fun LayoutBottomSheetBinding.setRecyclerview() = this.listAll.apply {
        adapter = AttendanceAdapter(
            ::onItemClick,
            ::onCheckClick,
            ::onWrongClick,
            ::onLongClick
        ).also { attendanceAdapter = it }
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onItemClick(model: AttendanceModel) {
        val action = ArchiveBottomSheetDirections.actionArchiveBottomSheetToDetailViewBottomSheet(
            model,
            model.subject,
            defPercentage
        )
        navigate(action)
    }

    private fun onCheckClick(model: AttendanceModel) {
        com.nilay.evenger.ui.fragments.attendance.utils.onCheckClick(viewModel, model)
    }

    private fun onWrongClick(model: AttendanceModel) {
        com.nilay.evenger.ui.fragments.attendance.utils.onWrongClick(viewModel, model)
    }

    private fun onLongClick(model: AttendanceModel) {
        val action =
            ArchiveBottomSheetDirections.actionArchiveBottomSheetToAttendanceMenuBottomSheet(
                model,
                REQUEST_MENU_FROM_ARCHIVE
            )
        navigate(action)
    }


    private fun LayoutBottomSheetBinding.setToolbar() = this.apply {
        setTopView(
            BottomSheetItem(
                getString(R.string.archive),
                R.drawable.ic_delete_all,
                onIconClick = {
                    deleteAllArchive()// FIXME: communicator.hasChange = true
                },
                textViewApply = {
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward, 0, 0, 0)
                    setOnClickListener {
                        dismiss()
                    }
                })
        )
    }

    private fun deleteAllArchive() = showDialog(
        DialogModel(
            title = "Delete All Archive",
            message = "Are you sure you want to delete all archive?",
            positiveText = "Yes",
            negativeText = "No",
            positiveAction = {
                viewModel.deleteAllArchive()
                dismiss()
            },
        )
    )
}
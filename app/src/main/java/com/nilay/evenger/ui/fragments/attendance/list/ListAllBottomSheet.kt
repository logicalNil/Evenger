package com.nilay.evenger.ui.fragments.attendance.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilay.evenger.R
import com.nilay.evenger.core.api.ApiCase
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.databinding.LayoutBottomSheetBinding
import com.nilay.evenger.utils.BaseBottomSheet
import com.nilay.evenger.utils.BottomSheetItem
import com.nilay.evenger.utils.launchWhenCreated
import com.nilay.evenger.utils.setTopView
import com.nilay.evenger.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListAllBottomSheet : BaseBottomSheet() {
    private lateinit var binding: LayoutBottomSheetBinding
    private lateinit var listAllAdapter: ListAllAdapter

    @Inject
    lateinit var cases: ApiCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetBinding.inflate(inflater)
        binding.apply {
            setToolbar()
            setRecyclerView()
        }
        return binding.root
    }

    private fun LayoutBottomSheetBinding.setRecyclerView() = this.listAll.apply {
        adapter = ListAllAdapter(
            onClick = ::handleClick
        ).also { listAllAdapter = it }
        layoutManager = LinearLayoutManager(requireContext())
        observeData()
    }

    private fun handleClick(attendanceModel: AttendanceModel, isSelected: Boolean) =
        launchWhenCreated {
            Log.d("AAA", "handleClick: $isSelected")
        cases.addOrRemove.invoke(attendanceModel, isSelected)
        }

    private fun observeData() {
        launchWhenCreated {
            try {
                listAllAdapter.list = cases.getAppSubjects.invoke().toMutableList()
            } catch (e: Exception) {
                toast("Error ${e.message}")
            }
        }
    }

    private fun LayoutBottomSheetBinding.setToolbar() = this.apply {
        setTopView(
            BottomSheetItem(
                getString(R.string.add_subject_syllabus),
                R.drawable.ic_arrow_downward,
                onIconClick = {
                    dismiss()
                })
        )
    }
}
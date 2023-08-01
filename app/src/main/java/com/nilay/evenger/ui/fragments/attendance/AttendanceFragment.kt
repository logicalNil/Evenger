package com.nilay.evenger.ui.fragments.attendance

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilay.evenger.R
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.databinding.FragmentAttendanceBinding
import com.nilay.evenger.ui.fragments.attendance.adapter.AttendanceAdapter
import com.nilay.evenger.ui.fragments.attendance.adapter.AttendanceItem
import com.nilay.evenger.ui.fragments.attendance.utils.findPercentage
import com.nilay.evenger.ui.fragments.attendance.utils.showUndoMessage
import com.nilay.evenger.utils.DialogModel
import com.nilay.evenger.utils.DrawerState
import com.nilay.evenger.utils.REQUEST_ADD_SUBJECT_FROM_SYLLABUS
import com.nilay.evenger.utils.customBackPress
import com.nilay.evenger.utils.enterTransition
import com.nilay.evenger.utils.launchWhenCreated
import com.nilay.evenger.utils.navigate
import com.nilay.evenger.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class AttendanceFragment : Fragment(R.layout.fragment_attendance) {
    private val binding: FragmentAttendanceBinding by viewBinding()

    private val viewModel: AttendanceViewModel by activityViewModels()
    private lateinit var attendanceAdapter: AttendanceAdapter
    private var defPercentage = 75


    private val drawerState by lazy {
        requireActivity() as DrawerState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setFab()
            setRecyclerView()
            bottomAppBar()
        }
        observeData()
        listenForUndoMessage()
        handleBackPress()
        observePref()
    }

    private fun observePref() = launchWhenCreated {
        viewModel.getAllPref.collect {
            binding.setDefPercentage(it.defPercentage)
            defPercentage = it.defPercentage
            attendanceAdapter.defPercentage = it.defPercentage
        }
    }

    private fun FragmentAttendanceBinding.setDefPercentage(
        defPercentage: Int
    ) = this.attendanceView.apply {
        attendanceView.tvGoal.text =
            resources.getString(R.string.goal, defPercentage.toString())
        binding.attendanceView.tv3.text =
            resources.getString(R.string.goal, defPercentage.toString())
        binding.attendanceView.progressCircularInner.progress = defPercentage
    }

    private fun FragmentAttendanceBinding.setRecyclerView() = this.attendanceView.showAtt.apply {
        adapter = AttendanceAdapter(
            ::onItemClick,
            ::onCheckClick,
            ::onWrongClick,
            ::onLongClick
        ).also { attendanceAdapter = it }
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onItemClick(model: AttendanceModel) {
        val action = AttendanceFragmentDirections.actionAttendanceFragmentToDetailViewBottomSheet(
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
            AttendanceFragmentDirections.actionAttendanceFragmentToAttendanceMenuBottomSheet(model)
        navigate(action)
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeData() = launchWhenCreated {
        viewModel.unArchive.collect { attendanceList ->
            findPercentage(
                attendanceList.sumOf { it.present }.toFloat(),
                attendanceList.sumOf { it.total }.toFloat()
            ) { present, total ->
                when (total) {
                    0.0F -> 0.0F
                    else -> ((present / total) * 100)
                }
            }.let { binding.setTopView(it) }
            val data: MutableList<AttendanceItem> = attendanceList.map { attendanceModel ->
                AttendanceItem.AttendanceData(attendanceModel)
            } as MutableList<AttendanceItem>
            attendanceAdapter.items = data
            binding.attendanceView.emptyAnimation.isVisible = data.isEmpty()
        }
    }

    private fun FragmentAttendanceBinding.setTopView(finalPercentage: Float) =
        this.attendanceView.apply {
            val emoji = when {
                finalPercentage >= 80F -> resources.getString(R.string.moreThan80)
                finalPercentage >= defPercentage -> resources.getString(R.string.moreThanDefault)
                finalPercentage < defPercentage && finalPercentage > 60F -> resources.getString(
                    R.string.lessThanDefault
                )

                finalPercentage < 60F && finalPercentage != 0F -> resources.getString(R.string.lessThan60)
                else -> resources.getString(R.string.def_emoji)
            }
            tvPercentage.text = emoji
            materialDivider.text = emoji
            progressCircularOuter.progress = finalPercentage.toInt()
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.FLOOR
            tvOverAll.text = resources.getString(
                R.string.overallAttendance, df.format(finalPercentage)
            )
            tv4.text = resources.getString(
                R.string.overallAttendance, df.format(finalPercentage)
            )
        }

    private fun FragmentAttendanceBinding.setFab() = this.extendedFab.setOnClickListener {
        navigateToAttendance()
    }

    private fun navigateToAttendance() {
        val action =
            AttendanceFragmentDirections.actionAttendanceFragmentToAddEditAttendanceBottomSheet()
        findNavController().navigate(action)
    }

    private fun FragmentAttendanceBinding.bottomAppBar() = this.bottomAppBar.apply {
        setNavigationOnClickListener {
            drawerState.changeState()
        }
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_book -> navigateToAddSubjectFromSyllabus()
                R.id.menu_archive -> navigateToArchiveFragment()
                R.id.menu_setting -> navigateToChangePercentageDialog()
                R.id.menu_delete_all -> deleteAll()
                else -> false
            }

        }
    }

    private fun navigateToAddSubjectFromSyllabus(): Boolean {
        val action = AttendanceFragmentDirections.actionAttendanceFragmentToListAllBottomSheet()
        navigate(action)
        return true
    }

    private fun navigateToChangePercentageDialog(): Boolean {
        val direction =
            AttendanceFragmentDirections.actionAttendanceFragmentToChangePercentageDialog(
                defPercentage
            )
        navigate(direction)
        return true
    }

    private fun deleteAll(): Boolean {
        showDialog(
            DialogModel(
                "Delete All",
                "Are you sure you want to delete all attendance?",
                "Yes",
                "No",
                positiveAction = {
                    viewModel.deleteAll()
                    dismiss()
                }
            )
        )
        return true
    }

    private fun navigateToArchiveFragment(): Boolean {
        val action =
            AttendanceFragmentDirections.actionAttendanceFragmentToArchiveBottomSheet(defPercentage)
        navigate(action)
        return true
    }

    private fun listenForUndoMessage() = launchWhenCreated {
        viewModel.attendanceEvent.collect { attendanceEvent ->
            when (attendanceEvent) {
                is AttendanceViewModel.AttendanceEvent.ShowUndoDeleteMessage -> {
//                        Single attendance
                    attendanceEvent.attendance.showUndoMessage(
                        binding.root
                    ) {
                        viewModel.add(it, REQUEST_ADD_SUBJECT_FROM_SYLLABUS)
                    }

                }
            }
        }
    }

    private fun handleBackPress() {
        customBackPress {
            when {
//                mainActivity.getDrawerLayout().isDrawerOpen(GravityCompat.START) ->
//                    mainActivity.setDrawerState(false)

                else -> findNavController().navigateUp()
            }
        }
    }
}
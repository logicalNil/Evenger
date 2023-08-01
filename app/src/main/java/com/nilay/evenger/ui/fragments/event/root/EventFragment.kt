package com.nilay.evenger.ui.fragments.event.root

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.search.SearchView
import com.nilay.evenger.R
import com.nilay.evenger.core.db.event.EventModel
import com.nilay.evenger.core.db.event.EventUseCases
import com.nilay.evenger.databinding.FragmentScreenBinding
import com.nilay.evenger.ui.fragments.event.root.adapter.EventAdapter
import com.nilay.evenger.ui.fragments.event.root.adapter.EventItems
import com.nilay.evenger.utils.DrawerState
import com.nilay.evenger.utils.enterTransition
import com.nilay.evenger.utils.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

private const val TAG = "EventFragment"

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.fragment_screen) {
    private val binding: FragmentScreenBinding by viewBinding()
    private val viewModel: EventViewModel by viewModels()

    private val drawerState by lazy {
        requireActivity() as DrawerState
    }

    @Inject
    lateinit var cases: EventUseCases
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventSearchAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setRecyclerView()
            setSearchView()
            setCalender()
            drawer()
            searchRecyclerView()
        }
        handleExpand()
        observeData()
    }

    private fun handleExpand() {
        binding.searchView.addTransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.SHOWING) {
                drawerState.setBottomViewVisibility(false)
            }
            if (newState == SearchView.TransitionState.HIDING) {
                drawerState.setBottomViewVisibility(true)
                viewModel.query.value = "_DEFAULT_"
            }
        }
    }

    private fun FragmentScreenBinding.setSearchView() = this.searchView.apply {
        editText.doOnTextChanged { text, _, _, _ ->
            viewModel.query.value = text.toString().ifBlank { "_DEFAULT_" }
        }
    }

    private fun FragmentScreenBinding.searchRecyclerView() = this.rvSearch.apply {
        adapter = EventAdapter(
            onLongClick = ::handleLongClick,
            onEventClick = onClick(),
        ).also { eventSearchAdapter = it }
        layoutManager = LinearLayoutManager(requireContext())
        observeSearchItem()
    }

    private fun observeSearchItem() = launchWhenCreated {
        viewModel.getSearchItem().collectLatest {
            eventSearchAdapter.items = it
            binding.empty.isVisible = it.isEmpty()
        }
    }

    private fun FragmentScreenBinding.drawer() {
        searchBar.setNavigationOnClickListener {
            drawerState.changeState()
        }
    }

    private fun FragmentScreenBinding.setCalender() = this.calendarView.apply {
        shouldDrawIndicatorsBelowSelectedDays(true)
        setFirstDayOfWeek(Calendar.SUNDAY)
        setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                navigateToAddEdit(
                    date = dateClicked?.time ?: System.currentTimeMillis(),
                )
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                viewModel.monthStateFlow.value =
                    firstDayOfNewMonth?.time ?: System.currentTimeMillis()
            }

        })
    }

    private fun FragmentScreenBinding.setRecyclerView() = this.rvEvent.apply {
        adapter = EventAdapter(
            onLongClick = ::handleLongClick,
            onEventClick = onClick(),
        ).also { eventAdapter = it }
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onClick(): (EventModel, Boolean) -> Unit = { event, isDone ->
        viewModel.setEventIsDone(event, isDone)
    }

    private fun handleLongClick(eventModel: EventModel) {
        actionDialog(eventModel)
    }

    private fun observeData() = launchWhenCreated {
        viewModel.getData().collect {
            eventAdapter.items = it
            binding.ivEmpty.isVisible = it.isEmpty()
            it.filterIsInstance<EventItems.Event>().map { items ->
                items.event
            }.let { events ->
                binding.setCalenderData(events)
            }
        }
    }

    private fun FragmentScreenBinding.setCalenderData(eventItems: List<EventModel>) =
        this.calendarView.apply {
            removeAllEvents()
            eventItems.map { event ->
                Event(
                    MaterialColors.getColor(
                        requireContext(), androidx.appcompat.R.attr.colorPrimary, Color.GREEN
                    ), event.created, event.title
                )
            }.forEach {
                Log.d(TAG, "setCalender: ${it.data}")
                this.addEvent(it)
            }
        }

    private fun actionDialog(eventModel: EventModel) {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Action")
            .setMessage("Choose your action to perform").setPositiveButton("Edit") { d, _ ->
                navigateToAddEdit(eventModel, eventModel.created)
                d.dismiss()
            }.setNegativeButton("Delete") { d, _ ->
                viewModel.deleteEvent(eventModel)
                d.dismiss()
            }.show()
    }

    private fun navigateToAddEdit(event: EventModel? = null, date: Long) {
        val action = EventFragmentDirections.actionEventFragmentToAddEditDialog(
            event = event, date = date
        )
        findNavController().navigate(action)
    }
}
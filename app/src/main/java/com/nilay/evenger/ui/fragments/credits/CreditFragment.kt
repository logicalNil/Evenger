package com.nilay.evenger.ui.fragments.credits

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilay.evenger.R
import com.nilay.evenger.databinding.FragmentCreditsBinding
import com.nilay.evenger.utils.ToolbarData
import com.nilay.evenger.utils.enterTransition
import com.nilay.evenger.utils.openLinkToDefaultApp
import com.nilay.evenger.utils.set
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditFragment : Fragment(R.layout.fragment_credits) {
    private val binding: FragmentCreditsBinding by viewBinding()
    private lateinit var creditsAdapter: CreditsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setToolbar()
            setRecyclerView()
        }
    }

    private fun FragmentCreditsBinding.setRecyclerView() = this.recyclerView.apply {
        adapter = CreditsAdapter(::openLink).also { creditsAdapter = it }
        layoutManager = LinearLayoutManager(requireContext())
        creditsAdapter.list = credits
    }

    private fun openLink(credit: Credit) {
        openLinkToDefaultApp(credit.url)
    }

    private fun FragmentCreditsBinding.setToolbar() = this.includeToolbar.apply {
        set(
            ToolbarData(
                title = R.string.acknowledgements, action = findNavController()::navigateUp
            )
        )
    }
}

enum class Licenses(val title: String) {
    APACHE_2_0("Apache 2.0"), MIT(
        "MIT"
    ),
}

data class Credit(
    val title: String, val url: String, val licenses: List<Licenses> = listOf()
)

val credits = listOf(
    Credit(
        "Android-Viewbinding", "https://github.com/yogacp/android-viewbinding", listOf(Licenses.MIT)
    ),
    Credit(
        "Splash-Screen",
        "https://developer.android.com/develop/ui/views/launch/splash-screen",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Core-Ktx",
        "https://developer.android.com/jetpack/androidx/releases/core",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Hilt", "https://developer.android.com/training/dependency-injection/hilt-android",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "App Compat", "https://developer.android.com/jetpack/androidx/releases/appcompat",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Constraint Layout",
        "https://developer.android.com/jetpack/androidx/releases/constraintlayout",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Fragment Ktx",
        "https://developer.android.com/jetpack/androidx/releases/fragment",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Lifecycle Ktx",
        "https://developer.android.com/jetpack/androidx/releases/lifecycle",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Material",
        "https://developer.android.com/jetpack/androidx/releases/material",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Navigation",
        "https://developer.android.com/jetpack/androidx/releases/navigation",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Compact-Calendar-View",
        "https://github.com/SundeepK/CompactCalendarView",
        listOf(Licenses.MIT)
    ),
    Credit(
        "Firebase",
        "https://firebase.google.com/",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Gson",
        "https://github.com/google/gson",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Recycler-View",
        "https://developer.android.com/jetpack/androidx/releases/recyclerview",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Room",
        "https://developer.android.com/jetpack/androidx/releases/room",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Retofit",
        "https://github.com/square/retrofit",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Lottie",
        "https://github.com/airbnb/lottie-android",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Browser",
        "https://developer.android.com/jetpack/androidx/releases/browser",
        listOf(Licenses.APACHE_2_0)
    ),
    Credit(
        "Play-Services",
        "https://developers.google.com/android/guides/setup",
        listOf(Licenses.APACHE_2_0)
    )
).sortedBy {
    it.title
}
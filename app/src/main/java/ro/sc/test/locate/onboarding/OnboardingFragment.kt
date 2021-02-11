package ro.sc.test.locate.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import ro.sc.test.locate.R
import ro.sc.test.locate.databinding.FragmentOnboardingBinding

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpagerOnboarding.apply {
            adapter = OnboardingAdapter()
        }

        binding.dotsIndicator.setViewPager2(binding.viewpagerOnboarding)

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate("app:locate://register".toUri(),
                navOptions {
                    anim {
                        enter = R.anim.fade_in
                        exit = R.anim.fade_out
                        popEnter = 0
                        popExit = R.anim.fade_out
                    }
                })
        }

        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(
                "app:locate://login".toUri(),
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.nav_graph, true)
                    .setEnterAnim(R.anim.fade_in)
                    .setExitAnim(R.anim.fade_out)
                    .build()
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
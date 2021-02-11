package ro.sc.test.locate.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ro.sc.test.locate.R
import ro.sc.test.locate.databinding.FragmentSuccessBinding

@AndroidEntryPoint
class SuccessFragment : Fragment() {
    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_KEY_ID = "is_reset"

        @JvmStatic
        fun create(id: Long): SuccessFragment {
            return SuccessFragment().apply {
                arguments = bundleOf(ARG_KEY_ID to id)
            }
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            goToLogin()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message =
            if (arguments?.getBoolean(ARG_KEY_ID) == true) R.string.success_desc_reset else R.string.success_desc_register
        binding.textInfoDesc.setText(message)

        binding.imgSuccess.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        findNavController().navigate(
            "app:locate://login".toUri(),
            NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.nav_graph, true)
                .setExitAnim(R.anim.slide_out_v_pop)
                .setEnterAnim(R.anim.slide_in_v_pop)
                .build()
        )
    }

    override fun onPause() {
        super.onPause()
        callback.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        callback.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
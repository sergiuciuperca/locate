package ro.sc.test.locate.reset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ro.sc.test.locate.R
import ro.sc.test.locate.databinding.FragmentResetPasswordBinding

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.stateFlow.onEach {
            handle(it)
        }
            .launchIn(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonReset.setOnClickListener {
            viewModel.reset(binding.inputLayoutEmail.editText?.text.toString())
        }

        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(
                "app:locate://login".toUri(),
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_login, false)
                    .setEnterAnim(R.anim.fade_in)
                    .setExitAnim(R.anim.slide_out_v)
                    .build()
            )
        }
    }


    private fun handle(state: ResetViewState) {
        if (state.resetSuccess) {
            findNavController().navigate(
                "app:locate://success/true".toUri(),
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.nav_graph, true)
                    .setEnterAnim(R.anim.slide_in_v)
                    .setExitAnim(R.anim.slide_out_v)
                    .build()
            )
            return
        }

        if (state.uiError != null) {
            showErrorMessage(state.uiError)
        }
    }

    private fun showErrorMessage(error: ResetError) {
        val message = when (error) {
            InvalidEmailAddress -> getString(R.string.login_invalid_email)
            AccountNotFound -> getString(R.string.reset_account_not_found)
            NetworkError -> getString(R.string.network_connection)
            else -> getString(R.string.unknown_error)
        }

        MaterialDialog(requireContext()).show {
            title(R.string.dialog_info)
            message(text = message)
            positiveButton(R.string.ok)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
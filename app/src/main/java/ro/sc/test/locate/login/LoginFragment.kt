package ro.sc.test.locate.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.PopUpToBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ro.sc.test.locate.R
import ro.sc.test.locate.databinding.FragmentLoginBinding


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            viewModel.login(
                binding.inputLayoutEmail.editText?.text.toString(),
                binding.inputLayoutPassword.editText?.text.toString()
            )
        }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate("app:locate://register".toUri())
        }

        binding.buttonForgot.setOnClickListener {
            findNavController().navigate("app:locate://reset-password".toUri(), navOptions {
                anim {
                    exit = R.anim.fade_out
                    enter = R.anim.slide_in_v_pop
                    popEnter = R.anim.fade_in
                    popExit = R.anim.slide_out_v
                }
            })
        }

        viewModel.stateFlow.onEach { state ->
            handle(state)
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handle(state: LoginViewState) {
        if (state.loginSuccess) {
            findNavController().navigate(
                "app:locate://home".toUri(), NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
            )
            return
        }
        if (state.uiError != null) {
            showErrorMessage(state.uiError)
        }
    }

    private fun showErrorMessage(error: LoginError) {
        val message = when (error) {
            InvalidEmail -> getString(R.string.login_invalid_email)
            InvalidPassword -> getString(R.string.login_invalid_password)
            NetworkError -> getString(R.string.network_connection)
            InvalidUser -> getString(R.string.login_user_not_found)
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
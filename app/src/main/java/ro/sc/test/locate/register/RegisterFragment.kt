package ro.sc.test.locate.register

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
import ro.sc.test.locate.databinding.FragmentRegisterBinding

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegister.setOnClickListener {
            viewModel.register(
                binding.inputLayoutName.editText?.text.toString(),
                binding.inputLayoutEmail.editText?.text.toString(),
                binding.inputLayoutPassword.editText?.text.toString(),
                binding.inputLayoutPasswordConfirm.editText?.text.toString()
            )
        }

        viewModel.stateFlow
            .onEach {
                handle(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handle(state: RegisterViewState) {
        if (state.registerSuccess) {
            findNavController().navigate(
                "app:locate://success/false".toUri(),
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

    private fun showErrorMessage(error: RegisterError) {
        val message = when (error) {
            InvalidEmail -> getString(R.string.login_invalid_email)
            WeakPassword -> getString(R.string.register_password_weak)
            PasswordDoNotMatch -> getString(R.string.register_password_match)
            EmailCollision -> getString(R.string.register_email_collision)
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
package ro.sc.test.locate.register

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ro.sc.test.locate.ReduxViewModel
import ro.sc.test.locate.domain.LoginUseCase
import ro.sc.test.locate.domain.RegisterUserUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ReduxViewModel<RegisterViewState>(RegisterViewState()) {
    private val uiActions = MutableSharedFlow<RegisterAction>()

    init {
        handleLoginEvent()
    }

    private fun handleLoginEvent() {
        viewModelScope.launch {
            uiActions.filterIsInstance<Register>()
                .flatMapLatest {
                    registerUserUseCase.registerUser(
                        RegisterUserUseCase.Params(
                            it.name,
                            it.email,
                            it.password,
                            it.confirmPassword
                        )
                    )
                }
                .collectAndSetState { result ->
                    when (result) {
                        RegisterUserUseCase.UseCaseResult.Loading -> {
                            copy(isLoading = true, uiError = null)
                        }
                        RegisterUserUseCase.UseCaseResult.RegisterSuccess -> {
                            copy(isLoading = false, registerSuccess = true, uiError = null)
                        }
                        else -> {
                            copy(isLoading = false, uiError = result.asError())
                        }
                    }
                }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            uiActions.emit(Register(name, email, password, confirmPassword))
        }
    }
}
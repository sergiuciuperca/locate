package ro.sc.test.locate.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ro.sc.test.locate.ReduxViewModel
import ro.sc.test.locate.domain.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ReduxViewModel<LoginViewState>(LoginViewState()) {
    private val uiActions = MutableSharedFlow<LoginAction>()

    init {
        handleLoginEvent()
    }

    private fun handleLoginEvent() {
        viewModelScope.launch {
            uiActions.filterIsInstance<Login>()
                .flatMapLatest {
                    loginUseCase.login(it.email, it.password)
                }
                .collectAndSetState { result ->
                    when (result) {
                        LoginUseCase.UseCaseResult.Loading -> {
                            copy(isLoading = true, uiError = null)
                        }
                        LoginUseCase.UseCaseResult.LoginSuccess -> {
                            copy(isLoading = false, loginSuccess = true, uiError = null)
                        }
                        else -> {
                            copy(isLoading = false, uiError = result.asError())
                        }
                    }
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiActions.emit(Login(email, password))
        }
    }
}
package ro.sc.test.locate.reset

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ro.sc.test.locate.ReduxViewModel
import ro.sc.test.locate.domain.ResetPasswordUseCase
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val resetUseCase: ResetPasswordUseCase
) : ReduxViewModel<ResetViewState>(ResetViewState()) {
    private val uiActions = MutableSharedFlow<ResetAction>()

    init {
        handleLoginEvent()
    }

    private fun handleLoginEvent() {
        viewModelScope.launch {
            uiActions.filterIsInstance<Reset>()
                .flatMapLatest {
                    resetUseCase.resetPassword(it.email)
                }
                .collectAndSetState { result ->
                    when (result) {
                        ResetPasswordUseCase.UseCaseResult.Loading -> {
                            copy(isLoading = true, uiError = null)
                        }
                        ResetPasswordUseCase.UseCaseResult.ResetSuccess -> {
                            copy(isLoading = false, resetSuccess = true, uiError = null)
                        }
                        else -> {
                            copy(isLoading = false, uiError = result.asError())
                        }
                    }
                }
        }
    }

    fun reset(email: String) {
        Log.e("Loginvm", "reset with email $email")
        viewModelScope.launch {
            uiActions.emit(Reset(email))
        }
    }
}
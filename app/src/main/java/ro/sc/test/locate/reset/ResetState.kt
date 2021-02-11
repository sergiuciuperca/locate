package ro.sc.test.locate.reset

import ro.sc.test.locate.domain.ResetPasswordUseCase

data class ResetViewState(
    val isLoading: Boolean = false,
    val resetSuccess: Boolean = false,
    val uiError: ResetError? = null
)

sealed class ResetError
object InvalidEmailAddress: ResetError()
object NetworkError: ResetError()
object AccountNotFound: ResetError()

fun ResetPasswordUseCase.UseCaseResult.asError(): ResetError? {
    return when(this) {
        ResetPasswordUseCase.UseCaseResult.InvalidEmail -> InvalidEmailAddress
        ResetPasswordUseCase.UseCaseResult.EmailNotFound -> AccountNotFound
        ResetPasswordUseCase.UseCaseResult.NetworkError -> NetworkError
        else -> null
    }
}

sealed class ResetAction
data class Reset(val email: String) : ResetAction()
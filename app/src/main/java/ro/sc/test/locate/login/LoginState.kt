package ro.sc.test.locate.login

import ro.sc.test.locate.domain.LoginUseCase

data class LoginViewState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val uiError: LoginError? = null
)

sealed class LoginError
object InvalidPassword : LoginError()
object InvalidEmail : LoginError()
object InvalidUser : LoginError()
object NetworkError : LoginError()
object UnknownError : LoginError()

fun LoginUseCase.UseCaseResult.asError(): LoginError? {
    return when (this) {
        LoginUseCase.UseCaseResult.InvalidPassword -> InvalidPassword
        LoginUseCase.UseCaseResult.InvalidEmail -> InvalidEmail
        LoginUseCase.UseCaseResult.InvalidUser -> InvalidUser
        LoginUseCase.UseCaseResult.CouldNotLogin -> UnknownError
        LoginUseCase.UseCaseResult.NetworkError -> NetworkError
        else -> null
    }
}


sealed class LoginAction
data class Login(val email: String, val password: String) : LoginAction()
package ro.sc.test.locate.register

import ro.sc.test.locate.domain.RegisterUserUseCase

data class RegisterViewState(
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val uiError: RegisterError? = null
)


sealed class RegisterError
object InvalidEmail : RegisterError()
object WeakPassword : RegisterError()
object PasswordDoNotMatch : RegisterError()
object EmailCollision : RegisterError()
object NetworkError : RegisterError()
object UnknownError : RegisterError()

fun RegisterUserUseCase.UseCaseResult.asError(): RegisterError? {
    return when (this) {
        RegisterUserUseCase.UseCaseResult.InvalidConfirmPassword -> PasswordDoNotMatch
        RegisterUserUseCase.UseCaseResult.InvalidEmail -> InvalidEmail
        RegisterUserUseCase.UseCaseResult.InvalidPassword -> WeakPassword
        RegisterUserUseCase.UseCaseResult.EmailCollision -> EmailCollision
        RegisterUserUseCase.UseCaseResult.NetworkError -> NetworkError
        else -> null
    }
}

sealed class RegisterAction
data class Register(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
) : RegisterAction()
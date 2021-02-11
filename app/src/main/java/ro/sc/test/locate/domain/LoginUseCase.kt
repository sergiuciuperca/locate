package ro.sc.test.locate.domain

import android.util.Patterns
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ro.sc.test.locate.data.entities.ErrorResult
import ro.sc.test.locate.data.entities.Success
import ro.sc.test.locate.data.repositories.user.UserDataSource
import ro.sc.test.locate.util.AppCoroutineDispatchers
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val userDataSource: UserDataSource
) {
    suspend fun login(email: String, password: String): Flow<UseCaseResult> {
        return flow {
            emit(UseCaseResult.Loading)
            emit(handleLogin(email, password))
        }
            .flowOn(dispatchers.io)
    }

    private suspend fun handleLogin(email: String, password: String): UseCaseResult {
        if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return UseCaseResult.InvalidEmail
        }

        if (password.trim().isEmpty()) {
            return UseCaseResult.InvalidPassword
        }

        return when (val result = userDataSource.login(email.trim(), password.trim())) {
            is Success -> {
                UseCaseResult.LoginSuccess
            }
            is ErrorResult -> {
                when (result.throwable) {
                    is FirebaseAuthInvalidUserException -> {
                        return UseCaseResult.InvalidUser
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        return UseCaseResult.InvalidPassword
                    }

                    is FirebaseException -> {
                        return UseCaseResult.NetworkError
                    }
                }
                UseCaseResult.CouldNotLogin
            }
        }
    }

    sealed class UseCaseResult {
        object Loading : UseCaseResult()
        object LoginSuccess : UseCaseResult()
        object InvalidEmail : UseCaseResult()
        object InvalidUser : UseCaseResult()
        object InvalidPassword : UseCaseResult()
        object CouldNotLogin : UseCaseResult()
        object NetworkError : UseCaseResult()
    }
}
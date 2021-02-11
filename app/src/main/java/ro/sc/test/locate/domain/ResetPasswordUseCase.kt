package ro.sc.test.locate.domain

import android.util.Patterns
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ro.sc.test.locate.data.entities.ErrorResult
import ro.sc.test.locate.data.entities.Success
import ro.sc.test.locate.data.repositories.user.UserDataSource
import ro.sc.test.locate.util.AppCoroutineDispatchers
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val userDataSource: UserDataSource
) {
    suspend fun resetPassword(email: String): Flow<UseCaseResult> {
        return flow {
            emit(UseCaseResult.Loading)
            emit(handleReset(email))
        }
            .flowOn(dispatchers.io)
    }

    private suspend fun handleReset(email: String): UseCaseResult {
        if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return UseCaseResult.InvalidEmail
        }

        return when (val data = userDataSource.resetPassword(email.trim())) {
            is Success -> {
                UseCaseResult.ResetSuccess
            }
            is ErrorResult -> {
                when (data.throwable) {
                    is FirebaseAuthInvalidUserException -> {
                        return UseCaseResult.EmailNotFound
                    }

                    is FirebaseException -> {
                        return UseCaseResult.NetworkError
                    }
                }
                UseCaseResult.UnknownError
            }
        }
    }

    sealed class UseCaseResult {
        object Loading : UseCaseResult()
        object ResetSuccess : UseCaseResult()
        object InvalidEmail : UseCaseResult()
        object EmailNotFound : UseCaseResult()
        object UnknownError : UseCaseResult()
        object NetworkError : UseCaseResult()
    }
}
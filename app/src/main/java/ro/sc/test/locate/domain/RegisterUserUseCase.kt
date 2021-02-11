package ro.sc.test.locate.domain

import android.util.Patterns
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ro.sc.test.locate.data.entities.ErrorResult
import ro.sc.test.locate.data.entities.Success
import ro.sc.test.locate.data.repositories.user.UserDataSource
import ro.sc.test.locate.util.AppCoroutineDispatchers
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val userDataSource: UserDataSource
) {
    suspend fun registerUser(params: Params): Flow<UseCaseResult> {
        return flow {
            emit(UseCaseResult.Loading)
            emit(handleReset(params))
        }
            .flowOn(dispatchers.io)
    }

    private suspend fun handleReset(params: Params): UseCaseResult {
        if (params.email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(params.email)
                .matches()
        ) {
            return UseCaseResult.InvalidEmail
        }

        if (params.password.trim().length < 6) {
            return UseCaseResult.InvalidPassword
        }

        if (params.password != params.confirmPassword) {
            return UseCaseResult.InvalidConfirmPassword
        }

        return when (val result =
            userDataSource.signUp(params.email, params.password, params.name)) {
            is Success -> {
                UseCaseResult.RegisterSuccess
            }
            is ErrorResult -> {
                when (val error = result.throwable) {
                    is FirebaseAuthUserCollisionException -> {
                        return UseCaseResult.EmailCollision
                    }

                    is FirebaseException -> {
                        return UseCaseResult.NetworkError
                    }
                }
                UseCaseResult.InvalidEmail
            }
        }
    }

    data class Params(
        val name: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    )

    sealed class UseCaseResult {
        object Loading : UseCaseResult()
        object RegisterSuccess : UseCaseResult()
        object InvalidEmail : UseCaseResult()
        object InvalidPassword : UseCaseResult()
        object InvalidConfirmPassword : UseCaseResult()
        object EmailCollision : UseCaseResult()
        object NetworkError : UseCaseResult()
    }
}
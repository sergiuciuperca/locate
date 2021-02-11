package ro.sc.test.locate.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ro.sc.test.locate.data.repositories.PreferenceManager
import ro.sc.test.locate.data.repositories.user.UserDataSource
import ro.sc.test.locate.util.AppCoroutineDispatchers
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val userDataSource: UserDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {


    suspend fun checkUserStatus(): UseCaseResult {
        return withContext(dispatchers.io) {
            val user = userDataSource.currentLoggedUser().get()
            delay(1750)//splash delay

            if (user != null) {
                return@withContext UseCaseResult.ShowHome
            }

//            if (preferenceManager.onboardingDisplayed) {
//                return@withContext UseCaseResult.ShowLogin
//            }

            preferenceManager.setOnboardingDisplayed()
            return@withContext UseCaseResult.ShowOnboarding
        }
    }

    sealed class UseCaseResult {
        object ShowOnboarding : UseCaseResult()
        object ShowLogin : UseCaseResult()
        object ShowHome : UseCaseResult()
    }
}
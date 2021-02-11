package ro.sc.test.locate.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

class PreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private companion object {
        const val ONBOARDING_DISPLAY = "onboarding_displayed"
    }

    val onboardingDisplayed: Boolean
        get() = sharedPreferences.getBoolean(ONBOARDING_DISPLAY, false)

    fun setOnboardingDisplayed() {
        sharedPreferences.edit {
            putBoolean(ONBOARDING_DISPLAY, true)
        }
    }
}
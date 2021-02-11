package ro.sc.test.locate.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ro.sc.test.locate.ReduxViewModel
import ro.sc.test.locate.domain.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    authUseCase: AuthUseCase
) : ReduxViewModel<MainViewState>(MainViewState()) {

    init {
        viewModelScope.launch {
            when (authUseCase.checkUserStatus()) {
                AuthUseCase.UseCaseResult.ShowOnboarding -> {
                    setState { copy(mainNavigation = Onboarding) }
                }
                AuthUseCase.UseCaseResult.ShowHome -> {
                    setState { copy(mainNavigation = Home) }
                }
                AuthUseCase.UseCaseResult.ShowLogin -> {
                    setState { copy(mainNavigation = Login) }
                }
            }
        }
    }
}
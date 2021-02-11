package ro.sc.test.locate.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ro.sc.test.locate.ReduxViewModel
import ro.sc.test.locate.data.entities.ui.HomeItemData
import ro.sc.test.locate.domain.HomeItemsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeItemsUseCase: HomeItemsUseCase
) : ReduxViewModel<HomeViewState>(HomeViewState()) {

    init {
        viewModelScope.launch {
            homeItemsUseCase.observeHomeItems()
                .collectAndSetState { copy(items = it) }
        }
    }
}

data class HomeViewState(
    val items: List<HomeItemData> = emptyList()
)
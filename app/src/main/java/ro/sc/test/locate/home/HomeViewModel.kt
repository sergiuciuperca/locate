package ro.sc.test.locate.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ro.sc.test.locate.ReduxViewModel
import ro.sc.test.locate.domain.HomeItemsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeItemsUseCase: HomeItemsUseCase
) : ReduxViewModel<HomeViewState>(HomeViewState()) {
    private val uiActions = MutableSharedFlow<HomeAction>()

    init {
        observeItems()
    }

    private fun observeItems() {
        viewModelScope.launch {
            uiActions.filterIsInstance<FilterJobs>()
                .distinctUntilChanged()
                .debounce(300)
                .onStart { emit(FilterJobs()) }
                .flatMapLatest {
                    homeItemsUseCase.observeHomeItems(it.query.trim())
                }
                .collectAndSetState { copy(items = it) }
        }
    }

    fun filter(query: String) {
        viewModelScope.launch {
            uiActions.emit(FilterJobs(query))
        }
    }
}
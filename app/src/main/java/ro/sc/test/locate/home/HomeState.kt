package ro.sc.test.locate.home

import ro.sc.test.locate.data.entities.ui.HomeItemData

data class HomeViewState(
    val items: List<HomeItemData> = emptyList()
)

sealed class HomeAction
data class FilterJobs(val query: String = "") : HomeAction()
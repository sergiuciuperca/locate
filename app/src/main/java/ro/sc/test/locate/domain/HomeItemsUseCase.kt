package ro.sc.test.locate.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ro.sc.test.locate.data.entities.ui.HomeItemData
import ro.sc.test.locate.data.repositories.job.JobsDataSource
import ro.sc.test.locate.util.AppCoroutineDispatchers
import javax.inject.Inject

class HomeItemsUseCase @Inject constructor(
    private val jobsDataSource: JobsDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {
    suspend fun observeHomeItems(query: String = ""): Flow<List<HomeItemData>> =
        jobsDataSource.observeJobs()
            .flowOn(dispatchers.io)
            .map { jobs -> jobs.filter { it.title.contains(query, true) } }
}
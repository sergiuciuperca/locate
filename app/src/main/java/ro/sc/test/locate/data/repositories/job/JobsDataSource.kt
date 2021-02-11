package ro.sc.test.locate.data.repositories.job

import kotlinx.coroutines.flow.Flow
import ro.sc.test.locate.data.entities.ui.HomeItemData

interface JobsDataSource {
    suspend fun observeJobs(): Flow<List<HomeItemData>>
}
package ro.sc.test.locate.data.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ro.sc.test.locate.data.repositories.job.FirestoreDataSource
import ro.sc.test.locate.data.repositories.job.JobsDataSource
import ro.sc.test.locate.data.repositories.user.FirebaseUserDataSource
import ro.sc.test.locate.data.repositories.user.UserDataSource

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceBinds {

    @Binds
    abstract fun userDataSource(binds: FirebaseUserDataSource): UserDataSource

    @Binds
    abstract fun jobsDataSource(binds: FirestoreDataSource): JobsDataSource
}
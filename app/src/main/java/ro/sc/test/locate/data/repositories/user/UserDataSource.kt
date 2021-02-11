package ro.sc.test.locate.data.repositories.user

import ro.sc.test.locate.data.entities.Result
import ro.sc.test.locate.data.entities.User

interface UserDataSource {
    suspend fun currentLoggedUser(): Result<User?>
    suspend fun login(email: String, password: String): Result<User?>
    suspend fun signUp(email: String, password: String, name: String): Result<Boolean>
    suspend fun resetPassword(email: String): Result<Boolean>
}
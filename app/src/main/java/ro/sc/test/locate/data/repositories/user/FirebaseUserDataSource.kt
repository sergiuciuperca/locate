package ro.sc.test.locate.data.repositories.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import ro.sc.test.locate.data.entities.ErrorResult
import ro.sc.test.locate.data.entities.Result
import ro.sc.test.locate.data.entities.Success
import ro.sc.test.locate.data.entities.User
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    UserDataSource {

    override suspend fun currentLoggedUser(): Result<User?> {
        return Success(firebaseAuth.currentUser?.toUser())
    }

    override suspend fun login(email: String, password: String): Result<User?> {
        return try {
            val data = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            currentLoggedUser()
        } catch (t: Throwable) {
            ErrorResult(t)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<Boolean> {
        return try {
            val data = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            val update = data.user?.updateProfile(profileUpdate)?.await()

            return Success(true)
        } catch (t: Throwable) {
            ErrorResult(t)
        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return try {
            val data = firebaseAuth.sendPasswordResetEmail(email).await()
            Success(true)
        } catch (t: Throwable) {
            ErrorResult(t)
        }

    }
}

fun FirebaseUser.toUser(): User {
    return User(this.email.orEmpty(), this.displayName.orEmpty())
}
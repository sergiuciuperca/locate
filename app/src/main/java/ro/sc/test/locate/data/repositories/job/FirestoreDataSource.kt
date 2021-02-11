package ro.sc.test.locate.data.repositories.job

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ro.sc.test.locate.data.entities.ui.HomeItemData
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : JobsDataSource {


    override suspend fun observeJobs(): Flow<List<HomeItemData>> = callbackFlow {
        val subscription = firestore.collection("jobs")
            .addSnapshotListener { snapshot, _ ->
                offer(parseJobDocuments(snapshot?.documents.orEmpty()))
            }

        awaitClose { subscription.remove() }
    }

    private fun parseJobDocuments(documents: List<DocumentSnapshot>): List<HomeItemData> {
        return documents.map { doc ->
            HomeItemData(
                doc.id.hashCode().toLong(),
                doc.data?.getValue("title") as? String ?: "",
                doc.data?.getValue("image") as? String ?: "",
                (doc.data?.getValue("stars") as? Number ?: 0).toInt(),
                doc.data?.getValue("time") as? String ?: "",
                doc.data?.getValue("price") as? String ?: "",
                doc.data?.getValue("info") as? String ?: ""
            )
        }
    }
}
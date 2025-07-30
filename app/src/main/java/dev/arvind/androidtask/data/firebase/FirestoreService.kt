package dev.arvind.androidtask.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.arvind.androidtask.data.remote.dto.ServerDto
import dev.arvind.androidtask.data.remote.dto.toDomain
import dev.arvind.androidtask.data.remote.dto.toDto
import dev.arvind.androidtask.domain.model.Server
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){

    private fun getUserCollection() = auth.currentUser?.uid?.let { userId ->
        firestore.collection("users").document(userId).collection("servers")
    }

    suspend fun syncServers(servers: List<Server>): Result<Unit> {
        return try {
            val userCollection = getUserCollection()
                ?: return Result.failure(Exception("User not authenticated"))

            val batch = firestore.batch()

            servers.forEach { server ->
                val docRef = userCollection.document(server.id)
                batch.set(docRef, server.toDto())
            }

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getServers(): Result<List<Server>> {
        return try {
            val userCollection = getUserCollection()
                ?: return Result.failure(Exception("User not authenticated"))

            val snapshot = userCollection.get().await()
            val servers = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ServerDto::class.java)?.toDomain()
            }

            Result.success(servers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
package dev.arvind.androidtask.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.usecase.UpdateServerStateUseCase
import dev.arvind.androidtask.utils.NotificationHelper
import kotlinx.coroutines.delay
import kotlin.random.Random

@HiltWorker
class BootWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateServerStateUseCase: UpdateServerStateUseCase,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("BootWorker", "Starting server boot process...")
            val serverId = inputData.getString("server_id")
                ?: return Result.failure()

            // Simulate boot time (5-15 seconds)
            delay(Random.nextLong(5000, 15000))

            val result = updateServerStateUseCase(serverId, ServerState.RUNNING)

            if (result.isSuccess) {
                notificationHelper.showNotification(
                    "Server Booted",
                    "Server $serverId is now running"
                )
                Log.d("BootWorker", "Server boot process completed.")
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Log.d("BootWorker", "Server boot process failed.", e)
            Result.failure()
        }
    }
}
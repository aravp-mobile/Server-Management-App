package dev.arvind.androidtask.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.arvind.androidtask.domain.usecase.CalculateBillingUseCase

@HiltWorker
class BillingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val calculateBillingUseCase: CalculateBillingUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("BillingWorker", "Starting billing calculation...")
            val result = calculateBillingUseCase()

            // Simulate work for now
            kotlinx.coroutines.delay(1000)

            if (result.isSuccess) {
                Log.d("BillingWorker", "Billing calculation completed successfully.")
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Log.d("BillingWorker", "Billing calculation failed.", e)
            Result.failure()
        }
    }
}
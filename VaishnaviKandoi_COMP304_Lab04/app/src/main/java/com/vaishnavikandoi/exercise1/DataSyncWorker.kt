package com.vaishnavikandoi.exercise1

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DataSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Sync data or perform background tasks
        return Result.success()
    }
}

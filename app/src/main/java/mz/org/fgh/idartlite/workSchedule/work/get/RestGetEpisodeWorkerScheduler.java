package mz.org.fgh.idartlite.workSchedule.work.get;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import mz.org.fgh.idartlite.base.service.BaseService;
import mz.org.fgh.idartlite.rest.helper.RESTServiceHandler;
import mz.org.fgh.idartlite.rest.service.RestEpisodeService;

public class RestGetEpisodeWorkerScheduler extends Worker {
    private static final String TAG = "RestGetEpisodeWorker";

    public RestGetEpisodeWorkerScheduler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (RESTServiceHandler.getServerStatus(BaseService.baseUrl)) {
                Log.d(TAG, "doWork: Sync Patient Data");
                RestEpisodeService.restGetAllReadyEpisodes(null);
                RestEpisodeService.restGetAllEpisodes(null);
            } else {
                Log.e(TAG, "Response Servidor Offline");
                return Result.failure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }
}

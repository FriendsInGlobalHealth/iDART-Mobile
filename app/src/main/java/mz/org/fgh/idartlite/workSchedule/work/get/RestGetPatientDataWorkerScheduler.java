package mz.org.fgh.idartlite.workSchedule.work.get;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import mz.org.fgh.idartlite.base.rest.BaseRestService;
import mz.org.fgh.idartlite.rest.helper.RESTServiceHandler;
import mz.org.fgh.idartlite.rest.service.Patient.RestPatientService;
import mz.org.fgh.idartlite.workSchedule.work.generic.AbstractWorker;

import static mz.org.fgh.idartlite.base.application.IdartLiteApplication.CHANNEL_1_ID;

public class RestGetPatientDataWorkerScheduler extends AbstractWorker {
    private static final String TAG = "RestGetPatientDataWorke";

    public RestGetPatientDataWorkerScheduler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {

            if (RESTServiceHandler.getServerStatus(BaseRestService.baseUrl)) {
                Log.d(TAG, "doWork: Sync Patient Data");
                RestPatientService.restGetAllPatient(watcher);

                while (!watcher.hasUpdates()){
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException exception) {

                    }
                }
                issueNotification(CHANNEL_1_ID);
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

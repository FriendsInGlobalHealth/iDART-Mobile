package mz.org.fgh.idartlite.workSchedule.work.post;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import mz.org.fgh.idartlite.base.model.BaseModel;
import mz.org.fgh.idartlite.base.service.BaseService;
import mz.org.fgh.idartlite.model.Dispense;
import mz.org.fgh.idartlite.model.Episode;
import mz.org.fgh.idartlite.rest.helper.RESTServiceHandler;
import mz.org.fgh.idartlite.service.dispense.DispenseService;
import mz.org.fgh.idartlite.service.episode.EpisodeService;
import mz.org.fgh.idartlite.rest.service.RestDispenseService;
import mz.org.fgh.idartlite.rest.service.RestEpisodeService;

public class RestPostPatientDataWorkerScheduler extends Worker {

    private static final String TAG = "RestPostWorkerScheduler";
    private List<Dispense> dispenseList;
    
    protected DispenseService dispenseService;

    protected EpisodeService episodeService;
    
    public RestPostPatientDataWorkerScheduler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            dispenseService = new DispenseService(BaseService.getApp(),null);
            episodeService = new EpisodeService(BaseService.getApp(),null);
            if (RESTServiceHandler.getServerStatus(BaseService.baseUrl)) {
                Log.d(TAG, "doWork: Sync Patient Data");
                dispenseList = dispenseService.getAllDispensesByStatus(BaseModel.SYNC_SATUS_READY);
                if(dispenseList != null)
                if(dispenseList.size() > 0) {
                    for (Dispense dispense : dispenseList) {
                        RestDispenseService.restPostDispense(dispense);
                    }
                }else {Log.d(TAG, "doWork: Sem Aviamentos para syncronizar");}

            List<Episode> episodeList=episodeService.getAllEpisodeByStatus(BaseModel.SYNC_SATUS_READY);
            if(episodeList != null && episodeList.size() > 0) {
                for (Episode episode : episodeList) {
                    RestEpisodeService.restPostEpisode(episode);
                }
            }
                else {
                    Log.d(TAG, "doWork: Sem Episodios para syncronizar");
                }
            }
            else {
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

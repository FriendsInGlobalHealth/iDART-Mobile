package mz.org.fgh.idartlite.service;

import android.app.Application;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import mz.org.fgh.idartlite.base.BaseService;
import mz.org.fgh.idartlite.model.Episode;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.User;
import mz.org.fgh.idartlite.rest.RESTServiceHandler;
import mz.org.fgh.idartlite.util.Utilities;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EpisodeService extends BaseService {

    public EpisodeService(Application application, User currUser) {
        super(application, currUser);
    }

    public List<Episode> getAllEpisodesByPatient(Patient patient) throws SQLException{
        return getDataBaseHelper().getEpisodeDao().getAllByPatient(patient);
    }

    public Episode getLatestByPatientAndSanitryUuid(Patient patient,String uuid) throws SQLException{
        return getDataBaseHelper().getEpisodeDao().getLatestByPatientAndSanitryUuid(patient,uuid);
    }

    public Episode findEpisodeWithStopReasonByPatient(Patient patient) throws SQLException {
        return getDataBaseHelper().getEpisodeDao().findEpisodeWithStopReasonByPatient(patient);
    }


    public void createEpisode(Episode episode) throws SQLException {

        episode.setSyncStatus("R");
        episode.setUuid(Utilities.getNewUUID().toString());
        getDataBaseHelper().getEpisodeDao().create(episode);
    }
    public void udpateEpisode(Episode episode) throws SQLException {
        getDataBaseHelper().getEpisodeDao().update(episode);
    }
    public void deleteEpisode(Episode episode) throws SQLException {
        getDataBaseHelper().getEpisodeDao().delete(episode);
    }
}

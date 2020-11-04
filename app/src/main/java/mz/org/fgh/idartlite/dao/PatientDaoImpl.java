package mz.org.fgh.idartlite.dao;

import android.app.Application;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mz.org.fgh.idartlite.model.Clinic;
import mz.org.fgh.idartlite.model.Dispense;
import mz.org.fgh.idartlite.model.Episode;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.Prescription;
import mz.org.fgh.idartlite.model.TherapeuticLine;
import mz.org.fgh.idartlite.util.DateUtilitis;

public class PatientDaoImpl extends GenericDaoImpl<Patient, Integer> implements PatientDao{

    public PatientDaoImpl(Class dataClass) throws SQLException {
        super(dataClass);
    }

    public PatientDaoImpl(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public PatientDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    public List<Patient> searchPatientByParamAndClinic(String param, Clinic clinic, long offset, long limit) throws SQLException {
        List<Patient> recs = queryBuilder().limit(limit)
                                            .offset(offset)
                                            .where()
                                            .like(Patient.COLUMN_NID, "%" + param + "%")
                                            .or().like(Patient.COLUMN_FIRST_NAME, "%" + param + "%")
                                            .or().like(Patient.COLUMN_LAST_NAME, "%" + param + "%")
                                            .and()
                                            .eq(Patient.COLUMN_CLINIC_ID, clinic.getId()).query();
        return recs;
    }

    @Override
    public int countNewPatientsByPeriod(Date start, Date end, Application application) throws SQLException {
        QueryBuilder<Episode, Integer> episodeQb = IdartLiteDataBaseHelper.getInstance(application.getApplicationContext()).getEpisodeDao().queryBuilder();
        episodeQb.where()
                .isNull(Episode.COLUMN_STOP_REASON)
                .and()
                .isNotNull(Episode.COLUMN_START_REASON)
                .and()
                .ge(Episode.COLUMN_EPISODE_DATE, start)
                .and()
                .le(Episode.COLUMN_EPISODE_DATE, end);

        return (int) episodeQb.countOf();
    }

    public boolean checkIsEmpty(String param, Clinic clinic) throws SQLException {
       return  queryBuilder().where().like(Patient.COLUMN_NID, "%" + param + "%").or().like(Patient.COLUMN_FIRST_NAME, "%" + param + "%").or().like(Patient.COLUMN_LAST_NAME, "%" + param + "%").and().eq(Patient.COLUMN_CLINIC_ID, clinic.getId()).query().isEmpty();
    }
}

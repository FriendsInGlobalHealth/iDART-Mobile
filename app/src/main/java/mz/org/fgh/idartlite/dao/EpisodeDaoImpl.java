package mz.org.fgh.idartlite.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import mz.org.fgh.idartlite.model.Episode;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.User;

import java.sql.SQLException;
import java.util.List;

public class EpisodeDaoImpl extends GenericDaoImpl<Episode, Integer> implements EpisodeDao{

    public EpisodeDaoImpl(Class dataClass) throws SQLException {
        super(dataClass);
    }

    public EpisodeDaoImpl(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public EpisodeDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public List<Episode> getAllByPatient(Patient patient) throws SQLException {
        return queryBuilder().where().eq(Episode.COLUMN_PATIENT_ID, patient.getId()).query();
    }
}

package mz.org.fgh.idartlite.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import mz.org.fgh.idartlite.model.Clinic;

import java.sql.SQLException;

public class ClinicDaoImpl extends GenericDaoImpl<Clinic, Integer> implements ClinicDao{

    public ClinicDaoImpl(Class dataClass) throws SQLException {
        super(dataClass);
    }

    public ClinicDaoImpl(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public ClinicDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}

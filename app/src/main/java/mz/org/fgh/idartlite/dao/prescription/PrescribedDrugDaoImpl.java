package mz.org.fgh.idartlite.dao.prescription;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.fgh.idartlite.dao.generic.GenericDaoImpl;
import mz.org.fgh.idartlite.model.PrescribedDrug;

public class PrescribedDrugDaoImpl extends GenericDaoImpl<PrescribedDrug, Integer> implements IPrescribedDrugDao {
    public PrescribedDrugDaoImpl(Class dataClass) throws SQLException {
        super(dataClass);
    }

    public PrescribedDrugDaoImpl(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public PrescribedDrugDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}

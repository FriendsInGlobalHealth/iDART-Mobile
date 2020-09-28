package mz.org.fgh.idartlite.dao;

import android.app.Application;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.fgh.idartlite.model.Dispense;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.Prescription;
import mz.org.fgh.idartlite.model.TherapeuticLine;

public class DispenseDaoImpl extends GenericDaoImpl<Dispense, Integer> implements DispenseDao {

    public DispenseDaoImpl(Class dataClass) throws SQLException {
        super(dataClass);
    }

    public DispenseDaoImpl(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public DispenseDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public List<Dispense> getAllByPrescription(Prescription prescription) throws SQLException {
        return queryBuilder().where().eq(Dispense.COLUMN_PRESCRIPTION, prescription.getId()).query();
    }

    @Override
    public long countAllOfPrescription(Prescription prescription) throws SQLException {
        return queryBuilder().where().eq(Dispense.COLUMN_PRESCRIPTION, prescription.getId()).countOf();
    }

    public List<Dispense> getAllOfPatient(Application application, Patient patient) throws SQLException {

        QueryBuilder<Prescription, Integer> prescriptionQb = IdartLiteDataBaseHelper.getInstance(application.getApplicationContext()).getPrescriptionDao().queryBuilder();
        prescriptionQb.where().eq(Prescription.COLUMN_PATIENT_ID, patient.getId());

        QueryBuilder<TherapeuticLine, Integer> therapeuticLineQb = IdartLiteDataBaseHelper.getInstance(application.getApplicationContext()).getTherapeuticLineDao().queryBuilder();
        prescriptionQb.join(therapeuticLineQb);

        QueryBuilder<Dispense, Integer> dispenseQb =   IdartLiteDataBaseHelper.getInstance(application.getApplicationContext()).getDispenseDao().queryBuilder();
        dispenseQb.join(prescriptionQb);

        List<Dispense> dispenses = dispenseQb.orderBy(Dispense.COLUMN_NEXT_PICKUP_DATE,false).query();

        return dispenses;
    }
}

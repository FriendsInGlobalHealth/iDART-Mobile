package mz.org.fgh.idartlite.service;

import android.app.Application;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import mz.org.fgh.idartlite.base.BaseService;
import mz.org.fgh.idartlite.dao.PatientDao;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.User;

public class PatientService extends BaseService {

    private PatientDao patientDao;

    public PatientService(Application application, User currentUser) {
        super(application, currentUser);
        try{
            patientDao = getDataBaseHelper().getPatientDao();
        }catch (SQLException sql){
            Log.i("erro ", sql.getMessage());
        }
    }

    public List<Patient> search(String param) throws SQLException {
        return patientDao.queryBuilder().where().like(Patient.COLUMN_NID, "%" + param + "%").or().like(Patient.COLUMN_FIRST_NAME, "%" + param + "%").query();
    }

    public List<Patient> getALLPatient() throws  SQLException {
        return patientDao.queryForAll();
    }

    public void  savePatient(Patient patient) throws SQLException {
        getDataBaseHelper().getPatientDao().create(patient);
    }
}

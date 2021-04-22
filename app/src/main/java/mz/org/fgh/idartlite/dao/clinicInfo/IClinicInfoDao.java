package mz.org.fgh.idartlite.dao.clinicInfo;

import android.app.Application;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import mz.org.fgh.idartlite.dao.generic.IGenericDao;
import mz.org.fgh.idartlite.model.ClinicInformation;
import mz.org.fgh.idartlite.model.Dispense;
import mz.org.fgh.idartlite.model.Episode;
import mz.org.fgh.idartlite.model.Patient;

public interface IClinicInfoDao extends IGenericDao<ClinicInformation, Integer> {


    public List<ClinicInformation> getAllByPatient(Patient patient) throws SQLException;

    public List<ClinicInformation> getAllClinicInfoByStatus(String status) throws SQLException;

    public List<ClinicInformation> getPregnantPatientWithStartDateAndEndDateWithLimit(Application application, Date startDate, Date endDate, long offset, long limit) throws SQLException;

    public List<ClinicInformation> getTBSuspectPatientWithStartDateAndEndDateWithLimit(Application application, Date startDate, Date endDate, long offset, long limit) throws SQLException;

    public List<ClinicInformation> getTracedPatientsWithStartDateAndEndDateWithLimit(Application application, Date startDate, Date endDate, long offset, long limit) throws SQLException;
}

package mz.org.fgh.idartlite.viewmodel.patient;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import mz.org.fgh.idartlite.BR;
import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.base.viewModel.BaseViewModel;
import mz.org.fgh.idartlite.common.ApplicationStep;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.util.Utilities;
import mz.org.fgh.idartlite.view.patientPanel.PatientDemographicFragment;
import mz.org.fgh.idartlite.view.patientPanel.PatientPanelActivity;
import mz.org.fgh.idartlite.view.patientPanel.PrescriptionFragment;
import mz.org.fgh.idartlite.view.prescription.PrescriptionActivity;
import mz.org.fgh.idartlite.viewmodel.prescription.PrescriptionVM;

public class PatientDemographicsVM extends BaseViewModel {

    private boolean initialDataVisible;
    private boolean contactDataVisible;
    private boolean otherDataVisible;

    private Patient patient;

    private PatientDemographicFragment patientDemographicFragment;

    public PatientDemographicsVM(@NonNull Application application) {
        super(application);
    }


    public void editPatient(){


            if (getPatientDemographicFragment().getMyActivity().getPatient().hasEndEpisode()) {
                Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.cant_edit_patient_data)).show();
            } else {
                getCurrentStep().changeToEdit();
                getPatientDemographicFragment().startPatientActivity();
            }



    }



    @Bindable
    public boolean isInitialDataVisible() {
        return initialDataVisible;
    }

    public void setInitialDataVisible(boolean initialDataVisible) {
        this.initialDataVisible = initialDataVisible;
        notifyPropertyChanged(BR.initialDataVisible);
    }

    @Bindable
    public boolean isContactDataVisible() {
        return contactDataVisible;
    }

    public void setContactDataVisible(boolean contactDataVisible) {
        this.contactDataVisible = contactDataVisible;
        notifyPropertyChanged(BR.contactDataVisible);
    }

    @Bindable
    public boolean isOtherDataVisible() {
        return otherDataVisible;
    }

    public void setOtherDataVisible(boolean otherDataVisible) {
        this.otherDataVisible = otherDataVisible;
        notifyPropertyChanged(BR.otherDataVisible);
    }

    public void changeDataViewStatus(View view){
        getPatientDemographicFragment().changeVisibilityToInitialData(view);
    }

    public PatientDemographicFragment getPatientDemographicFragment() {
        return patientDemographicFragment;
    }

    public void setPatientDemographicFragment(PatientDemographicFragment patientDemographicFragment) {
        this.patientDemographicFragment = patientDemographicFragment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


}

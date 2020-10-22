package mz.org.fgh.idartlite.view.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.base.GenericFragment;
import mz.org.fgh.idartlite.databinding.PacinteDetailsFragmentBinding;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.util.Utilities;
import mz.org.fgh.idartlite.viewmodel.PatientDetailsVM;

public class PatientDetailsFragment extends GenericFragment {
    PacinteDetailsFragmentBinding pacinteDetailsFragmentBinding;

    public PatientDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pacinteDetailsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.pacinte_details_fragment, container,false);

        return pacinteDetailsFragmentBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRelatedViewModel().setPatient(getSelectedPatient());
        pacinteDetailsFragmentBinding.setViewModel(getRelatedViewModel());
        getRelatedViewModel().setPatientDetailsFragment(this);
    }


    public void changeVisibilityToInitialData(View view) {
        if (view.equals(pacinteDetailsFragmentBinding.initialData)){
            if (pacinteDetailsFragmentBinding.personDataLyt.getVisibility() == View.VISIBLE){
                switchLayout();
                pacinteDetailsFragmentBinding.ibtnInitialData.animate().setDuration(200).rotation(180);
                Utilities.collapse(pacinteDetailsFragmentBinding.personDataLyt);
            }else {
                switchLayout();
                pacinteDetailsFragmentBinding.ibtnInitialData.animate().setDuration(200).rotation(0);
                Utilities.expand(pacinteDetailsFragmentBinding.personDataLyt);
            }

        }else if (view.equals(pacinteDetailsFragmentBinding.contacto)){
            if (pacinteDetailsFragmentBinding.contactDataLyt.getVisibility() == View.VISIBLE){
                switchLayout();
                pacinteDetailsFragmentBinding.ibtnContacto.animate().setDuration(200).rotation(180);
                Utilities.collapse(pacinteDetailsFragmentBinding.contactDataLyt);
            }else {
                switchLayout();
                pacinteDetailsFragmentBinding.ibtnContacto.animate().setDuration(200).rotation(0);
                Utilities.expand(pacinteDetailsFragmentBinding.contactDataLyt);
            }

        }else if (view.equals(pacinteDetailsFragmentBinding.others)){
            if (pacinteDetailsFragmentBinding.othersDataLyt.getVisibility() == View.VISIBLE){
                switchLayout();
                pacinteDetailsFragmentBinding.ibtnOthers.animate().setDuration(200).rotation(180);
                Utilities.collapse(pacinteDetailsFragmentBinding.othersDataLyt);
            }else {
                switchLayout();
                pacinteDetailsFragmentBinding.ibtnOthers.animate().setDuration(200).rotation(0);
                Utilities.expand(pacinteDetailsFragmentBinding.othersDataLyt);
            }
        }
    }

    private void switchLayout(){
        getRelatedViewModel().setInitialDataVisible(!getRelatedViewModel().isInitialDataVisible());
        getRelatedViewModel().setContactDataVisible(!getRelatedViewModel().isContactDataVisible());
        getRelatedViewModel().setOtherDataVisible(!getRelatedViewModel().isOtherDataVisible());
    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(getMyActivity()).get(PatientDetailsVM.class);
    }

    public PatientDetailsVM getRelatedViewModel() {
        return (PatientDetailsVM) super.getRelatedViewModel();
    }

    public PatientActivity getMyActivity(){
        return (PatientActivity) getActivity();
    }

    private Patient getSelectedPatient(){
        return getMyActivity().getPatient();
    }
}
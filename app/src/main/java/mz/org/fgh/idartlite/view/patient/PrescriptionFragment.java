package mz.org.fgh.idartlite.view.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.base.GenericFragment;
import mz.org.fgh.idartlite.databinding.PrescriptionFragmentBinding;
import mz.org.fgh.idartlite.model.DispenseType;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.Prescription;
import mz.org.fgh.idartlite.model.TherapeuticLine;
import mz.org.fgh.idartlite.model.TherapeuticRegimen;
import mz.org.fgh.idartlite.util.DateUtilitis;
import mz.org.fgh.idartlite.util.Utilities;
import mz.org.fgh.idartlite.view.patient.adapter.PrescriptionAdapter;
import mz.org.fgh.idartlite.viewmodel.PrescriptionVM;

public class PrescriptionFragment extends GenericFragment {

    private RecyclerView rcvPrescriptions;
    private List<Prescription> prescriptionList;

    private PrescriptionFragmentBinding prescriptionFragmentBinding;
    private PrescriptionAdapter prescriptionAdapter;

    public PrescriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        prescriptionFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.prescription_fragment, container,false);
        return prescriptionFragmentBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.rcvPrescriptions = prescriptionFragmentBinding.rcvPrescriptions;



        try {
            this.prescriptionList = getRelatedViewModel().gatAllOfPatient(getSelectedPatient());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Utilities.listHasElements(prescriptionList)) {
            prescriptionAdapter = new PrescriptionAdapter(rcvPrescriptions, this.prescriptionList, getMyActivity());
            displayDataOnRecyclerView(rcvPrescriptions, prescriptionAdapter, getContext());
        }

    }

    /*private void displayPrescriptions(RecyclerView rcvEpisodes) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rcvPrescriptions.setLayoutManager(mLayoutManager);
        rcvPrescriptions.setItemAnimator(new DefaultItemAnimator());
        rcvPrescriptions.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        prescriptionAdapter = new PrescriptionAdapter(rcvPrescriptions, this.prescriptionList, getMyActivity());
        rcvPrescriptions.setAdapter(prescriptionAdapter);

        rcvPrescriptions.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rcvPrescriptions, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(getContext(), "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));
    }*/

    public PatientActivity getMyActivity(){
        return (PatientActivity) getActivity();
    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(PrescriptionVM.class);
    }

    @Override
    public PrescriptionVM getRelatedViewModel() {
        return (PrescriptionVM) super.getRelatedViewModel();
    }

    private Patient getSelectedPatient(){
        return getMyActivity().getPatient();
    }

    private void generateTestData(){

        Prescription p = new Prescription();
        p.setDispenseType(new DispenseType());
        p.getDispenseType().setDescription("Semanal");
        p.getDispenseType().setCode("SEMANAL");
        p.setPatient(getMyActivity().getPatient());
        p.setTherapeuticLine(new TherapeuticLine());
        p.getTherapeuticLine().setDescription("Linha terapeutica 1");
        p.getTherapeuticLine().setCode("Linha_Ter_1");
        p.setTherapeuticRegimen(new TherapeuticRegimen());
        p.getTherapeuticRegimen().setDescription("Regime 1");
        p.getTherapeuticRegimen().setRegimenCode("REGIME_1");
        p.setExpiryDate(DateUtilitis.getCurrentDate());
        p.setPrescriptionDate(DateUtilitis.getCurrentDate());
        p.setSyncStatus("N");

        try {
            getRelatedViewModel().create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
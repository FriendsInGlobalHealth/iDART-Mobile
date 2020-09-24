package mz.org.fgh.idartlite.view.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.adapter.ClickListener;
import mz.org.fgh.idartlite.base.BaseModel;
import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.base.GenericFragment;
import mz.org.fgh.idartlite.common.ListbleDialogListener;
import mz.org.fgh.idartlite.databinding.PrescriptionFragmentBinding;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.Prescription;
import mz.org.fgh.idartlite.util.Utilities;
import mz.org.fgh.idartlite.view.patient.adapter.PrescriptionAdapter;
import mz.org.fgh.idartlite.viewmodel.PrescriptionVM;

public class PrescriptionFragment extends GenericFragment implements ListbleDialogListener {

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

        prescriptionFragmentBinding.newPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> params = new HashMap<>();
                params.put("patient", getMyActivity().getPatient());
                params.put("user", getCurrentUser());
                params.put("clinic", getMyActivity().getCurrentClinic());
                nextActivity(getContext(), PrescriptionActivity.class,params);
            }
        });

        rcvPrescriptions.addOnItemTouchListener(
                new ClickListener(getContext(), rcvPrescriptions, new ClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        displayPopupMenu(view, position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        displayPopupMenu(view, position);
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
            ));
    }

    private void displayPopupMenu(View view, int position) {
        getRelatedViewModel().setPrescription(prescriptionList.get(position));
        getRelatedViewModel().getPrescription().setListPosition(position);

        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(PrescriptionFragment.this::onMenuItemClick);
        inflater.inflate(R.menu.edit_remove_menu, popup.getMenu());
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit:
                Map<String, Object> params = new HashMap<>();
                params.put("prescription", getRelatedViewModel().getPrescription());
                params.put("user", getCurrentUser());
                params.put("clinic", getMyActivity().getCurrentClinic());
                nextActivity(getContext(), PrescriptionActivity.class,params);
                return true;
            case R.id.remove:
                Utilities.displayDeleteConfirmationDialogFromList(PrescriptionFragment.this.getContext(),PrescriptionFragment.this.getString(R.string.list_item_delete_msg),getRelatedViewModel().getPrescription().getListPosition(),PrescriptionFragment.this).show();
                return true;
            default:
                return false;
        }

    }

    @Override
    public void remove(int position) {

        String errorMsg = getRelatedViewModel().checkPrescriptionRemoveConditions();

        if(!Utilities.stringHasValue(errorMsg)){

            try {
                prescriptionList.remove(getRelatedViewModel().getPrescription());

                rcvPrescriptions.getAdapter().notifyItemRemoved(position);
                rcvPrescriptions.removeViewAt(position);
                rcvPrescriptions.getAdapter().notifyItemRangeChanged(position, rcvPrescriptions.getAdapter().getItemCount());

                getRelatedViewModel().deletePrescription(prescriptionList.get(position));
            } catch (SQLException e) {
                Utilities.displayAlertDialog(PrescriptionFragment.this.getContext(), getString(R.string.record_sucessfuly_removed)).show();
            }
        }
        else {
            Utilities.displayAlertDialog(PrescriptionFragment.this.getContext(),errorMsg).show();
        }

    }

    @Override
    public void remove(BaseModel baseModel) {

    }

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

}
package mz.org.fgh.idartlite.view.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.base.BaseActivity;
import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.common.Listble;
import mz.org.fgh.idartlite.common.ListbleAdapter;
import mz.org.fgh.idartlite.common.RecyclerTouchListener;
import mz.org.fgh.idartlite.databinding.ActivityPrescriptionBinding;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.view.patient.adapter.PrescriptionAdapter;
import mz.org.fgh.idartlite.viewmodel.PrescriptionVM;

public class PrescriptionActivity extends BaseActivity {

    private ActivityPrescriptionBinding prescriptionBinding;

    private List<Listble> selectedDrugs;

    private RecyclerView rcvSelectedDrugs;

    private ListbleAdapter listbleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prescriptionBinding = DataBindingUtil.setContentView(this, R.layout.activity_prescription);

        rcvSelectedDrugs = prescriptionBinding.rcvSelectedDrugs;

        Intent intent = this.getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                getRelatedViewModel().getPrescription().setPatient((Patient) bundle.getSerializable("patient"));
                prescriptionBinding.setViewModel(getRelatedViewModel());

                if (getRelatedViewModel().getPrescription().getPatient() == null){
                    throw new RuntimeException("Não foi seleccionado um paciente para detalhar.");
                }
            }
        }

        prescriptionBinding.prescriptionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PrescriptionActivity.this, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                prescriptionBinding.prescriptionDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        prescriptionBinding.initialData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibilityToInitialData(view);

            }
        });

        prescriptionBinding.imvAddSelectedDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (selectedDrugs == null) selectedDrugs = new ArrayList<>();

               if ( prescriptionBinding.spnDrugs.getSelectedItem() != null){
                   Listble listble = (Listble) prescriptionBinding.spnDrugs.getSelectedItem();
                   listble.setListPosition(selectedDrugs.size()+1);
                   selectedDrugs.add(listble);
               }
            }
        });
    }

    private void changeVisibilityToInitialData(View view) {
        if (view.equals(prescriptionBinding.initialData)){
            prescriptionBinding.initialDataLyt.setVisibility(View.VISIBLE);
            prescriptionBinding.drugsDataLyt.setVisibility(View.GONE);
        }else if (view.equals(prescriptionBinding.txvDrugs)){
            prescriptionBinding.initialDataLyt.setVisibility(View.GONE);
            prescriptionBinding.drugsDataLyt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(PrescriptionVM.class);
    }

    @Override
    public PrescriptionVM getRelatedViewModel() {
        return (PrescriptionVM) super.getRelatedViewModel();
    }

    private void displaySelectedDrugs(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvSelectedDrugs.setLayoutManager(mLayoutManager);
        rcvSelectedDrugs.setItemAnimator(new DefaultItemAnimator());
        rcvSelectedDrugs.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));

        listbleAdapter = new ListbleAdapter(rcvSelectedDrugs, this.selectedDrugs, this);
        rcvSelectedDrugs.setAdapter(listbleAdapter);


    }
}
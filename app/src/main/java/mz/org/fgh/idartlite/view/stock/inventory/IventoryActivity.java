package mz.org.fgh.idartlite.view.stock.inventory;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.adapter.recyclerview.listable.ListbleRecycleViewAdapter;
import mz.org.fgh.idartlite.adapter.spinner.listable.ListableSpinnerAdapter;
import mz.org.fgh.idartlite.base.activity.BaseActivity;
import mz.org.fgh.idartlite.base.viewModel.BaseViewModel;
import mz.org.fgh.idartlite.databinding.ActivityIventoryBinding;
import mz.org.fgh.idartlite.model.Drug;
import mz.org.fgh.idartlite.util.Utilities;
import mz.org.fgh.idartlite.viewmodel.stock.InventoryVM;

public class IventoryActivity extends BaseActivity {

    private ListableSpinnerAdapter drugArrayAdapter;

    private RecyclerView rcvSelectedDrugs;

    private ListbleRecycleViewAdapter listbleRecycleViewAdapter;

    private ActivityIventoryBinding iventoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iventoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_iventory);

        populateDrugs(getRelatedViewModel().getDrugs());

        iventoryBinding.setViewModel(getRelatedViewModel());

        iventoryBinding.autCmpDrugs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                getRelatedViewModel().setSelectedDrug((Drug) adapterView.getItemAtPosition(pos));
            }
        });
    }

    public void populateDrugs(List<Drug> drugs){
        drugArrayAdapter = new ListableSpinnerAdapter(this, R.layout.simple_auto_complete_item, drugs);
        iventoryBinding.autCmpDrugs.setAdapter(drugArrayAdapter);
        iventoryBinding.autCmpDrugs.setThreshold(1);
    }

    public void changeFormSectionVisibility(View view) {
        if (view.equals(iventoryBinding.initialData)) {
            if (iventoryBinding.initialDataLyt.getVisibility() == View.VISIBLE){
                iventoryBinding.ibtnInitialData.animate().setDuration(200).rotation(180);
                Utilities.collapse(iventoryBinding.initialDataLyt);
            }else {
                iventoryBinding.ibtnInitialData.animate().setDuration(200).rotation(0);
                Utilities.expand(iventoryBinding.initialDataLyt);
            }
        }
    }

    public void displaySelectedDrugs(){
        if (listbleRecycleViewAdapter != null) {
            listbleRecycleViewAdapter.notifyDataSetChanged();
        }else {
            rcvSelectedDrugs = iventoryBinding.rcvSelectedDrugs;

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(IventoryActivity.this);
            rcvSelectedDrugs.setLayoutManager(mLayoutManager);
            rcvSelectedDrugs.setItemAnimator(new DefaultItemAnimator());
            rcvSelectedDrugs.addItemDecoration(new DividerItemDecoration(IventoryActivity.this, 0));

            listbleRecycleViewAdapter = new ListbleRecycleViewAdapter(rcvSelectedDrugs, getRelatedViewModel().getAdjustmentList(), this);
            rcvSelectedDrugs.setAdapter(listbleRecycleViewAdapter);
        }
    }

    @Override
    public InventoryVM getRelatedViewModel() {
        return (InventoryVM) super.getRelatedViewModel();
    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(InventoryVM.class);
    }
}
package mz.org.fgh.idartlite.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.fgh.idartlite.BR;
import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.base.BaseActivity;
import mz.org.fgh.idartlite.base.BaseModel;
import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.common.DialogListener;
import mz.org.fgh.idartlite.common.Listble;
import mz.org.fgh.idartlite.common.ValorSimples;
import mz.org.fgh.idartlite.model.DispenseType;
import mz.org.fgh.idartlite.model.Drug;
import mz.org.fgh.idartlite.model.Patient;
import mz.org.fgh.idartlite.model.PrescribedDrug;
import mz.org.fgh.idartlite.model.Prescription;
import mz.org.fgh.idartlite.model.TherapeuticLine;
import mz.org.fgh.idartlite.model.TherapeuticRegimen;
import mz.org.fgh.idartlite.service.DispenseService;
import mz.org.fgh.idartlite.service.DispenseTypeService;
import mz.org.fgh.idartlite.service.DrugService;
import mz.org.fgh.idartlite.service.PrescriptionService;
import mz.org.fgh.idartlite.service.TherapeuthicLineService;
import mz.org.fgh.idartlite.service.TherapheuticRegimenService;
import mz.org.fgh.idartlite.util.Utilities;
import mz.org.fgh.idartlite.view.patient.PatientActivity;
import mz.org.fgh.idartlite.view.patient.PrescriptionActivity;
import mz.org.fgh.idartlite.view.patient.PrescriptionFragment;

public class PrescriptionVM extends BaseViewModel implements DialogListener {

    private PrescriptionService prescriptionService;

    private Prescription prescription;
    
    private TherapheuticRegimenService regimenService;
    private TherapeuthicLineService lineService;
    private DispenseTypeService dispenseTypeService;
    private DrugService drugService;

    private PrescriptionFragment relatedListingFragment;

    private DispenseService dispenseService;

    private boolean seeOnlyOfRegime;

    private boolean initialDataVisible;

    private boolean drugDataVisible;

    private boolean urgentPrescription;
    private boolean newPrescriptionMustBeEspetial;
    private Prescription oldPrescription;
    private List<ValorSimples> durations;

    private Drug selectedDrug;
    private List<ValorSimples> motives;

    private List<Listble> selectedDrugs;



    public PrescriptionVM(@NonNull Application application) {
        super(application);

        initNewPrescription();

        initServices(application);

        urgentPrescription = false;

        this.seeOnlyOfRegime = true;

        durations = new ArrayList<>();
        motives = new ArrayList<>();

        loadPrescriptionDuration();
        loadUrgenceMotives();

        initialDataVisible = true;
    }

    private void initNewPrescription() {
        this.prescription = new Prescription();

    }

    private void loadPrescriptionDuration(){
        durations.add(new ValorSimples());
        durations.add(ValorSimples.fastCreate(2, Prescription.DURATION_TWO_WEEKS));
        durations.add(ValorSimples.fastCreate(4, Prescription.DURATION_ONE_MONTH));
        durations.add(ValorSimples.fastCreate(8, Prescription.DURATION_TWO_MONTHS));
        durations.add(ValorSimples.fastCreate(12, Prescription.DURATION_THREE_MONTHS));
        durations.add(ValorSimples.fastCreate(16, Prescription.DURATION_FOUR_MONTHS));
        durations.add(ValorSimples.fastCreate(20, Prescription.DURATION_FIVE_MONTHS));
        durations.add(ValorSimples.fastCreate(24, Prescription.DURATION_SIX_MONTHS));
    }

    private void loadUrgenceMotives(){
        motives.add(new ValorSimples());
        motives.add(ValorSimples.fastCreate("Perda de Medicamento"));
        motives.add(ValorSimples.fastCreate("Ausencia do Clinico"));
        motives.add(ValorSimples.fastCreate("Laboratorio"));
        motives.add(ValorSimples.fastCreate("Rotura de Stock"));
        motives.add(ValorSimples.fastCreate("Outro"));
    }

    public void requestForNewRecord(){
        try {

            if (getRelatedListingFragment().getMyActivity().getPatient().hasEndEpisode()) {
                Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.cant_edit_patient_data)).show();
            } else {
                getCurrentStep().changeToInit();

                this.prescription = prescriptionService.getLastPatientPrescription(((PatientActivity) getRelatedActivity()).getPatient());


                this.prescription.setDispenses(dispenseService.getAllDispenseByPrescription(this.prescription));

                if (!this.prescription.isClosed()) {

                    this.prescription.setPrescribedDrugs(prescriptionService.getAllOfPrescription(this.prescription));

                    newPrescriptionMustBeEspetial = true;

                    //Utilities.displayConfirmationDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.new_prescription_creation), getRelatedActivity().getString(R.string.yes), getRelatedActivity().getString(R.string.no), PrescriptionVM.this).show();
                    Utilities.displayConfirmationDialog(getRelatedActivity(), this.prescription.getPrescriptionAsString(), getRelatedActivity().getString(R.string.yes), getRelatedActivity().getString(R.string.no), PrescriptionVM.this).show();
                } else {
                    doOnConfirmed();
                }
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void initServices(@NonNull Application application) {
        prescriptionService = new PrescriptionService(application, getCurrentUser());
        regimenService = new TherapheuticRegimenService(application, getCurrentUser());
        lineService = new TherapeuthicLineService(application, getCurrentUser());
        dispenseTypeService = new DispenseTypeService(application, getCurrentUser());
        drugService = new DrugService(application, getCurrentUser());
        dispenseService = new DispenseService(application, getCurrentUser());
    }

    public List<Prescription> gatAllOfPatient(Patient patient) throws SQLException {
        return prescriptionService.getAllPrescriptionsByPatient(patient);
    }

    public void create(Prescription prescription) throws SQLException {
        this.prescriptionService.createPrescription(prescription);
    }

    public void deletePrescription(Prescription prescription) throws SQLException {
        this.prescriptionService.deletePrescription(prescription);
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
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
    public boolean isDrugDataVisible() {
        return drugDataVisible;
    }

    @Override
    public BaseActivity getRelatedActivity() {
        return super.getRelatedActivity();
    }

    public void setDrugDataVisible(boolean drugDataVisible) {
        this.drugDataVisible = drugDataVisible;
        notifyPropertyChanged(BR.drugDataVisible);
    }

    public List<TherapeuticRegimen> getAllTherapeuticRegimen () throws SQLException {
        return regimenService.getAll();
    }

    public List<TherapeuticLine> getAllTherapeuticLines () throws SQLException {
        return lineService.getAll();
    }

    public List<DispenseType> getAllDispenseTypes () throws SQLException {
        return dispenseTypeService.getAll();
    }

    public List<Drug> getAllDrugs() throws SQLException {
        return drugService.getAll();
    }

    public List<Drug> getAllDrugsOfTheraputicRegimen(TherapeuticRegimen therapeuticRegimen) throws SQLException {
        return drugService.getAllOfTherapeuticRegimen(therapeuticRegimen);
    }

    public void setPrescriptionToSpetial(){
        urgentPrescription = !urgentPrescription;
        if (urgentPrescription) {
            this.prescription.setUrgentPrescription(Prescription.URGENT_PRESCRIPTION);

        }else {
            this.prescription.setUrgentPrescription(Prescription.NOT_URGENT_PRESCRIPTION);
        }

        ((PrescriptionActivity)getRelatedActivity()).changeMotiveSpinnerStatus(urgentPrescription);
    }

    private PrescribedDrug initNewPrescribedDrug(Drug drug) {
        return  new PrescribedDrug(drug, getPrescription());
    }

    private void doSave(){
        if (getCurrentStep().isApplicationStepSave()) getPrescription().generateNextSeq();

        List<PrescribedDrug> prescribedDrugs = new ArrayList<>();

        for (Listble drug : selectedDrugs){
            prescribedDrugs.add(initNewPrescribedDrug((Drug) drug));
        }

        getPrescription().setPrescribedDrugs(prescribedDrugs);

        if (getCurrentStep().isApplicationStepSave()) getPrescription().setUuid(Utilities.getNewUUID().toString());

        getPrescription().setSyncStatus(BaseModel.SYNC_SATUS_READY);

        if (newPrescriptionMustBeEspetial && !this.prescription.isUrgent()){
            Utilities.displayAlertDialog(getRelatedActivity(),getRelatedActivity().getString(R.string.prescription_must_be_urgent)).show();
            return;
        }

        String validationErrors = this.prescription.validate(getRelatedActivity());
        if (!Utilities.stringHasValue(validationErrors)) {
            try {
                if (getRelatedActivity().getApplicationStep().isApplicationStepSave()) {
                    if (oldPrescription != null) prescriptionService.closePrescription(oldPrescription);
                    
                    prescriptionService.createPrescription(this.prescription);
                    Utilities.displayConfirmationDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.would_like_to_dispense), getRelatedActivity().getString(R.string.yes), getRelatedActivity().getString(R.string.no), ((PrescriptionActivity)getRelatedActivity())).show();
                } else if (getRelatedActivity().getApplicationStep().isApplicationStepEdit()) {
                    prescriptionService.updatePrescription(this.prescription);
                    Utilities.displayAlertDialog(getRelatedActivity(), "A Prescrição foi actualizada com sucesso.", ((PrescriptionActivity)getRelatedActivity())).show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.save_error_msg) + e.getLocalizedMessage()).show();
            }
        }else {
            Utilities.displayAlertDialog(getRelatedActivity(), validationErrors).show();
        }
    }

    public void save(){
        getCurrentStep().changeToSave();

        Utilities.displayConfirmationDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.confirm_prescription_save), getRelatedActivity().getString(R.string.yes), getRelatedActivity().getString(R.string.no), PrescriptionVM.this).show();

    }

    public Prescription getLastPatientPrescription(Patient patient) throws SQLException {

        return  this.prescriptionService.getLastPatientPrescription(patient);
    }

    public String checkPrescriptionRemoveConditions() {
        try {
            if (!this.prescription.isSyncStatusReady(this.prescription.getSyncStatus())) return getRelatedActivity().getString(R.string.prescription_cant_be_removed_msg);
            if (prescriptionHasDispenses()) return getRelatedActivity().getString(R.string.prescription_got_dispenses_msg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    private boolean prescriptionHasDispenses() throws SQLException {
        return dispenseService.countAllOfPrescription(this.prescription) > 0;
    }

    public void loadPrescribedDrugsOfPrescription() throws SQLException {
        this.prescription.setPrescribedDrugs(prescriptionService.getAllOfPrescription(this.prescription));
    }

    public void loadLastPatientPrescription() throws SQLException {
        this.prescription = prescriptionService.getLastPatientPrescription(this.prescription.getPatient());
        this.prescription.setPrescribedDrugs(prescriptionService.getAllOfPrescription(this.prescription));
        this.prescription.setId(0);
    }

    @Bindable
    public boolean isSeeOnlyOfRegime() {
        return seeOnlyOfRegime;
    }

    public void setSeeOnlyOfRegime(boolean seeOnlyOfRegime) {
        this.seeOnlyOfRegime = seeOnlyOfRegime;
        notifyPropertyChanged(BR.seeOnlyOfRegime);
    }

    public void changeDrugsListingMode(){
        this.seeOnlyOfRegime = !this.seeOnlyOfRegime;

        loadDrugs();
    }

    public void loadDrugs() {
        reloadDrugsSpinnerByRegime(this.prescription.getTherapeuticRegimen());
    }

    private void reloadDrugsSpinnerByRegime(TherapeuticRegimen regimen) {
        List<Drug> drugs = new ArrayList<>();
        try {
            if (regimen != null && isSeeOnlyOfRegime()) {
                drugs.addAll(getDrugsWithoutRectParanthesis(getAllDrugsOfTheraputicRegimen(regimen)));
            }
            else {
                drugs.addAll(getDrugsWithoutRectParanthesis(getAllDrugs()));
            }
            ((PrescriptionActivity)getRelatedActivity()).populateDrugs(drugs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String prescriptionCanBeEdited() throws SQLException {
        if (this.prescription.getExpiryDate() != null) return "Não é possivel alterar a prescrição "+this.prescription.getUiId()+ ", pois encontra-se expirada.";
        if (prescriptionHasDispenses()) return getRelatedActivity().getString(R.string.cant_edit_prescription_msg);
        if (!this.prescription.isSyncStatusReady(this.prescription.getSyncStatus())) return getRelatedActivity().getString(R.string.cant_edit_synced_prescription);
        return "";
    }

    public PrescriptionFragment getRelatedListingFragment() {
        return relatedListingFragment;
    }

    public void setRelatedListingFragment(PrescriptionFragment relatedListingFragment) {
        this.relatedListingFragment = relatedListingFragment;
    }

    @Override
    public void doOnConfirmed() {
        if (getCurrentStep().isApplicationStepInit()) {
            initNewRecord();
        }else if (getCurrentStep().isApplicationStepSave()){
            doSave();
        }
    }

    public List<Drug> getDrugsWithoutRectParanthesis(List<Drug> drugs) throws SQLException {
        return drugService.getDrugsWithoutRectParanthesis(drugs);
    }

    private void initNewRecord() {
        initNewPrescription();
        this.prescription.setPatient(((PatientActivity) getRelatedActivity()).getPatient());

        getRelatedListingFragment().startPrescriptionActivity();
    }

    @Override
    public void doOnDeny() {
        if (getCurrentStep().isApplicationStepInit()) {
            getCurrentStep().changeToList();
        }else if (getCurrentStep().isApplicationStepSave()){
            getCurrentStep().changetocreate();
        }
    }

    public void backToPreviusActivity(){
        Map<String, Object> params = new HashMap<>();
        params.put("patient", getPrescription().getPatient());
        params.put("user", getCurrentUser());
        params.put("clinic", getCurrentClinic());
        params.put("requestedFragment", PrescriptionFragment.FRAGMENT_CODE_PRESCRIPTION);
        getRelatedActivity().nextActivityFinishingCurrent(PatientActivity.class,params);
    }

    public void checkIfMustBeUrgentPrescription() throws SQLException {
        oldPrescription = prescriptionService.getLastPatientPrescription(this.prescription.getPatient());

        oldPrescription.setDispenses(dispenseService.getAllDispenseByPrescription(oldPrescription));

        if (!oldPrescription.isClosed()) {
            newPrescriptionMustBeEspetial = true;
        }
    }

    public void setPrescriptionSupply(Listble supply){
        getPrescription().setSupply(supply.getId());
        notifyPropertyChanged(BR.prescriptionSupply);
    }

    @Bindable
    public Listble getPrescriptionSupply(){
        return Utilities.findOnArray(this.durations, ValorSimples.fastCreate(getPrescription().getSupply()));
    }

    @Bindable
    public Listble getSelectedDrug() {
        return selectedDrug;
    }

    public void setUrgentNotes(Listble supply){
        getPrescription().setUrgentNotes(supply.getDescription());
        notifyPropertyChanged(BR.urgentNotes);
    }

    @Bindable
    public Listble getUrgentNotes(){
        return Utilities.findOnArray(this.motives, ValorSimples.fastCreate(getPrescription().getUrgentNotes()));
    }

    public void setSelectedDrug(Listble selectedDrug) {
        this.selectedDrug = (Drug) selectedDrug;
        notifyPropertyChanged(BR.selectedDrug);
    }

    @Bindable
    public Listble getPrescriptionDispenseType(){
        return this.prescription.getDispenseType();
    }

    public void setPrescriptionDispenseType(Listble precriptionType){
        this.prescription.setDispenseType((DispenseType) precriptionType);
        notifyPropertyChanged(BR.prescriptionDispenseType);
    }

    @Bindable
    public Listble getPrescriptionRegimen(){
        return this.prescription.getTherapeuticRegimen();
    }

    public void setPrescriptionRegimen(Listble regimen){
        this.prescription.setTherapeuticRegimen((TherapeuticRegimen) regimen);
        notifyPropertyChanged(BR.prescriptionRegimen);
    }

    @Bindable
    public Listble getPrescriptionLine(){
        return this.prescription.getTherapeuticLine();
    }

    public void setPrescriptionLine(Listble line){
        this.prescription.setTherapeuticLine((TherapeuticLine) line);
        notifyPropertyChanged(BR.prescriptionLine);
    }

    public void addSelectedDrug(){
        if (selectedDrugs == null) selectedDrugs = new ArrayList<>();

            if (!selectedDrugs.contains(selectedDrug)) {
                selectedDrug.setListPosition(selectedDrugs.size()+1);
                selectedDrugs.add(selectedDrug);
                Collections.sort(selectedDrugs);

                ((PrescriptionActivity) getRelatedActivity()).displaySelectedDrugs();

                setSelectedDrug(null);

                notifyPropertyChanged(BR.selectedDrug);
            }else {
                Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.drug_data_duplication_msg)).show();
            }
    }

    public List<Listble> getSelectedDrugs() {
        return selectedDrugs;
    }

    public List<ValorSimples> getDurations() {
        return durations;
    }

    public List<ValorSimples> getMotives() {
        return motives;
    }

    public void setSelectedDrugs(List<Listble> drugs) {
        this.selectedDrugs = drugs;
    }

    public void changeInitialDataViewStatus(View view){
        ((PrescriptionActivity) getRelatedActivity()).changeFormSectionVisibility(view);
    }

    public void setPrescriptionDate(Date date) {
        this.prescription.setPrescriptionDate(date);
        notifyPropertyChanged(BR.prescriptionDate);
    }

    @Bindable
    public Date getPrescriptionDate() {
        return this.prescription.getPrescriptionDate();
    }
}

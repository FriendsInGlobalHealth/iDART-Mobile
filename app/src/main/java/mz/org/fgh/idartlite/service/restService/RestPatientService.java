package mz.org.fgh.idartlite.service.restService;

import android.app.Application;
import android.util.Log;

import androidx.collection.ArrayMap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;
import java.util.Map;

import mz.org.fgh.idartlite.base.BaseService;
import mz.org.fgh.idartlite.base.RestResponseListener;
import mz.org.fgh.idartlite.model.Clinic;
import mz.org.fgh.idartlite.model.User;
import mz.org.fgh.idartlite.rest.RESTServiceHandler;
import mz.org.fgh.idartlite.service.ClinicService;
import mz.org.fgh.idartlite.service.PatientService;

public class RestPatientService extends BaseService {

    private static final String TAG = "RestPatientService";
    private static ClinicService clinicService ;
    private static PatientService patientService;

    public RestPatientService(Application application, User currentUser) {
        super(application, currentUser);

        clinicService = new ClinicService(getApplication(),null);
        patientService = new PatientService(getApplication(),null);
    }

    public static void restGetAllPatient(RestResponseListener listener) {

        try {
            clinicService = new ClinicService(getApp(),null);

            Clinic clinic = clinicService.getCLinic().get(0);

        patientService = new PatientService(getApp(),null);

        String url = BaseService.baseUrl + "/sync_temp_patients?clinicuuid=eq."+clinic.getUuid()+"&syncstatus=eq.P&uuidopenmrs=not.in.(null,\"NA\")";

        if (RESTServiceHandler.getServerStatus(BaseService.baseUrl)) {
            getRestServiceExecutor().execute(() -> {

                RESTServiceHandler handler = new RESTServiceHandler();

                Map<String, Object> params = new ArrayMap<String, Object>();
                handler.addHeader("Content-Type", "Application/json");

                handler.objectRequest(url, Request.Method.GET, params, Object[].class, new Response.Listener<Object[]>() {
                    @Override
                    public void onResponse(Object[] patients) {
                        Log.d("Response", String.valueOf(patients.length));

                        if (patients.length > 0) {
                            for (Object patient : patients) {
                                Log.i(TAG, "onResponse: " + patient);
                                try {
                                    LinkedTreeMap<String, Object> itemresult = (LinkedTreeMap<String, Object>) patient;
                                    if(!patientService.checkPatient(itemresult)){
                                        patientService.saveOnPatient(itemresult);
                                    }else{
                                        Log.i(TAG, "onResponse: "+patient+" Ja Existe");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }finally {
                                    continue;
                                }
                            }
                        }else {
                            Log.w(TAG, "Response Sem Info." + patients.length);
                        }

                        if(listener != null){
                            listener.doOnRestSucessResponse(null);
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.getMessage());
                    }
                });
            });

        } else {
            Log.e(TAG,"Response Servidor Offline");
        }
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}

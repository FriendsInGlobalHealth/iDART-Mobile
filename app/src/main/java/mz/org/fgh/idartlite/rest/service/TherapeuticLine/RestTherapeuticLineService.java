package mz.org.fgh.idartlite.rest.service.TherapeuticLine;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import mz.org.fgh.idartlite.base.rest.BaseRestService;
import mz.org.fgh.idartlite.base.rest.ServiceWatcher;
import mz.org.fgh.idartlite.listener.rest.RestResponseListener;
import mz.org.fgh.idartlite.model.User;
import mz.org.fgh.idartlite.rest.helper.RESTServiceHandler;
import mz.org.fgh.idartlite.service.drug.ITherapeuthicLineService;
import mz.org.fgh.idartlite.service.drug.TherapeuthicLineService;

public class RestTherapeuticLineService extends BaseRestService {

    private static final String TAG = "RestTherapeuticLineServ";
    private static ITherapeuthicLineService therapeuticLineService;


    public RestTherapeuticLineService(Application application, User currentUser) {
        super(application, currentUser);

        therapeuticLineService = new TherapeuthicLineService(application,currentUser);
    }

    public static void restGetAllTherapeuticLine()  {
        getAllTherapeuticLine(null);
    }

    public static void restGetAllTherapeuticLine(RestResponseListener listener)  {
        getAllTherapeuticLine(listener);
    }


    public static void getAllTherapeuticLine(RestResponseListener listener) {

        String url = BaseRestService.baseUrl + "/linhat";
        therapeuticLineService = new TherapeuthicLineService(getApp(),null);

        ServiceWatcher serviceWatcher = ServiceWatcher.fastCreate(TAG, url);

        serviceWatcher.setServiceAsRunning();

        if (listener != null) listener.registRunningService(serviceWatcher);

            getRestServiceExecutor().execute(() -> {

                RESTServiceHandler handler = new RESTServiceHandler();
                handler.addHeader("Content-Type", "Application/json");

                handler.objectRequest(url, Request.Method.GET, null, Object[].class, new Response.Listener<Object[]>() {
                    @Override
                    public void onResponse(Object[] linhasTerapeuticas) {

                        if (linhasTerapeuticas.length > 0) {

                            int counter = 0;

                            for (Object line : linhasTerapeuticas) {
                                Log.i(TAG, "onResponse: " + line);
                                try {
                                    if(!therapeuticLineService.checkLine(line)){
                                        therapeuticLineService.saveLine(line);
                                        counter++;
                                    }else{
                                        Log.i(TAG, "onResponse: "+line+" Ja Existe");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }finally {
                                    continue;
                                }
                            }
                            if (counter > 0) serviceWatcher.setUpdates(counter +" novas Linhas Terapéuticas");
                        }else
                            Log.w(TAG, "Response Sem Info." + linhasTerapeuticas.length);

                        serviceWatcher.setServiceAsStopped();
                        if (listener != null) listener.updateServiceStatus(serviceWatcher);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        serviceWatcher.setServiceAsStopped();
                        if (listener != null) listener.updateServiceStatus(serviceWatcher);

                        Log.e("Response", generateErrorMsg(error));
                    }
                });
            });
    }
}

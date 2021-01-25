package mz.org.fgh.idartlite.base.rest;

import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.concurrent.ExecutorService;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.base.service.ServiceProvider;
import mz.org.fgh.idartlite.model.Clinic;
import mz.org.fgh.idartlite.model.User;
import mz.org.fgh.idartlite.rest.helper.ExecutorThreadProvider;
import mz.org.fgh.idartlite.util.Utilities;

public abstract class BaseRestService {

    protected Application application;
    protected User currentUser;
    protected Clinic currentClinic;

    public static Application app;

    protected static ServiceProvider serviceFactory;

    protected static ExecutorService restServiceExecutor;
    //public static final String baseUrl = "http://dev.fgh.org.mz:3110";

    public static final String baseUrl = "http://192.168.1.163:3009";

    public BaseRestService(Application application, User currentUser, Clinic currentClinic) {
        init(application,currentUser, currentClinic);
    }

    public BaseRestService(Application application, User currentUser) {
        init(application,currentUser, null);
    }

    public BaseRestService(Application application) {
       init(application,null, null);
    }

    public static ExecutorService getRestServiceExecutor() {
        return restServiceExecutor;
    }

    private void init(Application application, User currentUser, Clinic currentClinic){
        restServiceExecutor = ExecutorThreadProvider.getInstance().getExecutorService();

        serviceFactory = ServiceProvider.getInstance(application);

        this.currentClinic = currentClinic;
        this.currentUser = currentUser;
        this.application = application;
        app = application;
    }

    public static ServiceProvider getServiceFactory() {
        return serviceFactory;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Application getApplication(){
        return application;
    }
    public static  Application getApp(){
        return app;
    }

    protected static String generateErrorMsg(VolleyError error){
        if (error instanceof NetworkError) {
            return "A network error as occured ";
        } else if (error instanceof ServerError) {
            return "A server error as occured ";
        } else if (error instanceof AuthFailureError) {
            return "An authentication error as occured ";
        } else if (error instanceof ParseError) {
            return "A parse error as occured ";
        } else if (error instanceof NoConnectionError) {
            return "No connection ";
        } else if (error instanceof TimeoutError) {
            return "Connection timeout";
        }
        return Utilities.stringHasValue(error.getMessage()) ? error.getMessage() : getApp().getString(R.string.unknown_error);
    }
}

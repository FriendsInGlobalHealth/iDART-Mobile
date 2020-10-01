package mz.org.fgh.idartlite.service;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.sql.SQLException;

import mz.org.fgh.idartlite.base.BaseService;
import mz.org.fgh.idartlite.base.RestResponseListener;
import mz.org.fgh.idartlite.model.User;
import mz.org.fgh.idartlite.rest.RESTServiceHandler;
import mz.org.fgh.idartlite.util.Utilities;

public class UserService extends BaseService {

    private static final String TAG = "UserService";
    public static final String auth = "USER_AUTHENTIC";
    public static final String[] nonauth = {"NOT_AUTHENTIC"};

    public UserService(Application application, User currUser) {
        super(application, currUser);
    }

    public boolean login(User user) throws SQLException {
        return getDataBaseHelper().getUserDao().login(user);
    }

    public boolean checkIfUsertableIsEmpty() throws SQLException {
        return getDataBaseHelper().getUserDao().checkIfUsertableIsEmpty();
    }

    public void saveUser(User user) throws SQLException {
        getDataBaseHelper().getUserDao().saveGenericObjectByClass(user);
    }

    public String getUserAuthentication(String clinicuuid, String username, String password, RestResponseListener listener) {

        final String[] result = {null};

        if (RESTServiceHandler.getServerStatus(BaseService.baseUrl)) {
            getRestServiceExecutor().execute(() -> {
                String url = BaseService.baseUrl + "/users?select=*,clinic(*)&cl_username=eq." + username + "&cl_password=eq." + Utilities.MD5Crypt(password) + "&clinic.uuid=eq." + clinicuuid;

                RESTServiceHandler handler = new RESTServiceHandler();

                handler.addHeader("Content-Type", "Application/json");

                handler.objectRequest(url, Request.Method.GET, null, Object[].class, new Response.Listener<Object[]>() {
                    @Override
                    public void onResponse(Object[] response) {


                        if (response.length > 0) {
                          result[0] = auth;
                        }else {
                            result[0] = nonauth[0];
                        }

                        listener.doOnRestSucessResponse(result[0]);

                        Log.d("Response", response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.getMessage());
                    }
                });
            });
        } else {
            Log.e(TAG, "Response Servidor Offline");
            Toast.makeText(getApplication(), "Servidor offline, por favor tente mais tarde", Toast.LENGTH_LONG).show();

        }
        return result[0];
    }
}

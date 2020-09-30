package mz.org.fgh.idartlite.view;

import android.os.Bundle;

import mz.org.fgh.idartlite.R;
import mz.org.fgh.idartlite.TaskSchedule.restTaskSchedule.ExecuteGetWorkerScheduler;
import mz.org.fgh.idartlite.base.BaseActivity;
import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.base.RestResponseListener;
import mz.org.fgh.idartlite.service.ClinicService;
import mz.org.fgh.idartlite.service.UserService;
import mz.org.fgh.idartlite.service.restService.RestPatientService;

public class SecondSplashActivity extends BaseActivity implements RestResponseListener {

    private ClinicService clinicService;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);

        ExecuteGetWorkerScheduler executeGetWorkerScheduler = new ExecuteGetWorkerScheduler(getApplicationContext());
        executeGetWorkerScheduler.initConfigTaskWork();
        executeGetWorkerScheduler.initDataTaskWork();

        new Thread(new Runnable() {
            public void run() {

                RestPatientService.restGetAllPatient(SecondSplashActivity.this);

            }
        }).start();

    }

    @Override
    public BaseViewModel initViewModel() {
        return null;
    }

    @Override
    public void doOnRestSucessResponse(String flag) {
        nextActivity(HomeActivity.class);
        finish();
    }

    @Override
    public void doOnRestErrorResponse(String errormsg) {

    }
}
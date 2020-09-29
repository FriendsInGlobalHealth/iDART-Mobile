package mz.org.fgh.idartlite.service;

import android.app.Application;

import com.google.gson.internal.LinkedTreeMap;

import mz.org.fgh.idartlite.base.BaseService;
import mz.org.fgh.idartlite.model.DispenseType;
import mz.org.fgh.idartlite.model.TherapeuticLine;
import mz.org.fgh.idartlite.model.User;

import java.sql.SQLException;
import java.util.List;

import static mz.org.fgh.idartlite.model.DiseaseType.COLUMN_DESCRIPTION;

public class DispenseTypeService extends BaseService {

    public DispenseTypeService(Application application, User currUser) {
        super(application, currUser);
    }

    public void createDispenseType(DispenseType dispenseType) throws SQLException {
        getDataBaseHelper().getDispenseTypeDao().create(dispenseType);
    }

    public List<DispenseType> getAll() throws SQLException {
        return getDataBaseHelper().getDispenseTypeDao().queryForAll();
    }

    public DispenseType getDispenseType(String code) throws SQLException {

        List<DispenseType> typeList = getDataBaseHelper().getDispenseTypeDao().queryForEq(COLUMN_DESCRIPTION,code);

        if (typeList != null)
            if (!typeList.isEmpty())
                return typeList.get(0);

        return null;
    }

    public boolean checkDipsenseType(Object dispenseType) {

        boolean result = false;

        LinkedTreeMap<String, Object> itemresult = (LinkedTreeMap<String, Object>) dispenseType;

        try {

            DispenseType localDispenseType = getDispenseType(itemresult.get("value").toString());

            if(localDispenseType != null)
                result = true;

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


    public void saveDispoenseType(Object dispenseType){

        DispenseType loalDispensetype = new DispenseType();
        try {
            LinkedTreeMap<String, Object> itemresult = (LinkedTreeMap<String, Object>) dispenseType;

            loalDispensetype.setCode(String.valueOf(itemresult.get("name")));
            loalDispensetype.setDescription(String.valueOf(itemresult.get("value")));
            createDispenseType(loalDispensetype);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

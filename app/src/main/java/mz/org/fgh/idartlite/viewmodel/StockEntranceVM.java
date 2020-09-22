package mz.org.fgh.idartlite.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import java.sql.SQLException;
import java.util.List;

import mz.org.fgh.idartlite.base.BaseViewModel;
import mz.org.fgh.idartlite.model.Clinic;
import mz.org.fgh.idartlite.model.Stock;
import mz.org.fgh.idartlite.service.StockService;

public class StockEntranceVM extends BaseViewModel {

    private StockService stockService;

    public StockEntranceVM(@NonNull Application application) {
        super(application);
        stockService = new StockService(getApplication(), getCurrentUser());
    }

    public List<Stock> getStockByClinic(Clinic clinic) throws SQLException {
        return stockService.getStockByClinic(clinic);
    }
}

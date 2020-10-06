package mz.org.fgh.idartlite.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import mz.org.fgh.idartlite.model.Clinic;
import mz.org.fgh.idartlite.model.DispensedDrug;
import mz.org.fgh.idartlite.model.Drug;
import mz.org.fgh.idartlite.model.Stock;

import java.sql.SQLException;
import java.util.List;

public class StockDaoImpl extends GenericDaoImpl<Stock, Integer> implements StockDao {

    public StockDaoImpl(Class dataClass) throws SQLException {
        super(dataClass);
    }

    public StockDaoImpl(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public StockDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }


    @Override
    public List<Stock> getStockByClinic(Clinic clinic) throws SQLException {
        return queryBuilder().distinct().selectColumns(Stock.COLUMN_ORDER_NUMBER).where().eq(Stock.COLUMN_CLINIC, clinic.getId()).query();
    }

    @Override
    public List<Stock> getStockByOrderNumber(String orderNumber, Clinic clinic) throws SQLException {
        return queryBuilder().where().eq(Stock.COLUMN_CLINIC, clinic.getId()).and().eq(Stock.COLUMN_ORDER_NUMBER, orderNumber).query();
    }

    @Override
    public List<Stock> getAllStocksByClinicAndDrug(Clinic clinic, Drug drug) throws SQLException {
        return queryBuilder().where().eq(Stock.COLUMN_CLINIC,clinic.getId()).and().eq(Stock.COLUMN_DRUG, drug.getId()).query();
    }

    @Override
    public void updateStock(DispensedDrug dispensedDrug) throws SQLException {
        Stock stock = dispensedDrug.getStock();

        int actualStockMoviment = stock.getStockMoviment();

        int quantitySupplied = dispensedDrug.getQuantitySupplied();

        int remainingStock = actualStockMoviment - quantitySupplied;

        stock.setStockMoviment(remainingStock);
        stock.setSyncStatus(Stock.SYNC_SATUS_READY);

        createOrUpdate(stock);
    }
}

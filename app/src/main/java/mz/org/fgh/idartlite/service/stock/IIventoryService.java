package mz.org.fgh.idartlite.service.stock;

import java.sql.SQLException;
import java.util.List;

import mz.org.fgh.idartlite.base.service.IBaseService;
import mz.org.fgh.idartlite.model.Drug;
import mz.org.fgh.idartlite.model.Stock;
import mz.org.fgh.idartlite.model.StockAjustment;
import mz.org.fgh.idartlite.model.inventory.Iventory;

public interface IIventoryService extends IBaseService<Iventory> {

    List<Iventory> getAllWithPagination(long offset, long limit) throws SQLException;

    void initInventory(Iventory record) throws SQLException;

    void closeInventory(Iventory record) throws SQLException;

    boolean isLastInventoryOpen() throws SQLException;

    List<StockAjustment> getAllStockAjustmentsOfInventory(Iventory selectedRecord) throws SQLException;

    List<Stock> getAllOfDrug(Drug selectedDrug) throws SQLException;

    List<Drug> getAllDrugsWithExistingLote() throws SQLException;
}

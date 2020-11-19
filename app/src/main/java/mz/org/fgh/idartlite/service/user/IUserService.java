package mz.org.fgh.idartlite.service.user;

import java.sql.SQLException;

import mz.org.fgh.idartlite.base.service.IBaseService;
import mz.org.fgh.idartlite.model.User;


public interface IUserService extends IBaseService<User> {

    public boolean login(User user) throws SQLException;

    public boolean checkIfUsertableIsEmpty() throws SQLException ;

    public void saveUser(User user) throws SQLException ;

}

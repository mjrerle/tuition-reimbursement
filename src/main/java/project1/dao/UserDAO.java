package project1.dao;

import java.sql.SQLException;
import project1.utils.ReimbursementLogger;

import org.apache.log4j.Logger;
import project1.model.User;

public class UserDAO implements IUser {
    public static Logger logger = ReimbursementLogger.logger;
    @Override
    public User getUser(String username, String password) {
        try {
            
        } catch(SQLException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public User getUser(int u_id) {
        return null;
    }

}

package org.example.repository;

import org.example.model.User;
import org.example.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDbRepository implements IUserRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public UserDbRepository(Properties props) {
        logger.info("Initializing UserDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(User elem) {
        logger.traceEntry("saving user {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into users(first_name, last_name, email, password) values (?, ?, ?, ?)")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getEmail());
            preStmt.setString(4, elem.getPassword());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(User elem) {
        logger.traceEntry("deleting user {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from users where id_user=?")) {
            preStmt.setInt(1, elem.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(User elem, Integer id) {
        logger.traceEntry("updating user with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update users set first_name=?, last_name=?, email=?, password=? where id_user=?")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getEmail());
            preStmt.setString(4, elem.getPassword());
            preStmt.setInt(5, id);
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public User findById(Integer id) {
        logger.traceEntry("finding user with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from users where id_user=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result=preStmt.executeQuery()){
                String first_name = result.getString("first_name");
                String last_name = result.getString("last_name");
                String email = result.getString("email");
                String password = result.getString("password");
                User user = new User(first_name, last_name, email, password);
                user.setId(id);
                logger.trace("Found {} instance", result);
                return user;
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry("finding all the users");
        Connection con= dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from users")) {
            try(ResultSet result=preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id_user");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    User user = new User(first_name, last_name, email, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public User findByEmail(String email) {
        logger.traceEntry("finding user with the email {} ", email);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from users where email=?")) {
            preStmt.setString(1, email);
            try(ResultSet result=preStmt.executeQuery()){
                Integer id = result.getInt("id_user");
                String first_name = result.getString("first_name");
                String last_name = result.getString("last_name");
                String password = result.getString("password");
                if(first_name!=null && last_name!=null && password!=null) {
                    User user = new User(first_name, last_name, email, password);
                    user.setId(id);
                    logger.trace("Found {} instance", result);
                    return user;
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User findByEmailPassword(String email, String password) {
        logger.traceEntry("finding user with the email {} and password {}", email, password);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from users where email=? and password=?")) {
            preStmt.setString(1, email);
            preStmt.setString(2, password);
            try(ResultSet result=preStmt.executeQuery()){
                Integer id = result.getInt("id_user");
                String first_name = result.getString("first_name");
                String last_name = result.getString("last_name");
                if(first_name!=null && last_name!=null) {
                    User user = new User(first_name, last_name, email, password);
                    user.setId(id);
                    logger.trace("Found {} instance", result);
                    return user;
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }
}
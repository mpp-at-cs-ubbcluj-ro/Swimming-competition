package org.example.repository;

import org.example.model.Swimmer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SwimmerDbRepository implements ISwimmerRepository{
    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public SwimmerDbRepository(Properties props) {
        logger.info("Initializing SwimmerDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(Swimmer elem) {
        logger.traceEntry("saving swimmer {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into swimmers(first_name, last_name, birth_date) values (?, ?, ?)")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getBirthDate().toString());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Swimmer elem) {
        logger.traceEntry("deleting swimmer {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from swimmers where id_swimmer=?")) {
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
    public void update(Swimmer elem, Integer id) {
        logger.traceEntry("updating swimmer with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update swimmers set first_name=?, last_name=?, birth_date=? where id_swimmer=?")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getBirthDate().toString());
            preStmt.setInt(4, id);
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Swimmer findById(Integer id) {
        logger.traceEntry("finding swimmer with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from swimmers where id_swimmer=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result=preStmt.executeQuery()){
                String first_name = result.getString("first_name");
                String last_name = result.getString("last_name");
                String birth_date_string = result.getString("birth_date");
                LocalDate birth_date = LocalDate.parse(birth_date_string);
                //LocalDateTime birthDate = birth_date.atTime(0,0,0);
                Swimmer swimmer = new Swimmer(first_name, last_name, birth_date);
                swimmer.setId(id);
                logger.trace("Found {} instance", result);
                return swimmer;
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Swimmer> findAll() {
        logger.traceEntry("finding all the swimmers");
        Connection con= dbUtils.getConnection();
        List<Swimmer> swimmers = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from swimmers")) {
            try(ResultSet result=preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id_swimmer");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    String birth_date_string = result.getString("birth_date");
                    LocalDate birth_date = LocalDate.parse(birth_date_string);
                    Swimmer swimmer = new Swimmer(first_name, last_name, birth_date);
                    swimmer.setId(id);
                    swimmers.add(swimmer);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(swimmers);
        return swimmers;
    }
}

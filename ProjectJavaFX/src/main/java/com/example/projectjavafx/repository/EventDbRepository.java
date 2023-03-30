package com.example.projectjavafx.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.projectjavafx.model.Event;
import com.example.projectjavafx.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventDbRepository implements IEventRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public EventDbRepository(Properties props) {
        logger.info("Initializing EventDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(Event elem) {
        logger.traceEntry("saving event {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into events(distance, style) values (?, ?)")) {
            preStmt.setInt(1, elem.getDistance());
            preStmt.setString(2, elem.getStyle());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Event elem) {
        logger.traceEntry("deleting event {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from events where id_event=?")) {
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
    public void update(Event elem, Integer id) {
        logger.traceEntry("updating event with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update events set distance=?, style=? where id_event=?")) {
            preStmt.setInt(1, elem.getDistance());
            preStmt.setString(2, elem.getStyle());
            preStmt.setInt(3, id);
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Event findById(Integer id) {
        logger.traceEntry("finding event with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from events where id_event=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result=preStmt.executeQuery()){
                Integer distance = result.getInt("distance");
                String style = result.getString("style");
                Event event = new Event(distance, style);
                event.setId(id);
                logger.trace("Found {} instance", result);
                return event;
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        logger.traceEntry("finding all the events");
        Connection con= dbUtils.getConnection();
        List<Event> events = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from events")) {
            try(ResultSet result=preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id_event");
                    Integer distance= result.getInt("distance");
                    String style = result.getString("style");
                    Event event = new Event(distance, style);
                    event.setId(id);
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(events);
        return events;
    }
}

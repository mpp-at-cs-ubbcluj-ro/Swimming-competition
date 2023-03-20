package org.example.repository;

import org.example.model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Participation;
import org.example.model.Swimmer;
import org.example.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class ParticipationDbRepository implements IParticipationRepository{
    private final JdbcUtils dbUtils;
    private EventDbRepository eventRepo;
    private SwimmerDbRepository swimmerRepo;
    private static final Logger logger= LogManager.getLogger();
    public ParticipationDbRepository(Properties props, EventDbRepository eventRepository, SwimmerDbRepository swimmerRepository) {
        logger.info("Initializing ParticipationDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        eventRepo = eventRepository;
        swimmerRepo = swimmerRepository;
    }
    @Override
    public Iterable<Participation> findBySwimmer(Swimmer swimmer) {
        return null;
    }

    @Override
    public Iterable<Participation> findByEvent(Event event) {
        return null;
    }

    @Override
    public void add(Participation elem) {
        logger.traceEntry("saving participation {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into participations(id_swimmer, id_event) values (?, ?)")) {
            preStmt.setInt(1, elem.getSwimmer().getId());
            preStmt.setInt(2, elem.getEvent().getId());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Participation elem) {
        logger.traceEntry("deleting participation {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from participations where id_participation=?")) {
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
    public void update(Participation elem, Integer id) {
        logger.traceEntry("updating participation with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update participations set id_swimmer=?, id_event=? where id_participation=?")) {
            preStmt.setInt(1, elem.getSwimmer().getId());
            preStmt.setInt(2, elem.getEvent().getId());
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
    public Participation findById(Integer id) {
        logger.traceEntry("finding participation with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from participations where id_participation=?")) {
            try(ResultSet result=preStmt.executeQuery()){
                Integer id_swimmer = result.getInt("id_swimmer");
                Integer id_event = result.getInt("id_event");
                Swimmer swimmer = swimmerRepo.findById(id_swimmer);
                Event event = eventRepo.findById(id_event);
                Participation participation = new Participation(swimmer, event);
                participation.setId(id);
                logger.trace("Found {} instance", result);
                return participation;
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Participation> findAll() {
        logger.traceEntry("finding all the participations");
        Connection con= dbUtils.getConnection();
        List<Participation> participations = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from participations")) {
            try(ResultSet result=preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id_participation");
                    Integer id_swimmer= result.getInt("id_swimmer");
                    Integer id_event = result.getInt("id_event");
                    Swimmer swimmer = swimmerRepo.findById(id_swimmer);
                    Event event = eventRepo.findById(id_event);
                    Participation participation = new Participation(swimmer, event);
                    participation.setId(id);
                    participations.add(participation);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(participations);
        return participations;
    }
}

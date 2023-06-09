package com.emsi.dao.impl;

import com.emsi.dao.ProducerDao;
import com.emsi.entities.Producer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducerDaoImp implements ProducerDao {

    private Connection conn= DB.getConnection();
    @Override
    public void insert(Producer producer) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("INSERT INTO producer (Name,CIN,Adress,PhoneNumber) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, producer.getName());
            ps.setString(2, producer.getCIN());
            ps.setString(3, producer.getAddress());
            ps.setInt(4, producer.getPhoneNumber());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);

                    producer.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                System.out.println("Aucune ligne renvoyée");
            }
        } catch (SQLException e) {
            System.err.println("problème d'insertion d'un producteur");;
        } finally {
            DB.closeStatement(ps);
        }

    }

    @Override
    public void update(Producer producer) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE producer SET Name = ?,Cin = ?,Adress = ?,PhoneNumber = ?  WHERE Id = ?");
            ps.setString(1, producer.getName());
            ps.setString(2,producer.getCIN());
            ps.setString(3,producer.getAddress());
            ps.setInt(4,producer.getPhoneNumber());
            ps.setInt(5, producer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("problème de mise à jour d'un producteur");;
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM producer WHERE id = ?");

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("problème de suppression d'un producteur");;
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Producer findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM producer WHERE id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                Producer producer = new Producer();

                producer.setId(rs.getInt("Id"));
                producer.setName(rs.getString("Name"));
                producer.setCIN(rs.getString("Cin"));
                producer.setAddress(rs.getString("Adress"));
                producer.setPhoneNumber(rs.getInt("PhoneNumber"));

                return producer;
            }

            return null;
        } catch (SQLException e) {
            System.err.println("problème de requête pour trouver un producteur");;
            return null;
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public List<Producer> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM producer");
            rs = ps.executeQuery();

            List<Producer> listproducer = new ArrayList<>();

            while (rs.next()) {
                Producer producer = new Producer();

                producer.setId(rs.getInt("Id"));
                producer.setName(rs.getString("Name"));
                producer.setCIN(rs.getString("Cin"));
                producer.setAddress(rs.getString("Adress"));
                producer.setPhoneNumber(rs.getInt("PhoneNumber"));

                listproducer.add(producer);
            }

            return listproducer;
        } catch (SQLException e) {
            System.err.println("problème de requête pour sélectionner un producteur");;
            return null;
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }
}

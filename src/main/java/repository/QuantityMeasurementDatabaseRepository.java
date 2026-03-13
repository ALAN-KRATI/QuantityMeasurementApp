package repository;

import model.QuantityMeasurementEntity;
import support.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    @Override
    public void save(QuantityMeasurementEntity entity) {
        String sql = "INSERT INTO measurement(value, unit, operation, result) VALUES (?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entity.getOperation());
            stmt.setString(2, entity.getOperand1());
            stmt.setString(3, entity.getOperand2());
            stmt.setString(4, entity.getResult());

            stmt.executeUpdate();

        } 
        catch(SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}
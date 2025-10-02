package clinic_management_dao;

import clinic_management_ui.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    /**
     * Lấy tất cả đơn thuốc thuộc về một hồ sơ bệnh án.
     */
    public List<Prescription> getPrescriptionsByRecordId(int recordId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE record_id = ?";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prescription p = new Prescription();
                    p.setPrescriptionId(rs.getInt("prescription_id"));
                    p.setRecordId(rs.getInt("record_id"));
                    p.setMedicineName(rs.getString("medicine_name"));
                    p.setDosage(rs.getString("dosage"));
                    p.setQuantity(rs.getInt("quantity"));
                    p.setInstructions(rs.getString("instructions"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm một loại thuốc mới vào đơn thuốc.
     */
    public boolean addPrescription(Prescription p) {
        String sql = "INSERT INTO prescriptions (record_id, medicine_name, dosage, quantity, instructions) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getRecordId());
            ps.setString(2, p.getMedicineName());
            ps.setString(3, p.getDosage());
            ps.setInt(4, p.getQuantity());
            ps.setString(5, p.getInstructions());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa một loại thuốc khỏi đơn thuốc.
     */
    public boolean deletePrescription(int prescriptionId) {
        String sql = "DELETE FROM prescriptions WHERE prescription_id = ?";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
package clinic_management_dao;

import clinic_management_ui.Connect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    public List<Prescription> getPrescriptionsByRecordId(int recordId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE record_id = ?";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, recordId);
            ResultSet rs = pst.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPrescription(Prescription p) {
        String sql = "INSERT INTO prescriptions (record_id, medicine_name, dosage, quantity, instructions) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, p.getRecordId());
            pst.setString(2, p.getMedicineName());
            pst.setString(3, p.getDosage());
            pst.setInt(4, p.getQuantity());
            pst.setString(5, p.getInstructions());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // === THÊM PHƯƠNG THỨC NÀY VÀO ===
    /**
     * Xóa một loại thuốc khỏi đơn thuốc.
     * @param prescriptionId ID của dòng thuốc cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deletePrescription(int prescriptionId) {
        String sql = "DELETE FROM prescriptions WHERE prescription_id = ?";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, prescriptionId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
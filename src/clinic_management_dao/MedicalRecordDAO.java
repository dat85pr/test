package clinic_management_dao;

import clinic_management_ui.Connect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    public List<MedicalRecord> getRecordsByPatientId(int patientId) {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = "SELECT r.*, d.disease_name FROM medical_records r " +
                     "LEFT JOIN diseases d ON r.disease_id = d.disease_id " +
                     "WHERE r.patient_id = ?";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, patientId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                MedicalRecord record = new MedicalRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setAppointmentId(rs.getInt("appointment_id"));
                record.setPatientId(rs.getInt("patient_id"));
                record.setDoctorId(rs.getInt("doctor_id"));
                record.setDiseaseId(rs.getInt("disease_id"));
                record.setDiagnosis(rs.getString("diagnosis"));
                record.setTreatment(rs.getString("treatment"));
                record.setDiseaseName(rs.getString("disease_name"));
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addMedicalRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (appointment_id, patient_id, doctor_id, disease_id, diagnosis, treatment) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, record.getAppointmentId());
            pst.setInt(2, record.getPatientId());
            pst.setInt(3, record.getDoctorId());
            if (record.getDiseaseId() != null && record.getDiseaseId() > 0) {
                pst.setInt(4, record.getDiseaseId());
            } else {
                pst.setNull(4, Types.INTEGER);
            }
            pst.setString(5, record.getDiagnosis());
            pst.setString(6, record.getTreatment());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // === THÊM PHƯƠNG THỨC NÀY VÀO ===
    /**
     * Xóa một hồ sơ bệnh án khỏi cơ sở dữ liệu.
     * @param recordId ID của hồ sơ bệnh án cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteMedicalRecord(int recordId) {
        String sql = "DELETE FROM medical_records WHERE record_id = ?";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, recordId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public List<MedicalRecordDisplay> getAllRecordsWithDetails() {
        List<MedicalRecordDisplay> list = new ArrayList<>();
        String sql = "SELECT mr.record_id, p.patient_name, d.doctor_name, a.appointment_date, dis.disease_name, mr.diagnosis " +
                    "FROM medical_records mr " +
                    "JOIN appointments a ON mr.appointment_id = a.appointment_id " +
                     "JOIN patients p ON mr.patient_id = p.patient_id " +
                     "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                     "LEFT JOIN diseases dis ON mr.disease_id = dis.disease_id " +
                     "ORDER BY a.appointment_date DESC";
        try (Connection conn = Connect.ConnectDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // ... code để điền dữ liệu vào list ...
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MedicalRecordDisplay> searchRecords(String patientName, String doctorName) {
        List<MedicalRecordDisplay> list = new ArrayList<>();
        // ... code tìm kiếm tương tự như hàm search trong AppointmentDAO ...
     return list;
    }
}
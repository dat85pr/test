package clinic_management_dao;

import clinic_management_ui.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    /**
     * Thêm một hồ sơ bệnh án mới vào cơ sở dữ liệu.
     * @param record Đối tượng MedicalRecord chứa thông tin cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addMedicalRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (appointment_id, patient_id, doctor_id, disease_id, diagnosis, treatment) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getAppointmentId());
            ps.setInt(2, record.getPatientId());
            ps.setInt(3, record.getDoctorId());

            if (record.getDiseaseId() == 0) { // Giả sử 0 là không có bệnh
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, record.getDiseaseId());
            }
            
            ps.setString(5, record.getDiagnosis());
            ps.setString(6, record.getTreatment());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa một hồ sơ bệnh án khỏi cơ sở dữ liệu dựa trên ID.
     * @param recordId ID của hồ sơ cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteMedicalRecord(int recordId) {
        String sql = "DELETE FROM medical_records WHERE record_id = ?";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy tất cả hồ sơ bệnh án của một bệnh nhân cụ thể.
     * @param patientId ID của bệnh nhân.
     * @return Danh sách các đối tượng MedicalRecord.
     */
    public List<MedicalRecord> getRecordsByPatientId(int patientId) {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE patient_id = ?";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicalRecord record = new MedicalRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setAppointmentId(rs.getInt("appointment_id"));
                    // ... (Điền các thuộc tính khác)
                    list.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy tất cả hồ sơ bệnh án với thông tin chi tiết (tên bệnh nhân, bác sĩ, bệnh).
     * Dùng cho màn hình quản lý tổng thể.
     * @return Danh sách các đối tượng MedicalRecordDisplay.
     */
    public List<MedicalRecordDisplay> getAllRecordsWithDetails() {
        List<MedicalRecordDisplay> list = new ArrayList<>();
        String sql = "SELECT mr.record_id, p.patient_name, d.doctor_name, a.appointment_date, dis.disease_name, mr.diagnosis " +
                     "FROM medical_records mr " +
                     "JOIN appointments a ON mr.appointment_id = a.appointment_id " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                     "LEFT JOIN diseases dis ON mr.disease_id = dis.disease_id " +
                     "ORDER BY a.appointment_date DESC";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MedicalRecordDisplay record = new MedicalRecordDisplay();
                record.setRecordId(rs.getInt("record_id"));
                record.setPatientName(rs.getString("patient_name"));
                record.setDoctorName(rs.getString("doctor_name"));
                record.setAppointmentDate(rs.getTimestamp("appointment_date"));
                record.setDiseaseName(rs.getString("disease_name"));
                record.setDiagnosis(rs.getString("diagnosis"));
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm hồ sơ bệnh án theo tên bệnh nhân và/hoặc tên bác sĩ.
     * @param patientName Tên bệnh nhân (có thể là một phần).
     * @param doctorName Tên bác sĩ (có thể là một phần).
     * @return Danh sách các đối tượng MedicalRecordDisplay phù hợp.
     */
    public List<MedicalRecordDisplay> searchRecords(String patientName, String doctorName) {
        List<MedicalRecordDisplay> list = new ArrayList<>();
        String sql = "SELECT mr.record_id, p.patient_name, d.doctor_name, a.appointment_date, dis.disease_name, mr.diagnosis " +
                     "FROM medical_records mr " +
                     "JOIN appointments a ON mr.appointment_id = a.appointment_id " +
                     "JOIN patients p ON mr.patient_id = p.patient_id " +
                     "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                     "LEFT JOIN diseases dis ON mr.disease_id = dis.disease_id " +
                     "WHERE p.patient_name LIKE ? AND d.doctor_name LIKE ? " +
                     "ORDER BY a.appointment_date DESC";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + (patientName != null ? patientName : "") + "%");
            ps.setString(2, "%" + (doctorName != null ? doctorName : "") + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicalRecordDisplay record = new MedicalRecordDisplay();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setPatientName(rs.getString("patient_name"));
                    record.setDoctorName(rs.getString("doctor_name"));
                    record.setAppointmentDate(rs.getTimestamp("appointment_date"));
                    record.setDiseaseName(rs.getString("disease_name"));
                    record.setDiagnosis(rs.getString("diagnosis"));
                    list.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
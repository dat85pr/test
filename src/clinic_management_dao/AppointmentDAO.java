package clinic_management_dao;

import java.sql.*;
import java.util.*;
import clinic_management_ui.Connect;

public class AppointmentDAO {

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        // Cập nhật câu JOIN và tên cột
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.reason, a.status, " +
                     "p.full_name AS patientName, d.full_name AS doctorName " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN doctors d ON a.doctor_id = d.doctor_id";
        try (Connection conn = Connect.ConnectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Appointment ap = new Appointment();
                ap.setAppointmentId(rs.getInt("appointment_id"));
                ap.setPatientId(rs.getInt("patient_id"));
                ap.setDoctorId(rs.getInt("doctor_id"));
                ap.setAppointmentDate(rs.getTimestamp("appointment_date"));
                ap.setReason(rs.getString("reason"));
                ap.setStatus(rs.getString("status"));
                ap.setPatientName(rs.getString("patientName"));
                ap.setDoctorName(rs.getString("doctorName"));
                list.add(ap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Các phương thức khác như add, update, delete, getAppointmentsByPatientId cũng cần được cập nhật tương tự
    // Ví dụ cho getAppointmentsByPatientId
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name AS patientName, d.full_name AS doctorName " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "WHERE a.patient_id = ?";
        try (Connection conn = Connect.ConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment ap = new Appointment();
                ap.setAppointmentId(rs.getInt("appointment_id"));
                ap.setPatientId(rs.getInt("patient_id"));
                ap.setDoctorId(rs.getInt("doctor_id"));
                ap.setAppointmentDate(rs.getTimestamp("appointment_date"));
                ap.setReason(rs.getString("reason"));
                ap.setStatus(rs.getString("status"));
                ap.setPatientName(rs.getString("patientName"));
                ap.setDoctorName(rs.getString("doctorName"));
                list.add(ap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
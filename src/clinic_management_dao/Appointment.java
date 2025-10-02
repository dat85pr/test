package clinic_management_dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Appointment {
    // ... các thuộc tính và constructor của bạn giữ nguyên ...

    @Override
    public String toString() {
        // Định dạng lại để hiển thị trong JComboBox cho dễ đọc
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.appointmentDate);
        return "ID: " + appointmentId + " - " + date + " - " + doctorName;
    }
    
    //... getters and setters
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private Timestamp appointmentDate;
    private String reason;
    private String status;
    private String patientName;
    private String doctorName;
    public Appointment() {
        
    }

    public Appointment(int appointmentId, int patientId, int doctorId,
                        Timestamp appointmentDate, String reason, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}
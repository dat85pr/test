package clinic_management_dao;

import java.sql.Timestamp;

public class MedicalRecordDisplay {
    private int recordId;
    private String patientName;
    private String doctorName;
    private String diseaseName;
    private String diagnosis;
    private String treatment;
    private Timestamp createdAt;

    public MedicalRecordDisplay() {}

    // Getters & Setters
    public int getRecordId() {
        return recordId;
    }
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }
    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

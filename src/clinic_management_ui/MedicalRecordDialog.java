package clinic_management_ui;

import clinic_management_dao.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class MedicalRecordDialog extends javax.swing.JDialog {
    // ... (Các khai báo DAO và UI components giữ nguyên) ...
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final DiseaseDAO diseaseDAO = new DiseaseDAO();
    private JTable medicalRecordTable;
    private DefaultTableModel medicalRecordTableModel;
    private JComboBox<Appointment> appointmentComboBox;
    private JComboBox<Disease> diseaseComboBox;
    private JTextArea diagnosisTextArea, treatmentTextArea;
    private JTable prescriptionTable;
    private DefaultTableModel prescriptionTableModel;
    private JTextField medicineNameField, dosageField;
    private JSpinner quantitySpinner;
    private JTextArea instructionsTextArea;
    private JPanel prescriptionPanel;
    private final Patient currentPatient;
    private List<MedicalRecord> medicalRecordList;
    private List<Prescription> currentPrescriptionList;


    public MedicalRecordDialog(Frame owner, Patient patient) {
        super(owner, "Medical Records for: " + patient.getFullName(), true);
        this.currentPatient = patient;
        setSize(1280, 720);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createMedicalRecordPanel(), createPrescriptionPanel());
        splitPane.setResizeWeight(0.55);
        add(splitPane, BorderLayout.CENTER);

        loadInitialData();
    }

    private JPanel createMedicalRecordPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Medical Records"));
        
        // ... (Phần form panel giữ nguyên) ...
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Appointment:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; appointmentComboBox = new JComboBox<>(); formPanel.add(appointmentComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Diagnosed Disease:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; diseaseComboBox = new JComboBox<>(); formPanel.add(diseaseComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.NORTHWEST; formPanel.add(new JLabel("Detailed Diagnosis:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        diagnosisTextArea = new JTextArea(4, 20); formPanel.add(new JScrollPane(diagnosisTextArea), gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST; formPanel.add(new JLabel("Treatment Method:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        treatmentTextArea = new JTextArea(4, 20); formPanel.add(new JScrollPane(treatmentTextArea), gbc);
        
        // === THAY ĐỔI 1: Thêm nút Delete Record ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addRecordBtn = new JButton("Add Record");
        addRecordBtn.addActionListener(e -> addMedicalRecord());
        
        JButton deleteRecordBtn = new JButton("Delete Record"); // Nút mới
        deleteRecordBtn.addActionListener(e -> deleteMedicalRecord()); // Sự kiện mới
        
        buttonPanel.add(addRecordBtn);
        buttonPanel.add(deleteRecordBtn); // Thêm nút vào panel

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(topContainer, BorderLayout.NORTH);

        // ... (Phần table giữ nguyên) ...
        medicalRecordTableModel = new DefaultTableModel(new String[]{"ID", "Appointment ID", "Disease", "Diagnosis"}, 0);
        medicalRecordTable = new JTable(medicalRecordTableModel);
        medicalRecordTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) displaySelectedRecord();
        });
        panel.add(new JScrollPane(medicalRecordTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPrescriptionPanel() {
        prescriptionPanel = new JPanel(new BorderLayout(10, 10));
        prescriptionPanel.setBorder(BorderFactory.createTitledBorder("Prescriptions"));

        // ... (Phần form panel giữ nguyên) ...
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Medicine Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; medicineNameField = new JTextField(20); formPanel.add(medicineNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; dosageField = new JTextField(20); formPanel.add(dosageField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); formPanel.add(quantitySpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST; formPanel.add(new JLabel("Instructions:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        instructionsTextArea = new JTextArea(4, 20); formPanel.add(new JScrollPane(instructionsTextArea), gbc);

        // === THAY ĐỔI 2: Thêm nút Delete Medicine ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addPrescriptionBtn = new JButton("Add Medicine");
        addPrescriptionBtn.addActionListener(e -> addPrescription());
        
        JButton deletePrescriptionBtn = new JButton("Delete Medicine"); // Nút mới
        deletePrescriptionBtn.addActionListener(e -> deletePrescription()); // Sự kiện mới
        
        buttonPanel.add(addPrescriptionBtn);
        buttonPanel.add(deletePrescriptionBtn); // Thêm nút vào panel

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        prescriptionPanel.add(topContainer, BorderLayout.NORTH);

        // ... (Phần table giữ nguyên) ...
        prescriptionTableModel = new DefaultTableModel(new String[]{"ID", "Medicine", "Dosage", "Quantity"}, 0);
        prescriptionTable = new JTable(prescriptionTableModel);
        prescriptionPanel.add(new JScrollPane(prescriptionTable), BorderLayout.CENTER);
        setPanelEnabled(prescriptionPanel, false);
        return prescriptionPanel;
    }

    // ... (Các hàm loadInitialData, addMedicalRecord, displaySelectedRecord giữ nguyên) ...
    private void loadInitialData() { /* ... */ }
    private void addMedicalRecord() { /* ... */ }
    private void displaySelectedRecord() { /* ... */ }
    private void loadMedicalRecords() { /* ... */ }
    private void addPrescription() { /* ... */ }
    private void loadPrescriptionsForSelectedRecord() { /* ... */ }
    private void setPanelEnabled(JPanel panel, boolean isEnabled) { /* ... */ }

    // === THAY ĐỔI 3: Thêm phương thức xử lý sự kiện xóa hồ sơ bệnh án ===
    private void deleteMedicalRecord() {
        int selectedRow = medicalRecordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medical record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this medical record?\nAll associated prescriptions will also be deleted.", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int recordId = medicalRecordList.get(selectedRow).getRecordId();
            if (medicalRecordDAO.deleteMedicalRecord(recordId)) {
                JOptionPane.showMessageDialog(this, "Medical record deleted successfully.");
                loadMedicalRecords(); // Tải lại danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete medical record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // === THAY ĐỔI 4: Thêm phương thức xử lý sự kiện xóa thuốc ===
    private void deletePrescription() {
        int selectedRow = prescriptionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to delete from the prescription.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this medicine from the prescription?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int prescriptionId = currentPrescriptionList.get(selectedRow).getPrescriptionId();
            if (prescriptionDAO.deletePrescription(prescriptionId)) {
                JOptionPane.showMessageDialog(this, "Medicine deleted successfully.");
                loadPrescriptionsForSelectedRecord(); // Tải lại danh sách thuốc
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete medicine.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

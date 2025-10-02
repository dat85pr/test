package clinic_management_ui;

import clinic_management_dao.MedicalRecordDAO;
import clinic_management_dao.MedicalRecordDisplay;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicalRecordForm extends JDialog {
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private JTextField patientNameSearchField, doctorNameSearchField;
    private JButton searchButton, addNewButton, deleteButton;
    private JTable recordTable;
    private DefaultTableModel tableModel;

    public MedicalRecordForm(Frame owner) {
        super(owner, "Medical Record Management", true);
        setSize(1024, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Filters"));
        patientNameSearchField = new JTextField(15);
        doctorNameSearchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Patient Name:"));
        searchPanel.add(patientNameSearchField);
        searchPanel.add(new JLabel("Doctor Name:"));
        searchPanel.add(doctorNameSearchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"Record ID", "Patient Name", "Doctor Name", "Appointment Date", "Disease", "Diagnosis"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        recordTable = new JTable(tableModel);
        add(new JScrollPane(recordTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addNewButton = new JButton("Add New Record...");
        deleteButton = new JButton("Delete Selected Record");
        buttonPanel.add(addNewButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> searchRecords());
        deleteButton.addActionListener(e -> deleteSelectedRecord());
        addNewButton.addActionListener(e -> openAddDialog());

        loadAllRecords();
    }

    private void loadAllRecords() {
        List<MedicalRecordDisplay> list = medicalRecordDAO.getAllRecordsWithDetails();
        updateTable(list);
    }

    private void searchRecords() {
        List<MedicalRecordDisplay> list = medicalRecordDAO.searchRecords(patientNameSearchField.getText(), doctorNameSearchField.getText());
        updateTable(list);
    }
    
    private void updateTable(List<MedicalRecordDisplay> list) {
        tableModel.setRowCount(0);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records found.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (MedicalRecordDisplay record : list) {
                tableModel.addRow(new Object[]{
                    record.getRecordId(), record.getPatientName(), record.getDoctorName(),
                    record.getAppointmentDate(), record.getDiseaseName(), record.getDiagnosis()
                });
            }
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = recordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int recordId = (int) recordTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete record ID: " + recordId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (medicalRecordDAO.deleteMedicalRecord(recordId)) {
                JOptionPane.showMessageDialog(this, "Record deleted.");
                loadAllRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openAddDialog() {
        JOptionPane.showMessageDialog(this, 
            "To add a new record, please go to 'Patient' section, select a patient, and use 'View Medical Records' button.", 
            "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
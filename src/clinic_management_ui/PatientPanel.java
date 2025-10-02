package clinic_management_ui;

import clinic_management_dao.Patient;
import clinic_management_dao.PatientDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class PatientPanel extends JPanel {

    // --- CÁC BIẾN THÀNH VIÊN ---
    private final PatientDAO patientDAO = new PatientDAO();
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField fullNameField, dobField, addressField, phoneNumberField, bloodGroupField, insuranceField, emailField;
    private JComboBox<String> genderComboBox;
    private JButton viewRecordButton;
    private List<Patient> patientList;

    // --- CONSTRUCTOR (HÀM DỰNG) ---
    public PatientPanel() {
        // Thiết lập layout và dựng giao diện ngay trong constructor
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Tải dữ liệu ban đầu
        loadPatientData();
    }

    // --- CÁC PHƯƠNG THỨC ĐỂ DỰNG GIAO DIỆN (PRIVATE) ---
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Patient Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; fullNameField = new JTextField(15); panel.add(fullNameField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; genderComboBox = new JComboBox<>(new String[]{"Male", "Female"}); panel.add(genderComboBox, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Date of Birth (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; dobField = new JTextField(15); panel.add(dobField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; phoneNumberField = new JTextField(15); panel.add(phoneNumberField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; emailField = new JTextField(15); panel.add(emailField, gbc);
        gbc.gridx = 2; gbc.gridy = 2; panel.add(new JLabel("Blood Group:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; bloodGroupField = new JTextField(15); panel.add(bloodGroupField, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Insurance No:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; insuranceField = new JTextField(15); panel.add(insuranceField, gbc);
        gbc.gridx = 2; gbc.gridy = 3; panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; addressField = new JTextField(15); panel.add(addressField, gbc);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "Full Name", "Gender", "Date of Birth", "Phone", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) displaySelectedPatient();
        });
        return new JScrollPane(patientTable);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addPatient());
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updatePatient());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deletePatient());
        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());
        viewRecordButton = new JButton("View Medical Records");
        viewRecordButton.setEnabled(false);
        viewRecordButton.addActionListener(e -> openMedicalRecordDialog());
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(viewRecordButton);
        return panel;
    }

    // --- CÁC PHƯƠNG THỨC XỬ LÝ LOGIC ---
    private void loadPatientData() {
        patientList = patientDAO.getAllPatients();
        tableModel.setRowCount(0);
        for (Patient p : patientList) {
            tableModel.addRow(new Object[]{p.getId(), p.getFullName(), p.getGender(), p.getDateOfBirth(), p.getPhoneNumber(), p.getAddress()});
        }
    }

    private void displaySelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            Patient p = patientList.get(selectedRow);
            fullNameField.setText(p.getFullName());
            dobField.setText(p.getDateOfBirth());
            genderComboBox.setSelectedItem(p.getGender());
            addressField.setText(p.getAddress());
            phoneNumberField.setText(p.getPhoneNumber());
            bloodGroupField.setText(p.getBloodGroup());
            insuranceField.setText(p.getInsuranceNumber());
            emailField.setText(p.getEmail());
            viewRecordButton.setEnabled(true);
        } else {
            viewRecordButton.setEnabled(false);
        }
    }

    private void addPatient() {
        String fullName = fullNameField.getText();
        String gender = genderComboBox.getSelectedItem().toString();
        String dob = dobField.getText();
        String address = addressField.getText();
        String phone = phoneNumberField.getText();
        String blood = bloodGroupField.getText();
        String insurance = insuranceField.getText();
        String email = emailField.getText();

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = patientDAO.insertPatient(fullName, gender, dob, address, phone, blood, insurance, email);
        if (success) {
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            loadPatientData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add patient.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Patient p = new Patient();
        p.setId(patientList.get(selectedRow).getId());
        p.setFullName(fullNameField.getText());
        p.setGender(genderComboBox.getSelectedItem().toString());
        p.setDateOfBirth(dobField.getText());
        p.setAddress(addressField.getText());
        p.setPhoneNumber(phoneNumberField.getText());
        p.setBloodGroup(bloodGroupField.getText());
        p.setInsuranceNumber(insuranceField.getText());
        p.setEmail(emailField.getText());
        if (patientDAO.updatePatient(p)) {
            JOptionPane.showMessageDialog(this, "Update successful!");
            loadPatientData();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int patientId = patientList.get(selectedRow).getId();
            if (patientDAO.deletePatient(patientId)) {
                JOptionPane.showMessageDialog(this, "Delete successful!");
                loadPatientData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        fullNameField.setText("");
        dobField.setText("");
        addressField.setText("");
        phoneNumberField.setText("");
        genderComboBox.setSelectedIndex(0);
        bloodGroupField.setText("");
        insuranceField.setText("");
        emailField.setText("");
        patientTable.clearSelection();
        viewRecordButton.setEnabled(false);
    }
    
    private void openMedicalRecordDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            Patient selectedPatient = patientList.get(selectedRow);
            Window owner = SwingUtilities.getWindowAncestor(this);
            MedicalRecordDialog dialog = new MedicalRecordDialog((Frame) owner, selectedPatient);
            dialog.setVisible(true);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

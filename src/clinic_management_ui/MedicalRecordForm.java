package clinic_management_ui;

import clinic_management_dao.MedicalRecordDAO;
import clinic_management_dao.MedicalRecordDisplay;
import clinic_management_dao.Prescription;
import clinic_management_dao.PrescriptionDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MedicalRecordForm extends JFrame {

    // --- DAO ---
    private final MedicalRecordDAO recordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    // --- UI Components ---
    private JTextField txtPatientName, txtDoctorName;
    private JButton btnSearch, btnDeleteRecord;
    private JTable tblRecords;
    private DefaultTableModel recordTableModel;

    private JPanel prescriptionPanel;
    private JTable tblPrescriptions;
    private DefaultTableModel prescriptionTableModel;
    private JTextField txtMedicineName, txtDosage, txtQuantity;
    private JTextArea txtInstructions;
    private JButton btnSavePrescription, btnCancelPrescription, btnDeletePrescription;
    
    private JPanel navPanel;

    public MedicalRecordForm() {
        super("Clinic Management - Medical Records");
        initComponents();
        loadAllRecords();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
    }

    /**
     * === THAY ĐỔI 1: Cập nhật hàm này để thêm panel nút Save/Cancel ở dưới cùng ===
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 1. Panel điều hướng bên trái (màu hồng)
        createNavigationPanel();
        mainPanel.add(navPanel, BorderLayout.WEST);

        // 2. JSplitPane để chia đôi phần nội dung
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createMedicalRecordPanel(),
                createPrescriptionPanel()
        );
        splitPane.setResizeWeight(0.55);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // 3. Panel chứa các nút Save/Cancel cho toàn bộ cửa sổ (THÊM MỚI)
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSaveWindow = new JButton("Save & Close");
        JButton btnCancelWindow = new JButton("Cancel");
        
        btnSaveWindow.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelWindow.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        bottomButtonPanel.add(btnSaveWindow);
        bottomButtonPanel.add(btnCancelWindow);
        mainPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        // Gắn sự kiện để quay về trang chính
        btnSaveWindow.addActionListener(e -> backToMainPage());
        btnCancelWindow.addActionListener(e -> backToMainPage());

        // Thêm panel chính vào Frame
        this.setContentPane(mainPanel);
    }

    /**
     * === THAY ĐỔI 2: Tạo hàm mới để xử lý việc quay về trang chính ===
     */
    private void backToMainPage() {
        new AppointmentForm().setVisible(true);
        this.dispose(); // Đóng cửa sổ hiện tại
    }

    // ... (Các hàm còn lại giữ nguyên như phiên bản trước)

    private JPanel createMedicalRecordPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Medical Records Management"));
        JPanel searchPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel fieldsPanel = new JPanel(new FlowLayout());
        txtPatientName = new JTextField(15);
        txtPatientName.setBorder(BorderFactory.createTitledBorder("Patient Name"));
        txtDoctorName = new JTextField(15);
        txtDoctorName.setBorder(BorderFactory.createTitledBorder("Doctor Name"));
        fieldsPanel.add(txtPatientName);
        fieldsPanel.add(txtDoctorName);
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        btnSearch = new JButton("Search");
        btnDeleteRecord = new JButton("Delete Selected Record");
        buttonsPanel.add(btnSearch);
        buttonsPanel.add(btnDeleteRecord);
        searchPanel.add(fieldsPanel);
        searchPanel.add(buttonsPanel);
        panel.add(searchPanel, BorderLayout.NORTH);
        recordTableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Doctor", "Date", "Diagnosis"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRecords = new JTable(recordTableModel);
        tblRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tblRecords), BorderLayout.CENTER);
        tblRecords.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                updatePrescriptionView();
            }
        });
        btnSearch.addActionListener(evt -> searchRecords());
        btnDeleteRecord.addActionListener(evt -> deleteSelectedRecord());
        return panel;
    }

    private JPanel createPrescriptionPanel() {
        prescriptionPanel = new JPanel(new BorderLayout(10, 10));
        prescriptionPanel.setBorder(BorderFactory.createTitledBorder("Prescriptions for Selected Record"));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.add(new JLabel("Medicine Name:"));
        txtMedicineName = new JTextField();
        formPanel.add(txtMedicineName);
        formPanel.add(new JLabel("Dosage:"));
        txtDosage = new JTextField();
        formPanel.add(txtDosage);
        formPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        formPanel.add(txtQuantity);
        formPanel.add(new JLabel("Instructions:"));
        txtInstructions = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(txtInstructions));
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSavePrescription = new JButton("Save");
        btnCancelPrescription = new JButton("Cancel");
        btnDeletePrescription = new JButton("Delete Prescription");
        buttonPanel.add(btnSavePrescription);
        buttonPanel.add(btnCancelPrescription);
        buttonPanel.add(btnDeletePrescription);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);
        prescriptionPanel.add(formContainer, BorderLayout.NORTH);
        prescriptionTableModel = new DefaultTableModel(new String[]{"ID", "Medicine", "Dosage", "Qty"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPrescriptions = new JTable(prescriptionTableModel);
        prescriptionPanel.add(new JScrollPane(tblPrescriptions), BorderLayout.CENTER);
        btnSavePrescription.addActionListener(evt -> savePrescription());
        btnCancelPrescription.addActionListener(evt -> clearPrescriptionForm());
        btnDeletePrescription.addActionListener(evt -> deleteSelectedPrescription());
        setPrescriptionPanelEnabled(false);
        return prescriptionPanel;
    }
    
    private void createNavigationPanel() {
        navPanel = new JPanel();
        navPanel.setBackground(new Color(255, 204, 204));
        navPanel.setLayout(new GridLayout(10, 1, 10, 15));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        JLabel title = new JLabel("Clinic Management");
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        navPanel.add(title);
        JButton btnPatient = new JButton("Patient");
        JButton btnDoctor = new JButton("Doctor");
        JButton btnDepartment = new JButton("Department");
        JButton btnAppointment = new JButton("Appointment");
        JButton btnMedicalRecord = new JButton("Medical Record");
        JButton btnBill = new JButton("Bill");
        JButton btnLogin = new JButton("Login");
        btnPatient.addActionListener(e -> { new PatientForm().setVisible(true); this.dispose(); });
        btnAppointment.addActionListener(e -> { new AppointmentForm().setVisible(true); this.dispose(); });
        JButton[] navButtons = {btnPatient, btnDoctor, btnDepartment, btnAppointment, btnMedicalRecord, btnBill, btnLogin};
        for (JButton btn : navButtons) {
            btn.setFocusPainted(false);
            navPanel.add(btn);
        }
        btnMedicalRecord.setBackground(Color.WHITE);
    }
    
    private void savePrescription() {
        int selectedRecordRow = tblRecords.getSelectedRow();
        if (selectedRecordRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medical record first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int recordId = (int) tblRecords.getValueAt(selectedRecordRow, 0);
        String medicineName = txtMedicineName.getText().trim();
        String dosage = txtDosage.getText().trim();
        String instructions = txtInstructions.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        if (medicineName.isEmpty() || dosage.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Medicine Name, Dosage, and Quantity are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Prescription p = new Prescription();
        p.setRecordId(recordId);
        p.setMedicineName(medicineName);
        p.setDosage(dosage);
        p.setQuantity(quantity);
        p.setInstructions(instructions);
        if (prescriptionDAO.addPrescription(p)) {
            JOptionPane.showMessageDialog(this, "Prescription saved successfully.");
            loadPrescriptionsForRecord(recordId);
            clearPrescriptionForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save prescription.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearPrescriptionForm() {
        txtMedicineName.setText("");
        txtDosage.setText("");
        txtQuantity.setText("");
        txtInstructions.setText("");
    }
    
    private void loadAllRecords() {
        List<MedicalRecordDisplay> list = recordDAO.getAllRecordsWithDetails();
        recordTableModel.setRowCount(0);
        if (list != null) {
            for (MedicalRecordDisplay record : list) {
                recordTableModel.addRow(new Object[]{
                    record.getRecordId(), record.getPatientName(), record.getDoctorName(),
                    record.getAppointmentDate(), record.getDiagnosis()
                });
            }
        }
    }

    private void searchRecords() {
        String patientName = txtPatientName.getText().trim();
        String doctorName = txtDoctorName.getText().trim();
        List<MedicalRecordDisplay> list = recordDAO.searchRecords(patientName, doctorName);
        recordTableModel.setRowCount(0);
        if (list != null) {
            for (MedicalRecordDisplay record : list) {
                recordTableModel.addRow(new Object[]{
                    record.getRecordId(), record.getPatientName(), record.getDoctorName(),
                    record.getAppointmentDate(), record.getDiagnosis()
                });
            }
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = tblRecords.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int recordId = (int) tblRecords.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete record ID: " + recordId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (recordDAO.deleteMedicalRecord(recordId)) {
                JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                loadAllRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updatePrescriptionView() {
        int selectedRow = tblRecords.getSelectedRow();
        if (selectedRow != -1) {
            setPrescriptionPanelEnabled(true);
            int recordId = (int) tblRecords.getValueAt(selectedRow, 0);
            loadPrescriptionsForRecord(recordId);
        } else {
            setPrescriptionPanelEnabled(false);
            prescriptionTableModel.setRowCount(0);
        }
    }

    private void loadPrescriptionsForRecord(int recordId) {
        List<Prescription> list = prescriptionDAO.getPrescriptionsByRecordId(recordId);
        prescriptionTableModel.setRowCount(0);
        if (list != null) {
            for (Prescription p : list) {
                prescriptionTableModel.addRow(new Object[]{p.getPrescriptionId(), p.getMedicineName(), p.getDosage(), p.getQuantity()});
            }
        }
    }
    
    private void deleteSelectedPrescription() {
        int selectedPrescriptionRow = tblPrescriptions.getSelectedRow();
        if (selectedPrescriptionRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a prescription to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int prescriptionId = (int) tblPrescriptions.getValueAt(selectedPrescriptionRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this medicine from the prescription?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (prescriptionDAO.deletePrescription(prescriptionId)) {
                JOptionPane.showMessageDialog(this, "Prescription deleted successfully.");
                updatePrescriptionView();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete prescription.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setPrescriptionPanelEnabled(boolean enabled) {
        Component[] components = prescriptionPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                Component[] subComponents = ((JPanel) component).getComponents();
                for (Component subComponent : subComponents) {
                    subComponent.setEnabled(enabled);
                    if (subComponent instanceof JScrollPane) {
                         ((JScrollPane) subComponent).getViewport().getView().setEnabled(enabled);
                    }
                }
            }
            component.setEnabled(enabled);
        }
        tblPrescriptions.setEnabled(enabled);
    }
}
package clinic_management_ui;
        
import clinic_management_dao.Appointment;
import clinic_management_dao.AppointmentDAO;
        
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentPanel extends javax.swing.JPanel {
private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JTextField patientNameField, doctorNameField, roomField;

    public AppointmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(235, 235, 235)); // Màu nền xám nhạt

        // 1. Panel phía trên chứa các form và nút
        add(createTopPanel(), BorderLayout.NORTH);

        // 2. Bảng hiển thị dữ liệu ở giữa
        add(createTablePanel(), BorderLayout.CENTER);

        // 3. Tải dữ liệu từ CSDL
        loadAppointmentData();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        // Panel chứa các ô tìm kiếm
        JPanel searchFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        searchFieldsPanel.setOpaque(false);
        
        searchFieldsPanel.add(new JLabel("Patient Name:"));
        patientNameField = new JTextField(15);
        searchFieldsPanel.add(patientNameField);

        searchFieldsPanel.add(new JLabel("Doctor Name:"));
        doctorNameField = new JTextField(15);
        searchFieldsPanel.add(doctorNameField);
        
        searchFieldsPanel.add(new JLabel("Room:"));
        roomField = new JTextField(10);
        searchFieldsPanel.add(roomField);
        
        JButton searchButton = new JButton("Search");
        styleButton(searchButton);
        searchFieldsPanel.add(searchButton);

        // Panel chứa các nút chức năng
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actionButtonsPanel.setOpaque(false);
        
        JButton addButton = new JButton("Add Appointment");
        styleButton(addButton);
        actionButtonsPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        actionButtonsPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Appointment");
        styleButton(deleteButton);
        actionButtonsPanel.add(deleteButton);

        actionButtonsPanel.add(Box.createHorizontalStrut(40)); // Khoảng cách

        JButton checkRoomButton = new JButton("Check Room");
        styleButton(checkRoomButton);
        actionButtonsPanel.add(checkRoomButton);

        JButton checkScheduleButton = new JButton("Check Schedule");
        styleButton(checkScheduleButton);
        actionButtonsPanel.add(checkScheduleButton);

        topPanel.add(searchFieldsPanel, BorderLayout.NORTH);
        topPanel.add(actionButtonsPanel, BorderLayout.CENTER);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"AppointmentID", "Patient Name", "Doctor Name", "Date", "Reason", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFillsViewportHeight(true);
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        return new JScrollPane(appointmentTable);
    }

    private void loadAppointmentData() {
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);

        // Lấy danh sách lịch hẹn từ DAO
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        
        // Định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Thêm từng lịch hẹn vào bảng
        for (Appointment app : appointments) {
            String formattedDate = sdf.format(app.getAppointmentDate());
            tableModel.addRow(new Object[]{
                app.getAppointmentId(),
                app.getPatientName(),
                app.getDoctorName(),
                formattedDate,
                app.getReason(),
                app.getStatus()
            });
        }
    }
    
    // Hàm tiện ích để trang trí nút cho giống thiết kế
    private void styleButton(JButton button) {
        button.setBackground(new Color(255, 220, 220)); // Màu hồng nhạt cho nút
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.setPreferredSize(new Dimension(button.getText().length() > 10 ? 150 : 100, 30));
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

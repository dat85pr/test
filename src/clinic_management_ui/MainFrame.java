package clinic_management_ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class MainFrame extends javax.swing.JFrame {
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Tên định danh cho các card
    private final String APPOINTMENT_PANEL = "Appointment";
    private final String PATIENT_PANEL = "Patient";
    private final String DOCTOR_PANEL = "Doctor";
    private final String DEPARTMENT_PANEL = "Department";
    private final String MEDICAL_RECORD_PANEL = "Medical Record"; // Tên này có thể không dùng đến trực tiếp
    private final String BILL_PANEL = "Bill";

    public MainFrame() {
        setTitle("Clinic Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createNavigationPanel(), BorderLayout.WEST);

        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);

        addPanelsToCardLayout();
        add(mainContentPanel, BorderLayout.CENTER);
        
        // ===== THAY ĐỔI Ở ĐÂY =====
        // Hiển thị PatientPanel đầu tiên khi chạy ứng dụng
        cardLayout.show(mainContentPanel, PATIENT_PANEL);
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBackground(new Color(255, 230, 230)); // Màu hồng nhạt
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("Clinic Managem...");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Thêm các nút điều hướng
        panel.add(createNavButton("Patient", e -> cardLayout.show(mainContentPanel, PATIENT_PANEL)));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createNavButton("Doctor", e -> cardLayout.show(mainContentPanel, DOCTOR_PANEL)));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createNavButton("Department", e -> cardLayout.show(mainContentPanel, DEPARTMENT_PANEL)));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createNavButton("Appointment", e -> cardLayout.show(mainContentPanel, APPOINTMENT_PANEL)));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        // Nút Medical Record có thể không cần thiết vì ta truy cập qua PatientPanel
        panel.add(createNavButton("Medical Record", e -> JOptionPane.showMessageDialog(this, "Please select a patient and click 'View Medical Records'.")));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createNavButton("Bill", e -> cardLayout.show(mainContentPanel, BILL_PANEL)));
        
        panel.add(Box.createVerticalGlue()); 
        panel.add(createNavButton("Login", e -> { /* Xử lý cho nút Login */ }));

        return panel;
    }

    private JButton createNavButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.addActionListener(listener);
        return button;
    }

    private void addPanelsToCardLayout() {
        // Thêm các Panel vào CardLayout
        mainContentPanel.add(new PatientPanel(), PATIENT_PANEL);
        mainContentPanel.add(new AppointmentPanel(), APPOINTMENT_PANEL);
        mainContentPanel.add(createPlaceholderPanel("Doctor Management"), DOCTOR_PANEL);
        mainContentPanel.add(createPlaceholderPanel("Department Management"), DEPARTMENT_PANEL);
        mainContentPanel.add(createPlaceholderPanel("Bill Management"), BILL_PANEL);
    }
    
    // Panel giữ chỗ cho các chức năng chưa làm
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(235, 235, 235));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        // Thiết lập Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
             try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

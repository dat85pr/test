package clinic_management_ui;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Connect {
    public static Connection ConnectDB() {
        try {
            // Đảm bảo bạn đã thêm thư viện (JAR file) của MySQL Connector
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // Chuỗi kết nối của bạn
            String url = "jdbc:mysql://localhost:3310/clinic_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "root";
            String password = "dat31505"; // ĐIỀN MẬT KHẨU CỦA BẠN VÀO ĐÂY

            Connection con = DriverManager.getConnection(url, user, password);
            return con;
            
        } catch (Exception e) {
            // HIỂN THỊ LỖI RA MÀN HÌNH
            JOptionPane.showMessageDialog(null, 
                "Database Connection Error:\n" + e.getMessage(), 
                "Connection Failed", 
                JOptionPane.ERROR_MESSAGE);
            
            // In chi tiết lỗi ra console để debug
            e.printStackTrace(); 
            
            return null; // Trả về null nếu kết nối thất bại
        }
    }
}
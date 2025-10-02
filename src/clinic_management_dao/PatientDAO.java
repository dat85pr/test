package clinic_management_dao;

import clinic_management_ui.Connect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PatientDAO {

    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY patient_id";
        
        // Sử dụng try-with-resources để đảm bảo kết nối được đóng
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            // Kiểm tra xem kết nối có thành công không
            if (con == null) {
                return list; // Trả về danh sách rỗng nếu kết nối thất bại
            }

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("full_name"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        rs.getString("date_of_birth"), // Đảm bảo cột này tồn tại
                        rs.getString("address"),
                        rs.getString("blood_group"),
                        "N/A", // Bảng patients của bạn không có cột email
                        rs.getString("insurance_number")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            // HIỂN THỊ LỖI SQL RA MÀN HÌNH
            JOptionPane.showMessageDialog(null, 
                "Error fetching patient data:\n" + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return list;
    }

    // ... (các phương thức khác cũng nên được cập nhật tương tự) ...
    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (con == null) return false;
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePatient(Patient p) {
        // Bảng patients của bạn không có cột email, nên tôi đã xóa nó khỏi câu lệnh UPDATE
        String sql = "UPDATE patients SET full_name=?, gender=?, date_of_birth=?, phone_number=?, " +
                     "address=?, blood_group=?, insurance_number=? WHERE patient_id=?";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (con == null) return false;
            pst.setString(1, p.getFullName());
            pst.setString(2, p.getGender());
            pst.setString(3, p.getDateOfBirth());
            pst.setString(4, p.getPhoneNumber());
            pst.setString(5, p.getAddress());
            pst.setString(6, p.getBloodGroup());
            pst.setString(7, p.getInsuranceNumber());
            pst.setInt(8, p.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertPatient(String fullname, String gender, String birthday,
                                String address, String contact, String blood,
                                String insurance, String email) { // email không được sử dụng
        // Bảng patients của bạn không có cột email, nên tôi đã xóa nó khỏi câu lệnh INSERT
        String sql = "INSERT INTO patients (full_name, gender, date_of_birth, address, " +
                     "phone_number, blood_group, insurance_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Connect.ConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (con == null) return false;
            pst.setString(1, fullname);
            pst.setString(2, gender);
            pst.setString(3, birthday);
            pst.setString(4, address);
            pst.setString(5, contact);
            pst.setString(6, blood);
            pst.setString(7, insurance);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
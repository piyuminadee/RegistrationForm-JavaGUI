import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registration_Form extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField tfPsword;
    private JPasswordField tfconfirmPswrd;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;


    public Registration_Form(JFrame parent) {
        super(parent);
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,700));
        setModal(true);
        setLocationRelativeTo(parent);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }




        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {

            String name = tfName.getText();
            String email = tfEmail.getText();
            String address= tfAddress.getText();
            String phone = tfPhone.getText();
            String password = String.valueOf(tfPsword.getPassword());
            String confirmpassword = String.valueOf(tfconfirmPswrd.getPassword());

            if(name.isEmpty() || email.isEmpty() || address.isEmpty()|| phone.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this, "please enter all field", "Try again", JOptionPane.ERROR_MESSAGE);


                return;
            }
            if(!password.equals(confirmpassword)){
                JOptionPane.showMessageDialog(this, "Confirm password does not match", "Try again", JOptionPane.ERROR_MESSAGE);

                return;
            }
           user = addUserToDatabase(name, email,  phone,address, password);  // Allows us add new user to database but not implement
           if (user != null){
               dispose();
           }
           else {
               JOptionPane.showMessageDialog(this,
                       "Failed to register new user",
                       "Try Again",
                       JOptionPane.ERROR_MESSAGE
                       );

           }
    }

    //addUserToDatabase method allow user to implement database
    public User user;
    private User addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost/mystore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO users (name,email,phone,address,password)"+
                    "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

            int addedRows = preparedStatement.executeUpdate();
            if(addedRows>0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }
            stat.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }


    public static void main(String[] args) {
        Registration_Form myForm = new Registration_Form(null);
        User user = myForm.user;
        if(user != null){
            System.out.println("Successful registration of: "+ user.name);
        }
        else {
            System.out.println("Registration Canceled");
        }
      }
}

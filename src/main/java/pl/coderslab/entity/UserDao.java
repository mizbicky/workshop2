package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import workshop2.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";

    private static final String EDIT_QUERY =
            "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";

    private static final String DELETE_QUERY = "DELETE FROM users where id = ?";

    private static final String READ_ALL_USERS_QUERY =
            "SELECT * FROM users";

    private static final String GET_ID =
            "SELECT id FROM users where id = ?";

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

//    public User getId(User user) {
//        try {
//            Connection connection = DbUtil.connect();
//            PreparedStatement preparedStatement = connection.prepareStatement(GET_ID);
//            preparedStatement.setInt(1, user.getId());
//            ResultSet rs = preparedStatement.executeQuery();
//            if (rs.next()) {
//                int i = rs.getInt("id");
//                System.out.println("Your id: " + i);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return user;
//    }


    public User create(User user) {
        try (Connection con = DbUtil.connect()) {
            PreparedStatement ps = con.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashPassword(user.getPassword()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Your ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


        return user;
    }

    public User read(int userId) {
        try {

            Connection con = DbUtil.connect();
            PreparedStatement ps = con.prepareStatement(READ_USER_QUERY);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User update(User user) {
        try (Connection con = DbUtil.connect()) {
            PreparedStatement ps = con.prepareStatement(EDIT_QUERY);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUserName());
            ps.setString(3, hashPassword(user.getPassword()));
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public User delete(int userId) {
        try {
            Connection conn = DbUtil.connect();
            PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User[] findAll() {
        try {
            User[] users = new User[0];
            Connection conn = DbUtil.connect();
            PreparedStatement ps = conn.prepareStatement(READ_ALL_USERS_QUERY);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] usersArray = Arrays.copyOf(users, users.length + 1);
        usersArray[users.length] = u;
        return usersArray;
    }
}





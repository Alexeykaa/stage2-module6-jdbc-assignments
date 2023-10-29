package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {
    private static final String createUserSQL = "";
    private static final String updateUserSQL = "";
    private static final String deleteUser = "";
    private static final String findUserByIdSQL = "";
    private static final String findUserByNameSQL = "";
    private static final String findAllUserSQL = "";

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;


    public Long createUser() {
        Long result = null;
        return result;
    }
    public Long createUser(User user) {
        Long result = null;
        return result;
    }

    public User findUserById(Long userId) {
        User result = new User();
        return result;
    }

    public User findUserByName(String userName) {
        User result = new User();
        return result;
    }

    public List<User> findAllUser() {
        List<User> result = null;
        return result;
    }

    public User updateUser() {
        User result = new User();
        return result;
    }

    public User updateUser(User user) {
        User result = new User();
        return result;
    }

    public void deleteUser(Long userId) {
    }
}

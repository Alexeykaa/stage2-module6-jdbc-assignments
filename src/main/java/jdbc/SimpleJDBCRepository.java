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

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "";
    private static final String updateUserSQL = "";
    private static final String deleteUser = "";
    private static final String findUserByIdSQL = "";
    private static final String findUserByNameSQL = "";
    private static final String findAllUserSQL = "";

    public Long createUser() {
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

    private void deleteUser(Long userId) {
    }
}

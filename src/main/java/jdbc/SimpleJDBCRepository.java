package jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SimpleJDBCRepository {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJDBCRepository.class);

    private static final String CREATE_USER_SQL = "insert into myusers(firstname, lastname, age) values (?, ?, ?)";
    private static final String UPDATE_USER_SQL =
            "update myusers set firstname = ?, lastname = ?, age = ? where id = ?";
    private static final String DELETE_USER_SQL = "delete from myusers where id = ?";
    private static final String FIND_USER_BY_ID_SQL = "select id, firstname, lastname, age from myusers where id = ?";
    private static final String FIND_USER_BY_NAME_SQL =
            "select id, firstname, lastname, age from myusers where firstname like ? or lastname like ?";
    private static final String FIND_ALL_USER_SQL = "select id, firstname, lastname, age from myusers";

    public Long createUser(User user) {
        Long userId = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getLong(1);
                }
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot insert user {}", user, e);
        }
        return userId;
    }

    public User findUserById(Long userId) {
        User result = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_ID_SQL)
        ) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = map(rs);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Cannot find user id {}", userId, e);
        }

        return result;
    }

    public User findUserByName(String userName) {
        User result = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_NAME_SQL)
        ) {
            ps.setString(1, userName);
            ps.setString(2, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = map(rs);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Cannot find user name {}", userName, e);
        }
        return result;
    }

    public List<User> findAllUser() {
        List<User> result = new ArrayList<>();
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             Statement st = connection.createStatement()
        ) {
            ResultSet rs = st.executeQuery(FIND_ALL_USER_SQL);
            while (rs.next()) {
                result.add(map(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Cannot find any users", e);
        }
        return result;
    }

    public User updateUser(User user) {
        User result = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL)
        ) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                result = user;
            }
        } catch (SQLException e) {
            logger.error("Cannot update user {}", user, e);
        }
        return result;
    }

    public void deleteUser(Long userId) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL)
        ) {
            ps.setLong(1, userId);
            int deletedRows = ps.executeUpdate();
            logger.debug("Deleted users count {}", deletedRows);
        } catch (SQLException e) {
            logger.error("Cannot delete user id {}", userId, e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(rs.getLong(1), rs.getString(2), rs.getString(3),
                rs.getInt(4));
    }
}
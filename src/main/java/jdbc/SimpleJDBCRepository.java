package jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
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

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    public Long createUser(User user) {
        Long userId = null;
        ResultSet rs = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Cannot insert user {}", user, e);
        } finally {
            close(connection, ps, rs);
        }
        return userId;
    }

    public User findUserById(Long userId) {
        User result = null;
        ResultSet rs = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(FIND_USER_BY_ID_SQL);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = parse(rs);
            } else {
                logger.debug("user id not found {}", userId);
            }
        } catch (SQLException e) {
            logger.error("Cannot find user id {}", userId, e);
        } finally {
            close(connection, ps, rs);
        }

        return result;
    }

    public User findUserByName(String userName) {
        User result = null;
        ResultSet rs = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(FIND_USER_BY_NAME_SQL);
            ps.setString(1, userName);
            ps.setString(2, userName);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = parse(rs);
            } else {
                logger.debug("user name not found {}", userName);
            }
        } catch (SQLException e) {
            logger.error("Cannot find user name {}", userName, e);
        } finally {
            close(connection, ps, rs);
        }
        return result;
    }

    public List<User> findAllUser() {
        List<User> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(FIND_ALL_USER_SQL);
            while (rs.next()) {
                result.add(parse(rs));
            }
        } catch (SQLException e) {
            logger.error("Cannot find users", e);
        } finally {
            close(connection, st, rs);
        }
        return result;
    }

    public User updateUser(User user) {
        User result = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(UPDATE_USER_SQL);
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
        } finally {
            close(connection, ps);
        }
        return result;
    }

    public void deleteUser(Long userId) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(DELETE_USER_SQL);
            ps.setLong(1, userId);
            int deletedRows = ps.executeUpdate();
            logger.debug("Deleted users count {}", deletedRows);
        } catch (SQLException e) {
            logger.error("Cannot delete user id {}", userId, e);
        } finally {
            close(connection, ps);
        }
    }

    private User parse(ResultSet rs) throws SQLException {
        return new User(rs.getLong(1), rs.getString(2), rs.getString(3),
                rs.getInt(4));
    }

    private void close(Connection c, Statement st) {
        close(c, st, null);
    }

    private void close(Connection c, Statement st, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close ResultSet", e);
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close statement", e);
        }
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close connection", e);
        }
    }
}
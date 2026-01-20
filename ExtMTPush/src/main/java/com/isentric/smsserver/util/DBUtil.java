package com.isentric.smsserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

    private final JdbcTemplate avatarJdbcTemplate;
    private final JdbcTemplate generalJdbcTemplate;

    @Autowired
    public DBUtil(@Qualifier("avatarJdbcTemplate") JdbcTemplate avatarJdbcTemplate,
                  @Qualifier("generalJdbcTemplate") JdbcTemplate generalJdbcTemplate) {
        this.avatarJdbcTemplate = avatarJdbcTemplate;
        this.generalJdbcTemplate = generalJdbcTemplate;
    }

    public JdbcTemplate getAvatarJdbcTemplate() {
        return avatarJdbcTemplate;
    }

    public JdbcTemplate getGeneralJdbcTemplate() {
        return generalJdbcTemplate;
    }

    public Connection getAvatarConnection() throws SQLException {
        DataSource dataSource = avatarJdbcTemplate.getDataSource();
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        throw new SQLException("Unable to get Avatar DataSource connection");
    }

    public Connection getGeneralConnection() throws SQLException {
        DataSource dataSource = generalJdbcTemplate.getDataSource();
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        throw new SQLException("Unable to get General DataSource connection");
    }

    public void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(ps);
        closeConnection(conn);
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage());
            }
        }
    }

    public void closeStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                logger.error("Error closing statement: {}", e.getMessage());
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Error closing result set: {}", e.getMessage());
            }
        }
    }

    public int executeUpdate(String sql, Object... params) {
        return avatarJdbcTemplate.update(sql, params);
    }

    public int executeUpdateOnGeneral(String sql, Object... params) {
        return generalJdbcTemplate.update(sql, params);
    }
}


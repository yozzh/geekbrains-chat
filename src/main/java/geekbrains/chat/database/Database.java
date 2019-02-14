package geekbrains.chat.database;

import geekbrains.chat.public_.tables.Users;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.utils.MD5Hash;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static DSLContext context;

    public static void init() throws ClassNotFoundException, SQLException {
        if (context != null) return;

        Class.forName("org.h2.Driver");
        Connection connection = getConnection();

        context = DSL.using(connection, SQLDialect.H2);
    }

    static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:h2:file:%s", "./target/db");
        return DriverManager.getConnection(url, "sa", "");
    }

    public static UsersRecord getUser(String login, String password) throws PasswordIsInvalid {
        UsersRecord user;
        String passwordHash = null;
        try {
            passwordHash = MD5Hash.getHash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Record result = context.select()
                .from(Users.USERS)
                .where(Users.USERS.NAME.eq(login))
                .fetchOne();
        if (result == null) {
            user = context.newRecord(Users.USERS);
            user.setName(login);
            user.setPassword(passwordHash);
            user.store();
        } else {
            user = result.into(Users.USERS);

            if (!user.getPassword().equals(passwordHash)) {
                throw new PasswordIsInvalid();
            }
        }

        return user;
    }
}

package ru.atisot.galactmoney.base;

import ru.atisot.galactmoney.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

public class DataBase {

    public static double getBalance(String username) {
        String sql = String.format("select %s from %s where %s=?", Config.getBalanceColumn(), Config.getDataBaseTable(), Config.getUsernameColumn());
        double balance = 0.0;

        try (Connection con = DataSource.getConnection()) {

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet resultSet = pst.executeQuery();
            while(resultSet.next()){
                balance = resultSet.getDouble(Config.getBalanceColumn());
            }

            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public static boolean reduceBalance(String username, double amount) {
        String sql = format("update %s set %s=%s-? where %s=?", Config.getDataBaseTable(), Config.getBalanceColumn(), Config.getBalanceColumn(), Config.getUsernameColumn());

        try (Connection con = DataSource.getConnection()) {

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDouble(1, amount);
            pst.setString(2, username);

            pst.executeUpdate();

            pst.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

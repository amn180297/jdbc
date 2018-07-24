package com.cg.paymentwalletsystem.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.cg.paymentwalletsystem.exception.IPaymentWalletException;
import com.cg.paymentwalletsystem.exception.PaymentWalletException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {
	public Connection getConnection() throws ClassNotFoundException, IOException {
		Properties props = new Properties();
		FileInputStream in = new FileInputStream("db.properties");
		props.load(in);
		in.close();
		String driver = props.getProperty("driver");
		if (driver != null) {
			Class.forName(driver);
		}
		String url = props.getProperty("url");
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE7);
			} catch (PaymentWalletException e1) {
				System.out.println(e1.getMessage());
			}
		}
		
		return con;
	}

}

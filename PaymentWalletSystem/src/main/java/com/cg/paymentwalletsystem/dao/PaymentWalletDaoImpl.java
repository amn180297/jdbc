package com.cg.paymentwalletsystem.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.cg.paymentwalletsystem.bean.Customer;
import com.cg.paymentwalletsystem.exception.IPaymentWalletException;
import com.cg.paymentwalletsystem.exception.PaymentWalletException;
import com.cg.paymentwalletsystem.util.DataBaseUtil;

public class PaymentWalletDaoImpl implements IPaymentDao {

	private static Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	static {
		try {
			connection = new DataBaseUtil().getConnection();
		} catch (ClassNotFoundException e) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE7);
			} catch (PaymentWalletException e1) {
				System.out.println(e1.getMessage());
			}
		} catch (IOException e) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE7);
			} catch (PaymentWalletException e1) {
				System.out.println(e1.getMessage());
			}
		}
	}

	public void createAccount(Customer customer) {
		String sql = "insert into customer values ( ?, ?, ?, ?,?)";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, customer.getMobileNumber());
			preparedStatement.setString(2, customer.getName());
			preparedStatement.setString(3, customer.getEmail());
			preparedStatement.setString(4, String.valueOf(customer.getWalletBalance()));
			String trans="Account Created on \t" + LocalDateTime.now()+"\nAmount\tType\t\t\tDate\t\t\t\tRemaining Balance"
					+"\n---------------------------------------------------------------------------------------\n";
			preparedStatement.setString(5, trans);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Customer getCustomerDetails(String mobileNumber) {
		Customer searchedCustomer = null;
		String sql = "select * from parallelproject.customer where mobileNumber = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, mobileNumber);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String mobileno = resultSet.getString("mobilenumber");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String bal = resultSet.getString("balance");
				String transactions = resultSet.getString("transactions");
				searchedCustomer = new Customer();
				BigDecimal balance = new BigDecimal(bal);
				searchedCustomer.setMobileNumber(mobileno);
				searchedCustomer.setEmail(email);
				searchedCustomer.setName(name);
				searchedCustomer.setWalletBalance(balance);
				searchedCustomer.setTransactions(new StringBuilder(transactions));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return searchedCustomer;
	}

	public void depositMoney(Customer customer, BigDecimal depositAmount) {
		String query = "update customer set balance = ?, transactions = ? where mobilenumber = ?";

		try {
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString(1, String.valueOf(customer.getWalletBalance().add(depositAmount)));
			preparedStmt.setString(3, customer.getMobileNumber());
			customer.setWalletBalance(customer.getWalletBalance().add(depositAmount));
			String statement = customer.getTransactions().toString() + "\n" + depositAmount + "\tDeposited\t"
					+ LocalDateTime.now() + "\t" + customer.getWalletBalance();
			customer.setTransactions(new StringBuilder(statement));
			preparedStmt.setString(2, statement);
			preparedStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean withdrawMoney(Customer customer, BigDecimal withAmount) {
		boolean status = false;
		int res = customer.getWalletBalance().subtract(withAmount).compareTo(new BigDecimal("1000"));
		if (res == 1) {
			status = true;
			String query = "update customer set balance = ?, transactions = ? where mobilenumber = ?";
			try {
				PreparedStatement preparedStmt = connection.prepareStatement(query);
				preparedStmt.setString(1, String.valueOf(customer.getWalletBalance().subtract(withAmount)));
				preparedStmt.setString(3, customer.getMobileNumber());
				customer.setWalletBalance(customer.getWalletBalance().subtract(withAmount));
				String statement = customer.getTransactions().toString() + "\n" + withAmount + "\tWithdrawn\t"
						+ LocalDateTime.now() + "\t" + customer.getWalletBalance();
				customer.setTransactions(new StringBuilder(statement));
				preparedStmt.setString(2, statement);
				preparedStmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	public boolean fundTransfer(Customer sendCustomer, Customer recCustomer, BigDecimal depositAmount) {
		boolean status = false;
		int res = sendCustomer.getWalletBalance().subtract(depositAmount).compareTo(new BigDecimal("1000"));
		if (res == 1) {
			status = true;
			String query = "update customer set balance = ?, transactions = ? where mobilenumber = ?";
			try {
				PreparedStatement preparedStmt = connection.prepareStatement(query);
				preparedStmt.setString(1, String.valueOf(sendCustomer.getWalletBalance().subtract(depositAmount)));
				preparedStmt.setString(3, sendCustomer.getMobileNumber());
				sendCustomer.setWalletBalance(sendCustomer.getWalletBalance().subtract(depositAmount));
				String statement = sendCustomer.getTransactions().toString() + "\n" + depositAmount
						+ "\tTransfered To\t" + LocalDateTime.now() + "\t" + sendCustomer.getWalletBalance();
				preparedStmt.setString(2, statement);
				sendCustomer.setTransactions(new StringBuilder(statement));
				preparedStmt.executeUpdate();

				PreparedStatement preparedStmt1 = connection.prepareStatement(query);
				preparedStmt1.setString(1, String.valueOf(recCustomer.getWalletBalance().add(depositAmount)));
				preparedStmt1.setString(3, recCustomer.getMobileNumber());
				recCustomer.setWalletBalance(recCustomer.getWalletBalance().add(depositAmount));
				String statement2 = recCustomer.getTransactions().toString() + "\n" + depositAmount
						+ "\tTransfered from\t" + LocalDateTime.now() + "\t" + recCustomer.getWalletBalance();
				preparedStmt1.setString(2, statement2);
				recCustomer.setTransactions(new StringBuilder(statement2));
				preparedStmt1.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	public StringBuilder printTransaction(String mobileNumber) {
		String sql = "select transactions from parallelproject.customer where mobileNumber = ?";
		String transactions = null;
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, mobileNumber);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				transactions = resultSet.getString("transactions");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new StringBuilder(transactions);
	}

	/*
	 * private static Map<String, Customer> myMap = null; private static Map<String,
	 * StringBuilder> printingList = null; static { myMap = new HashMap<String,
	 * Customer>(); printingList = new HashMap<String, StringBuilder>(); }
	 * 
	 * public Customer getCustomerDetails(String mobileNumber) { return
	 * myMap.get(mobileNumber); }
	 * 
	 * public void createAccount(Customer customer) {
	 * myMap.put(customer.getMobileNumber(), customer); StringBuilder builder = new
	 * StringBuilder("Account Created on \t" + LocalDateTime.now() +
	 * "\n--------------------------------------------------------------------------\n"
	 * + String.format("%-10s  %-20s%-30s %-20s", "Amount ", "Transaction Type",
	 * "Date & Time", "Balance") +
	 * "\n--------------------------------------------------------------------------\n"
	 * ); printingList.put(customer.getMobileNumber(), builder); }
	 * 
	 * public void depositMoney(Customer customer, BigDecimal depositAmount) { if
	 * (myMap.get(customer.getMobileNumber()) != null) {
	 * customer.setWalletBalance(customer.getWalletBalance().add(depositAmount));
	 * StringBuilder builder = new StringBuilder();
	 * builder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9 " +
	 * depositAmount, "Deposited", LocalDateTime.now(), "\u20B9 " +
	 * customer.getWalletBalance())); printingList.put(customer.getMobileNumber(),
	 * printingList.get(customer.getMobileNumber()).append(builder)); } }
	 * 
	 * public boolean withdrawMoney(Customer customer, BigDecimal withdrawAmount) {
	 * boolean status = false; if (myMap.get(customer.getMobileNumber()) != null) {
	 * int res = customer.getWalletBalance().subtract(withdrawAmount).compareTo(new
	 * BigDecimal("1000")); if (res == 1) {
	 * customer.setWalletBalance(customer.getWalletBalance().subtract(withdrawAmount
	 * )); StringBuilder builder = new StringBuilder();
	 * builder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9 " +
	 * withdrawAmount, "Withdrawn", LocalDateTime.now(), "\u20B9 " +
	 * customer.getWalletBalance())); printingList.put(customer.getMobileNumber(),
	 * printingList.get(customer.getMobileNumber()).append(builder)); status = true;
	 * } } return status; }
	 * 
	 * public boolean fundTransfer(Customer sendCustomer, Customer recCustomer,
	 * BigDecimal transferAmount) { boolean status = false; if
	 * (myMap.get(sendCustomer.getMobileNumber()) != null) { if
	 * (myMap.get(recCustomer.getMobileNumber()) != null) { int res =
	 * sendCustomer.getWalletBalance().subtract(transferAmount).compareTo(new
	 * BigDecimal("1000")); if (res == 1) {
	 * sendCustomer.setWalletBalance(sendCustomer.getWalletBalance().subtract(
	 * transferAmount));
	 * recCustomer.setWalletBalance(recCustomer.getWalletBalance().add(
	 * transferAmount)); StringBuilder builder = new StringBuilder();
	 * builder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9  " +
	 * transferAmount, "Transfered To", LocalDateTime.now(), "\u20B9 " +
	 * sendCustomer.getWalletBalance()));
	 * printingList.put(sendCustomer.getMobileNumber(),
	 * printingList.get(sendCustomer.getMobileNumber()).append(builder));
	 * 
	 * // receiver printingList StringBuilder recBuilder = new StringBuilder();
	 * recBuilder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9  " +
	 * transferAmount, "Transfered From", LocalDateTime.now(), "\u20B9 " +
	 * recCustomer.getWalletBalance()));
	 * printingList.put(recCustomer.getMobileNumber(),
	 * printingList.get(recCustomer.getMobileNumber()).append(recBuilder)); status =
	 * true; } } } return status; }
	 * 
	 * public StringBuilder printTransaction(String mobileNumber) { return
	 * printingList.get(mobileNumber); }
	 */

}

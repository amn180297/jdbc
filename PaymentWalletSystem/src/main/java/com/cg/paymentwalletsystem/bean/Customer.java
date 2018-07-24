package com.cg.paymentwalletsystem.bean;

import java.math.BigDecimal;

public class Customer {
	private String mobileNumber;
	private String name;
	private String email;
	private Wallet wallet;
	private StringBuilder transactions;
	public StringBuilder getTransactions() {
		return transactions;
	}

	public void setTransactions(StringBuilder transactions) {
		this.transactions = transactions;
	}

	public Customer() {
		wallet = new Wallet();
	}

	public Customer(String mobileNumber, String name, String email, Wallet wallet) {
		super();
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.email = email;
		this.wallet = wallet;
	}

	public BigDecimal getWalletBalance() {
		return wallet.getBalance();
	}

	public void setWalletBalance(BigDecimal balance) {
		wallet.setBalance(balance);
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

}

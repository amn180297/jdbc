package com.cg.paymentwalletsystem.bean;

import java.math.BigDecimal;

public class Wallet {
	private BigDecimal balance = new BigDecimal("0.00");

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}

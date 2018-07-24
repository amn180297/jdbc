package com.cg.paymentwalletsystem.dao;

import java.math.BigDecimal;

import com.cg.paymentwalletsystem.bean.Customer;

public interface IPaymentDao {

	public void createAccount(Customer customer);

	public Customer getCustomerDetails(String mobileNumber);

	public void depositMoney(Customer customer, BigDecimal depositAmount);

	public boolean withdrawMoney(Customer customer, BigDecimal depositAmount);

	public boolean fundTransfer(Customer sendCustomer, Customer recCustomer, BigDecimal depositAmount);

	public StringBuilder printTransaction(String mobileNumber);

}

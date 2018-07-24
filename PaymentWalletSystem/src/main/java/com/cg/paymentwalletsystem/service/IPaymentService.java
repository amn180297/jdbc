package com.cg.paymentwalletsystem.service;

import java.math.BigDecimal;

import com.cg.paymentwalletsystem.bean.Customer;
import com.cg.paymentwalletsystem.exception.PaymentWalletException;

public interface IPaymentService {

	public boolean validateDetails(String mobileNumber, String name, String email) throws PaymentWalletException;

	public void createAccount(Customer customer);

	public boolean validateMobileNumber(String mobileNumber) throws PaymentWalletException;

	public Customer getCustomerDetails(String mobileNumber);

	public boolean validateAmount(String amount) throws PaymentWalletException;

	public void depositMoney(Customer customer, BigDecimal depositAmount);

	public boolean withdrawMoney(Customer customer, BigDecimal depositAmount);

	public boolean fundTransfer(Customer recCustomer, Customer recCustomer2, BigDecimal depositAmount);

	public StringBuilder printTransaction(String mobileNumber);

}

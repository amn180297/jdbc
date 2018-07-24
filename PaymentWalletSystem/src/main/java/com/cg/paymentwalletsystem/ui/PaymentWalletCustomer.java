package com.cg.paymentwalletsystem.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.cg.paymentwalletsystem.bean.Customer;
import com.cg.paymentwalletsystem.exception.IPaymentWalletException;
import com.cg.paymentwalletsystem.exception.PaymentWalletException;
import com.cg.paymentwalletsystem.service.IPaymentService;
import com.cg.paymentwalletsystem.service.PaymentWalletServiceImpl;

public class PaymentWalletCustomer {

	static IPaymentService service = new PaymentWalletServiceImpl();

	public static void main(String[] args) {
		int choice = 0;
		try {
			do {
				displayMenuMain();
				System.out.println("Enter Your Choice...");
				choice = new Scanner(System.in).nextInt();
				switch (choice) {
				case 1:
					signUp();
					break;
				case 2:
					signIn();
					break;
				case 3:
					exitSystem();
					break;
				default:
					System.out.println("\nPlease Enter Correction Option...\n");
					break;
				}
			} while (choice != 3);
		} catch (

		InputMismatchException e) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE6);
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}
		}
	}

	private static void exitSystem() {

		System.out.println("Thank you for using Application...!!!!");
		System.exit(0);
	}

	private static void signIn() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter Mobile Number: ");
			String mobileNumber = bufferedReader.readLine();
			try {
				boolean validationResult = service.validateMobileNumber(mobileNumber);
				if (validationResult) {
					Customer customer = service.getCustomerDetails(mobileNumber);
					if (customer != null) {
						// once signedIn print Menu
						int choice = 0;
						do {
							loginMenu();
							System.out.println("Enter Your Choice...");
							choice = new Scanner(System.in).nextInt();
							switch (choice) {
							case 1:
								deposit(customer);
								break;
							case 2:
								withdraw(customer);
								break;
							case 3:
								checkBalance(customer);
								break;
							case 4:
								fundTransfer(customer);
								break;
							case 5:
								printTransaction(customer);
								break;
							case 6:
								logout();
								break;
							default:
								System.out.println("\nPlease Enter Correction Option...\n");
								break;
							}

						} while (choice != 6);
					} else {
						System.out.println("\nSorry... No Account found with " + mobileNumber + " Number\n");
					}

				}
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}

		} catch (IOException exception) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE6);
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}

		}
	}

	private static void logout() {
		String args[] = { "a", "b" };
		System.out.println("\nLogged out Successfully\n");
		main(args);

	}

	private static void printTransaction(Customer customer) {
		StringBuilder transactionSlipList = service.printTransaction(customer.getMobileNumber());
		System.out.println("\n" + transactionSlipList);
	}

	private static void fundTransfer(Customer customer) {
		System.out.println("Enter Receiver Mobile Number: ");
		String receiverMobileNumber = new Scanner(System.in).next();
		boolean validationResult = false;
		try {
			validationResult = service.validateMobileNumber(receiverMobileNumber);
			if (validationResult) {
				if (!(customer.getMobileNumber().equals(receiverMobileNumber))) {
					Customer recCustomer = service.getCustomerDetails(receiverMobileNumber);
					if (recCustomer != null) {
						System.out.println("Enter Amount to Transfer: ");
						try {
							String amount = new Scanner(System.in).next();
							boolean tempDepositAmtValidation = false;
							try {
								tempDepositAmtValidation = service.validateAmount(amount);
								BigDecimal depositAmount = new BigDecimal(amount);
								if (tempDepositAmtValidation) {
									boolean status = service.fundTransfer(customer, recCustomer, depositAmount);
									if (status) {
										System.out.println("\nFund Transferred Succesfully\n"
												+ "\nUpdated Balance is:\u20B9 " + customer.getWalletBalance());
									} else {
										System.out.println("\nInsufficient Balance for withdrawl\n");
									}
								}

							} catch (PaymentWalletException paymentWalletException) {
								System.out.println(paymentWalletException.getMessage());
							}

						} catch (NumberFormatException e) {
						}

					} else {
						System.out.println("\nSorry... No Account found with " + receiverMobileNumber + " Number\n");
					}
				} else {
					System.out.println("\nPlease enter different mobile number\n");
				}
			}

		} catch (PaymentWalletException paymentWalletException) {
			System.out.println(paymentWalletException.getMessage());
		}
	}

	private static void checkBalance(Customer customer) {
		System.out.println("\nCurrent Balance \u20B9 " + customer.getWalletBalance());
	}

	private static void withdraw(Customer customer) {
		System.out.println("Enter Amount to Withdraw: ");
		try {
			String amount = new Scanner(System.in).next();
			boolean tempDepositAmtValidation = false;
			try {
				tempDepositAmtValidation = service.validateAmount(amount);
				BigDecimal depositAmount = new BigDecimal(amount);
				if (tempDepositAmtValidation) {
					boolean status = service.withdrawMoney(customer, depositAmount);
					if (status) {
						System.out.println("\nUpdated Balance is: \u20B9" + customer.getWalletBalance() + "\n");
					} else {
						System.out.println("\nInsufficient Balance for withdrawl\n");
					}

				}

			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}

		} catch (NumberFormatException e) {
		}

	}

	private static void deposit(Customer customer) {
		System.out.println("Enter Amount to deposit: ");
		try {
			String amount = new Scanner(System.in).next();
			boolean tempDepositAmtValidation = false;
			try {
				tempDepositAmtValidation = service.validateAmount(amount);
				BigDecimal depositAmount = new BigDecimal(amount);
				if (tempDepositAmtValidation) {
					service.depositMoney(customer, depositAmount);
					System.out.println("\n\u20B9" + depositAmount + " is deposited successfully"
							+ "\nUpdated Balance is:\u20B9 " + customer.getWalletBalance());
				}

			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}

		} catch (NumberFormatException e) {
		}

	}

	private static void loginMenu() {
		System.out.println("\n1. Deposit.");
		System.out.println("2. Withdraw.");
		System.out.println("3. Check Balance.");
		System.out.println("4. Fund Transfer.");
		System.out.println("5. Print Transaction.");
		System.out.println("6. Logout");
		System.out.println("------------------------");
	}

	private static void signUp() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter Mobile Number: ");
			String mobileNumber = bufferedReader.readLine();
			if (service.getCustomerDetails(mobileNumber) == null) {
				System.out.println("Enter Name: ");
				String name = bufferedReader.readLine();
				System.out.println("Enter Email: ");
				String email = bufferedReader.readLine();
				try {
					boolean validationResult = service.validateDetails(mobileNumber, name, email);
					if (validationResult) {
						Customer customer = new Customer();
						customer.setEmail(email);
						customer.setMobileNumber(mobileNumber);
						customer.setName(name);
						service.createAccount(customer);
						System.out.println("\n" + mobileNumber + " is registered successfully...\n");

					}
				} catch (PaymentWalletException paymentWalletException) {
					System.out.println(paymentWalletException.getMessage());
				}

			} else {
				System.out.println("\n" + mobileNumber + " is already registered...\n");
			}
		} catch (IOException e) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE6);
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}
		}
	}

	private static void displayMenuMain() {
		System.out.println("1. Sign Up");
		System.out.println("2. Sign In");
		System.out.println("3. Exit");
		System.out.println("----------------");

	}

}

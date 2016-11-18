package server;

import java.math.BigDecimal;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.MerchantAccount;
import com.braintreegateway.MerchantAccountRequest;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrors;

import messages.MerchantAccountMessage;
import model.BankPayment;
import model.Role;
import model.User;
import model.VenmoPayment;

public class BrainTreeConnector {
	
	private static BraintreeGateway gateway = new BraintreeGateway(
			  Environment.SANDBOX,
			  "vfzj23pxt637ycw3",
			  "fx7sfqvrj8nyqnvw",
			  "2109d1af69e35a11d85ef87bee8084b1"
			);
	
	private static String masterMerchantId = "sparklesparkhere";
	
	public BrainTreeConnector(){
	}
	
	public String getClientToken(String customerId){
		ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
			    .customerId(customerId);
			return gateway.clientToken().generate(clientTokenRequest);
		//String clientToken = gateway.clientToken().generate();
	}
	
	public String getClientTokenNoCustomerId(){
		return gateway.clientToken().generate();
	}
	
	
	public String createClient(User user, Role role){
		CustomerRequest request = new CustomerRequest()
			    .firstName(user.getFirstName())
			    .lastName(user.getLastName())
			    .email(user.getEmail())
			    .phone(role.getProfile().getPhoneNumber());
			Result<Customer> result = gateway.customer().create(request);

			result.isSuccess();

			return result.getTarget().getId();
	}
	
	public void createPaymentMethod(String customerId, String nonce){
		PaymentMethodRequest request = new PaymentMethodRequest()
			    .customerId(customerId)
			    .paymentMethodNonce(nonce)
			    .options()
			        .failOnDuplicatePaymentMethod(true)
			    .done();

		Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
	}
	
	
	
	public String createTransaction(String customerPaymentToken, String merchantId, String amount ){
		TransactionRequest request = new TransactionRequest()
			    .amount(new BigDecimal(amount))
			    .paymentMethodToken(customerPaymentToken)
			    .merchantAccountId(merchantId)
			    .options()
			        .submitForSettlement(true)
			        .done();

			Result<Transaction> result = gateway.transaction().sale(request);
			Transaction transaction = result.getTarget();
			return transaction.getId();
	}
	
	public String createTransactionNoClient(String customerPaymentNonce, String merchantId, String amount ){
		System.out.println("in create transaction");
		TransactionRequest request = new TransactionRequest()
			    .amount(new BigDecimal(amount))
			    .paymentMethodNonce(customerPaymentNonce)
			    //.merchantAccountId(merchantId)
			    .options()
			        .submitForSettlement(true)
			        .done();

			Result<Transaction> result = gateway.transaction().sale(request);
			System.out.println(result.isSuccess());
	
			Transaction transaction = result.getTarget();
			return transaction.getId();
	}
	
	public String createMerchant(MerchantAccountMessage info){
		
		System.out.println("create merchant account");
		Boolean isBank = (info.merchantPayment instanceof BankPayment);
		MerchantAccountRequest request = null;
		if (isBank){
			BankPayment payment = (BankPayment)info.merchantPayment;
			request = new MerchantAccountRequest().
			    individual().
			        firstName(info.user.getFirstName()).
			        lastName(info.user.getLastName()).
			        email(info.user.getEmail()).
			        phone(info.lender.getProfile().getPhoneNumber()).
			        dateOfBirth(info.merchantPayment.getDateOfBirth()).
			        address().
			            streetAddress(info.merchantPayment.getAddress().getFirstLine()).
			            locality(info.merchantPayment.getAddress().getCity()).
			            region(info.merchantPayment.getAddress().getState()).
			            postalCode(info.merchantPayment.getAddress().getZipCode()).
			            done().
			        done().
			    funding().
			       // descriptor("Blue Ladders").
			        destination(MerchantAccount.FundingDestination.BANK).
			        accountNumber(payment.getAccountNumber()).
			        routingNumber(payment.getRoutingNumber()).
			        done().
			    tosAccepted(true).
			    masterMerchantAccountId(masterMerchantId);
		}
		else{
			VenmoPayment payment = (VenmoPayment)info.merchantPayment;
			request = new MerchantAccountRequest().
			    individual().
			        firstName(info.user.getFirstName()).
			        lastName(info.user.getLastName()).
			        email(info.user.getEmail()).
			        phone(info.lender.getProfile().getPhoneNumber()).
			        dateOfBirth(info.merchantPayment.getDateOfBirth()).
			        address().
			            streetAddress(info.merchantPayment.getAddress().getFirstLine()).
			            locality(info.merchantPayment.getAddress().getCity()).
			            region(info.merchantPayment.getAddress().getState()).
			            postalCode(info.merchantPayment.getAddress().getZipCode()).
			            done().
			        done().
			    funding().
			       // descriptor("Blue Ladders").
			        destination(MerchantAccount.FundingDestination.EMAIL).
			        email(payment.getEmail()).
			        done().
			    tosAccepted(true).
			    masterMerchantAccountId(masterMerchantId);
		}
		
		Result<MerchantAccount> result = gateway.merchantAccount().create(request);
		MerchantAccount ma = result.getTarget();
		return ma.getId();
	}
	
	
	public Customer getCustomer(String customerId){
		return gateway.customer().find(customerId);
	}

}

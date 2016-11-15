package server;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;

import model.Role;
import model.User;

public class BrainTreeConnector {
	
	private static BraintreeGateway gateway = new BraintreeGateway(
			  Environment.SANDBOX,
			  "vfzj23pxt637ycw3",
			  "fx7sfqvrj8nyqnvw",
			  "2109d1af69e35a11d85ef87bee8084b1"
			);
	
	public BrainTreeConnector(String customerId){
		ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
			    .customerId(customerId);
			String clientToken = gateway.clientToken().generate(clientTokenRequest);
		//String clientToken = gateway.clientToken().generate();
	}
	
	public String createClient(User user, Role role){
		CustomerRequest request = new CustomerRequest()
			    .firstName(user.getFirstName())
			    .lastName(user.getLastName())
			    .company("Jones Co.")
			    .email(user.getEmail())
			    .fax("419-555-1234")
			    .phone(role.getProfile().getPhoneNumber())
			    .website("http://example.com");
			Result<Customer> result = gateway.customer().create(request);

			result.isSuccess();
			// true

			return result.getTarget().getId();
	}

}

package com.kerellpnz.mvcrest.service;

import java.util.List;
import java.util.logging.Logger;

import com.kerellpnz.mvcrest.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CustomerServiceRestClientImpl implements CustomerService {

	private final RestTemplate restTemplate;
	private final String crmRestUrl;
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	public CustomerServiceRestClientImpl(RestTemplate theRestTemplate,
										@Value("${crm.rest.url}") String theUrl) {
		restTemplate = theRestTemplate;
		crmRestUrl = theUrl;
		logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
	
	@Override
	public List<Customer> getCustomers() {
		logger.info("in getCustomers(): Calling REST API " + crmRestUrl);
		ResponseEntity<List<Customer>> responseEntity = 
											restTemplate.exchange(crmRestUrl, HttpMethod.GET, null,
													new ParameterizedTypeReference<>() {});
		List<Customer> customers = responseEntity.getBody();
		logger.info("in getCustomers(): customers" + customers);
		return customers;
	}

	@Override
	public Customer getCustomer(int theId) {
		logger.info("in getCustomer(): Calling REST API " + crmRestUrl);
		Customer theCustomer = 
				restTemplate.getForObject(crmRestUrl + "/" + theId, 
										  Customer.class);
		logger.info("in saveCustomer(): theCustomer=" + theCustomer);
		return theCustomer;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		logger.info("in saveCustomer(): Calling REST API " + crmRestUrl);
		int employeeId = theCustomer.getId();
		if (employeeId == 0) {
			restTemplate.postForEntity(crmRestUrl, theCustomer, String.class);
		} else {
			restTemplate.put(crmRestUrl, theCustomer);
		}
		logger.info("in saveCustomer(): success");	
	}

	@Override
	public void deleteCustomer(int theId) {
		logger.info("in deleteCustomer(): Calling REST API " + crmRestUrl);
		restTemplate.delete(crmRestUrl + "/" + theId);
		logger.info("in deleteCustomer(): deleted customer theId=" + theId);
	}
}

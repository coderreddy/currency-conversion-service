package com.kris.microservices.currencyconversionservice;

import java.math.BigDecimal;

import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyConversionController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ServiceImpl impl;
	
	@Autowired
	private CurrencyExchangeServiceProxy feignProxy;
	
	@GetMapping("/currencyConverter/from/{from}/to/{to}/quantity/{quantity}")
	public ConversionsService convertCurrency(@PathVariable String from,@PathVariable String to,@PathVariable BigDecimal quantity)
	{
		ConversionsService service = impl.getCurrencyExchangeValues(from, to);
		ConversionsService response;
		BigDecimal conversionMultiple = null;
		if(service != null)
		{
			conversionMultiple = service.getConversionMultiple();
		}
		response = new ConversionsService(from,to,conversionMultiple,quantity,quantity.multiply(conversionMultiple),service.getPort());
		return response;
	}
	
	@GetMapping("/currencyConverter-feign/from/{from}/to/{to}/quantity/{quantity}")
	@Produces("application/xml")
	public ConversionsService convertCurrencyFeign(@PathVariable String from,@PathVariable String to,@PathVariable BigDecimal quantity)
	{
		ConversionsService response = null;
		ConversionsService serviceResponse = feignProxy.currencyConverter(from, to);
		if(serviceResponse != null)
		{
			 response = new ConversionsService(from,to,serviceResponse.getConversionMultiple(),quantity,quantity.multiply(serviceResponse.getConversionMultiple()),serviceResponse.getPort());
		}
		logger.info("Service response {}",response);
		return response;
	}

}

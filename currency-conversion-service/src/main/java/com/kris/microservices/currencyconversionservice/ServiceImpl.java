package com.kris.microservices.currencyconversionservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceImpl {

	public ConversionsService getCurrencyExchangeValues(String from,String to)
	{
		RestTemplate template = new RestTemplate();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("from", from);
		params.put("to", to);
		ConversionsService service = template.
				getForObject("http://localhost:8000/currency-exchange/from/{from}/to/{to}", ConversionsService.class,params);
		return service;
		
	}
}

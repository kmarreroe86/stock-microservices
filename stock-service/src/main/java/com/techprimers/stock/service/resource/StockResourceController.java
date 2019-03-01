package com.techprimers.stock.service.resource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/rest/stock")
public class StockResourceController {

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/{username}")
	public List<QuoteModel> getStock(@PathVariable final String username) {

		/*Hard Coded
		 * ResponseEntity<List<String>> quoteResponse = restTemplate.exchange("http://localhost:8300/rest/db/" + username,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		});*/
		
		ResponseEntity<List<String>> quoteResponse = restTemplate.exchange("http://db-service/rest/db/" + username,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
				});

		List<String> quotes = quoteResponse.getBody();
		return quotes.stream().map(quote -> {
			BigDecimal stockPrice = getStockPrice(quote);
			return new QuoteModel(quote, stockPrice);

		}).collect(Collectors.toList());
	}

	private BigDecimal getStockPrice(String quote) {
		try {
			return YahooFinance.get(quote, false).getQuote().getPrice();
		} catch (IOException e) {
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}

	private class QuoteModel {
		private String quote;
		private BigDecimal price;

		public QuoteModel(String quote, BigDecimal price) {
			this.quote = quote;
			this.price = price;
		}

		public String getQuote() {
			return quote;
		}

		public void setQuote(String quote) {
			this.quote = quote;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}
	}
}

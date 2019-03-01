package com.example.rest.service.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.service.model.Quote;
import com.example.rest.service.model.Quotes;
import com.example.rest.service.repository.QuotesRepository;

@RestController
@RequestMapping("/rest/db")
public class DbServiceResourceController {

	@Autowired
	private QuotesRepository quotesRepository;

	@GetMapping("/{username}")
	public List<String> getQoutes(@PathVariable("username") final String username) {

		return getQuotesByUserName(username);
	}

	@PostMapping("/add")
	public List<String> add(@RequestBody final Quotes quotes) {

		quotes.getQuotes()
			.stream()
			.map(quote -> new Quote(quotes.getUserName(), quote))
			.forEach(quote -> {
				quotesRepository.save(quote);
			});

		return getQuotesByUserName(quotes.getUserName());
	}
	
	@DeleteMapping("/delete/{username}")	
	public List<String> delete(@PathVariable final String username) {
		
		List<Quote> quotes = quotesRepository.findByUserName(username);
		quotesRepository.deleteInBatch(quotes);
		
		return getQuotesByUserName(username);
	}
	

	private List<String> getQuotesByUserName(final String userName) {
		return quotesRepository.findByUserName(userName).stream().map(quote -> {
			return quote.getQuote();
		}).collect(Collectors.toList());
	}
}

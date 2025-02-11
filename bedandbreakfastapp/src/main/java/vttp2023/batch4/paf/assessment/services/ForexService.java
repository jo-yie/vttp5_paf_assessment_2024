package vttp2023.batch4.paf.assessment.services;

import java.util.LinkedHashMap;

import org.bson.Document;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForexService {

	// TODO hide url in app properties 
	String url = "https://api.frankfurter.app/latest";

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {

		// ?base=AUD&symbols=SGD

		String urlUpdated = url + "?base=" + from + "&symbols=" + to;

		RestTemplate restTemplate = new RestTemplate(); 

		RequestEntity<String> request = RequestEntity.post(urlUpdated)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body("");

		ResponseEntity<Document> response; 

		try { 
			response = restTemplate.exchange(request, Document.class);

			Document doc = response.getBody();

			LinkedHashMap<String, Double> rates = (LinkedHashMap<String, Double>) doc.get("rates");

			double exchangeRate = rates.get(to.toUpperCase());
			float floatExchangeRate = (float) exchangeRate; 

			float convertedAmt = amount * floatExchangeRate;

			return convertedAmt;

		} catch (Exception e) {

			System.out.println("Error" + e.getMessage());

		}

		return -1000f;
	}
}

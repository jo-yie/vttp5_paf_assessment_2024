package vttp2023.batch4.paf.assessment.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static vttp2023.batch4.paf.assessment.repositories.Constants.*;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	// TASK 3
	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 * 
		db.listings.aggregate(
			{
				$match : { 'address.suburb' : { $ne : null } }
			},
			{
				$group : {
					_id : '$address.suburb'
				}
			}
		)
	 *
	 */
	public List<String> getSuburbs(String country) {

		Criteria criteria = Criteria.where("address.suburb")
			.ne(null).ne("");

		MatchOperation matchOperation = Aggregation.match(criteria);
		GroupOperation groupOperation = Aggregation.group("address.suburb");

		Aggregation pipeline = Aggregation.newAggregation(matchOperation, groupOperation);

		List<Document> documents = template.aggregate(pipeline, "listings", Document.class).getMappedResults();

		List<String> suburbs = new ArrayList<>(); 
		for (Document d : documents) {
			String suburb = d.getString("_id");
			suburbs.add(suburb);

		}

		return suburbs;
	}

	// TASK 4
	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *
		db.listings.aggregate([
			{
				$match : { 
					'address.suburb' : { $regex: 'Lilyfield/Rozelle', $options : 'i' },
					price : { $lte : 500 },
					accommodates : { $gte : 1 }, 
					min_nights : { $lte : 2 }
				}
			}, 
			{
				$sort : { price : -1 }
			},
			{
				$project : { name : 1, accommodates : 1, price : 1 }
			}
		])
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {

		Criteria criteria = new Criteria().andOperator(
			Criteria.where(F_SUBURB).regex(suburb, "i"),
			Criteria.where(F_PRICE).lte(priceRange),
			Criteria.where(F_ACCOMMODATES).gte(persons),
			Criteria.where(F_MIN_NIGHTS).lte(duration)
		); 

		MatchOperation matchOperation = Aggregation.match(criteria);

		SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, F_PRICE);

		ProjectionOperation projectionOperation = Aggregation.project(F_NAME, F_ACCOMMODATES, F_PRICE);

		Aggregation pipeline = Aggregation.newAggregation(matchOperation, sortOperation, projectionOperation);

		List<Document> documents = template.aggregate(pipeline, C_LISTINGS, Document.class).getMappedResults();

		List<AccommodationSummary> accommodationSummaries = new ArrayList<>();

		for (Document d : documents) {
			AccommodationSummary a = docToAccommodationSummary(d);
			accommodationSummaries.add(a);

		}

		return accommodationSummaries;
	}

	// TASK 4 helper method 
	// Document --> AccomodationSummary
	public AccommodationSummary docToAccommodationSummary(Document doc) {

		AccommodationSummary a = new AccommodationSummary(); 
		
		a.setId(doc.getString("_id"));
		a.setName(doc.getString("name"));
		a.setAccomodates(doc.getInteger("accommodates"));
		a.setPrice(doc.get("price", Number.class).floatValue());

		return a;

	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}

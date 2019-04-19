//Simple Aggrgation Demo. This could be achived also by the find commmand.
String country = "Portugal";

//Create the filter
Bson countryPT = Filters.eq ("countries", country);

//Create the pipeline
List<Bson> pipeline = new ArrayList<>();

//com.mongodb.client.model.Aggregates
//stage build
Bson matchStage = Aggregates.match(countryPT);

//add the matchStage to the pipeline
pipeline.add(matchStage);

AggregateIterable<Document> iterable = moviesCollection.aggregate(pipeline);

//As a result of the aggegate() method, we get back an AggegateIterable. Similar //to other iterables, this object allows us to iterate over the result set.
List<Document> builderMatchStageResults = new rrayList<>();
iterable.into(builderMatchStageResults);

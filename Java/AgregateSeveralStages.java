//Agregate Several Stages

//Create the pipeline
List<Bson> pipeline = new ArrayList<>();

/* 
 For all movies produced inPortugal, sum the number of times
a prticular cast member gets to visit such a lovely place.
How many times has an individual cast member, participated in a movie
produced in this country. Retun the result in sorted Ascending regarding the number of gigs. In mongo shell the answer is
db.movies.aggregate ([
 {$match: {countries: "Portugal"}},
 {$unwind: "$cast"},
 {$group: {_id:"cast", count:{$sum:1}}}
])
*/

//match to find movies produced in portugal

String country = "Portugal";
Bson countryPT = Filters.eq ("countries", country);
Bson matchStage = Aggregates.match (countryPT);


//$unwind the elements of the $cast array
Bson unwindCastStage = Aggregates.unwind("$cast");

//$group based on cast name and count the number of times a cast
//member appears in the result set

String groupIdCast = "$cast";

/* Group operations are in place to do some sort of accumulation operation.
Operations like $sum, $avg, $min, $max .. are good candidates to be used along side group operations, and there is a java builder for that.
@see com.mongodb.client.model.Accumulators handles all accumulation operations.
*/

//use $sum accumulator to sum 1 for each cast member appearance.
BsonField sum1 = Accumulators.sum("count", 1);

//adding both group _id and accumulators
Bson groupStage = Aggregates.group (groupIdCast, sum1);

//$sort based on the new computed field "count"

//create the sort order using Sorts builder
Bson sortOrder = Sorts.descending("count");

//pass the sort order to the sort stage builder
Bson sortStage = Aggregates.sort (sortOrder);

/*
 With all these stages, we are now ready to call our aggregate method with a bit more complex of pipeline than a single $match stage
*/

pipeline.add(matchStage);
pipeline.add(unwindCastStage);
pipeline.add(groupStage);
pipeline.add(sortStage);

AgregateIterable<Document> iterable =
  mviesCollection.aggregate(pipeline);

List<Document> groupByResults = new ArrayList<>();
for (Document doc: iterable){
 System.out.println(doc);
 groupByResults.add(doc);
}
/*
    The aggregation framework also provides stages that combine
    operations that are typically expressed by several stages.
    For example, $sortByCount, combines both the $group with a $sum
    accumulator with a $sort stage.    
*/

    List<Bson> shorterPipeline = new ArrayList<>();

    // we already have built booth $match and $unwind stages
    shorterPipeline.add(matchStage);
    shorterPipeline.add(unwindCastStage);

    // create the $sortByCountStage
    Bson sortByCount = Aggregates.sortByCount("$cast");

    // append $sortByCount stage to shortPipeline
    shorterPipeline.add(sortByCount);

    // list to collect shorterPipeline results
    List<Document> sortByCountResults = new ArrayList<>();

    for (Document doc : moviesCollection.aggregate(shorterPipeline)) {
      System.out.println(doc);
      sortByCountResults.add(doc);
    }








@Test
  public void complexStages() {

    /*
    Not all aggregation stages are made equal. There are ones that are
    more complex than others, in terms of type of operation and
    parameters they may take to operate.
    Ex: a $lookup stage is takes a fair more amount of parameters/options
     to execute than a $addFields stage
     {
        $lookup: {
            from: "collection_name",
            pipeline: [{}] - sub-pipeline
            let: {...} - expression
            as: "field_name" - output array field name
        }
     }

     vs

     {
        $addFields: {
            "new_field": {expression} - expression that computes field value
           }
     }

     */

    List<Bson> pipeline = new ArrayList<>();

    /*
    To exemplify this scenario, let's go ahead and do the following:
    - create facets of the movie documents that where produced in Portugal
    - facet on cast_members: list of cast members that are found in the
    movies produced in Portugal
    - facet on genres_count: list of genres and it's count
    - facet on year_bucket: matching movies year bucket

    For each facet we are going to create a com.mongodb.client.Facet object.
     */

    // $unwind the cast array
    Bson unwindCast = Aggregates.unwind("$cast");

    // create a set of cast members with $group
    Bson groupCastSet = Aggregates.group("", Accumulators.addToSet("cast_list", "$cast"));

    /*
    Facet constructor takes a facet name and variable arguments,
    variable-length argument, of sub-pipeline stages that build up the
    expected facet values.
    For the cast_filter we need to unwind the cast arrays and use group
    to create a set of cast members.
     */

    Facet castMembersFacet = new Facet("cast_members", unwindCast, groupCastSet);

    // unwind genres
    Bson unwindGenres = Aggregates.unwind("$genres");

    // genres facet bucket
    Bson genresSortByCount = Aggregates.sortByCount("$genres");

    // create a genres count facet
    Facet genresCountFacet = new Facet("genres_count", unwindGenres, genresSortByCount);

    // year bucketAuto
    Bson yearBucketStage = Aggregates.bucketAuto("$year", 10);

    // year bucket facet
    Facet yearBucketFacet = new Facet("year_bucket", yearBucketStage);

    /*
    The Aggregates.facet() method also takes variable set of Facet
    objects that composes the sub-pipelines of each facet element.

    db.movies.aggregate([
        { "$match" : { "countries" : "Portugal" } },
        { "$facet" : {
            "cast_members" : [{ "$unwind" : "$cast" }, { "$group" : { "_id" : "", "cast_list" : { "$addToSet" : "$cast" } } }],
            "genres_count" : [{ "$unwind" : "$genres" }, { "$sortByCount" : "$genres" }],
            "year_bucket" : [{ "$bucketAuto" : { "groupBy" : "$year", "buckets" : 10 } }]
            }
        }
      ])
     */

    // $facets stage
    Bson facetsStage = Aggregates.facet(castMembersFacet, genresCountFacet, yearBucketFacet);

    // match stage
    Bson matchStage = Aggregates.match(Filters.eq("countries", "Portugal"));

    // putting it all together
    pipeline.add(matchStage);
    pipeline.add(facetsStage);

    int countDocs = 0;
    for (Document doc : moviesCollection.aggregate(pipeline)) {
      System.out.println(doc);
      countDocs++;
    }

    Assert.assertEquals(1, countDocs);
  }

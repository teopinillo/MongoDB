 /**
   * Instead of cursor methods, we can use the aggregation stages to accomplish the same result. Why
   * do we need an aggregation stage to do this? Well, the need for intermediary sort, limit and
   * skip stages, exist in multiple pipeline executions, so these stages are readily available for
   * usage within the aggregation pipelines.
   */
  @Test
  public void testFindLimitAndAggLimitStage() {

    // Let's start with limit.
    // Using our movies dataset, we can run the following query:
    // db.movies.find({directors: "Sam Raimi"}).limit(2)
    Bson qFilter = Filters.eq("directors", "Sam Raimi");
    Iterable limitCursor = moviesCollection.find(qFilter).limit(2);

    List<Document> limitedFindList = new ArrayList<>();
    ((FindIterable) limitCursor).into(limitedFindList);
    // the size of this list should be of 2
    Assert.assertEquals(2, limitedFindList.size());

    // Now let's go ahead and do the same using $limit stage
    List<Bson> pipeline = new ArrayList<>();

    // first we need to $match the wanted director
    pipeline.add(Aggregates.match(qFilter));

    // then we limit the results to 2 using $limit
    pipeline.add(Aggregates.limit(2));

    // and then call the aggregation method
    AggregateIterable<Document> aggLimitCursor = moviesCollection.aggregate(pipeline);

    // the results of this cursor match the previously returned documents from limit().
    for (Document d : aggLimitCursor) {
      Assert.assertNotEquals(-1, limitedFindList.indexOf(d));
    }
  }

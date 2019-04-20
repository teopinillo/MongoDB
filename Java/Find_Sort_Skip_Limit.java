    /*
     * Getting all the cursor methods together is done by appending each of
     * those methods to the resulting find iterable
     */
    Bson sortBy_i_Descending = Sorts.descending("i");
    Iterable<Document> cursor = sortable.find()
                                        .sort(sortBy_i_Descending)
                                        .skip(100)
                                        .limit(10);
  
 //--------------------------------------------------------------------------------------------------
    public List<Document> getMoviesByGenre(String sortKey, int limit, int skip, String... genres) {
    // query filter
    Bson castFilter = Filters.in("genres", genres);
    // sort key
    Bson sort = Sorts.descending(sortKey);
    List<Document> movies = new ArrayList<>();    
    moviesCollection.find(castFilter)
                    .sort(sort)
                    .skip(skip)
                    .limit(limit).iterator()
    .forEachRemaining(movies::add);
    return movies;
  }
//--------------------------------------------------------------------------------------------------
 /**
   * This method will execute the following mongo shell query: db.movies.find({"$text": { "$search":
   * `keywords` }}, {"score": {"$meta": "textScore"}}).sort({"score": {"$meta": "textScore"}})
   *
   * @param limit - integer value of number of documents to be limited to.
   * @param skip - number of documents to be skipped.
   * @param keywords - text matching keywords or terms
   * @return List of query matching Document objects
   */
  public List<Document> getMoviesByText(int limit, int skip, String keywords) {
    Bson textFilter = Filters.text(keywords);
    Bson projection = Projections.metaTextScore("score");
    Bson sort = Sorts.metaTextScore("score");
    List<Document> movies = new ArrayList<>();
    moviesCollection
        .find(textFilter)
        .projection(projection)
        .sort(sort)
        .skip(skip)
        .limit(limit)
        .iterator()
        .forEachRemaining(movies::add);
    return movies;
  }

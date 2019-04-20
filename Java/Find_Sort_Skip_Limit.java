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

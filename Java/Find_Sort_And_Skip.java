@Test
  public void testFindSortAndSkip() {
    /*
     * In addition to sorting the query output, we might be interested in sections of the
     * result set, instead of retrieving the whole set at all times.
     * Skip is another cursor method that we can call from the Iterable
     * object.
     */

    // Out of our 1000 documents, we might want to skip the first 990.
    // For that, we just need to append the number of documents we want to
    // skip.
    Iterable<Document> skippedIterable = sortable.find().skip(990);

    List<Document> skippedArray = new ArrayList<>();
    skippedIterable.iterator().forEachRemaining(skippedArray::add);

    /*
     * In this specific case, we are not specifying a particular sort
     * order to our documents. This means that the results will be returned in
     * their $natural sort order, which implies that the documents will be
     * returned based on how they got stored/created in the database server.
     * Generally this will reflect the insert order of documents.
     */


    // This means that using skip on it's own, without determining a
    // particular sort order, may result in different documents, given that
    // they are bound to the insertion order of documents, not the values
    // of the fields they represent. But there is nothing stopping us from
    // sorting them and skipping at the same time!
    Bson sortBy_i_Descending = Sorts.descending("i");
    Iterable<Document> sortedAndSkipped = sortable.find().sort(sortBy_i_Descending).skip(990);

    // The order by which we define the sort() and skip() instructions is
    // irrelevant.
    Iterable<Document> skippedAndSorted = sortable.find().skip(990).sort(sortBy_i_Descending);

    // let's build a sortedFirst list, iterating over all the results of
    // sortedAndSkipped iterable
    List<Document> sortedFirst = new ArrayList<>();
    sortedAndSkipped.iterator().forEachRemaining(sortedFirst::add);

    // and a similar one for skippedFirst
    List<Document> skippedFirst = new ArrayList<>();
    skippedAndSorted.iterator().forEachRemaining(skippedFirst::add);

     /*
     * Note: An important thing to understand about `skip` is that the
     * database will still have to iterate over all the documents in the
     * collection. Only returns after the skip number of documents has been
     * reached, however it will still to traverse the collection / index to
     * return the matching documents.
     */

  }


    /*
     * Another cursor method is limit(). The ability to define how many
     * documents we want to retrieve from the collection, on a given query.
     */

    // To impose a limit to the number of results returned by a query we
    // can append the 'limit' method to the returned iterable of a find
    // instruction.
    Iterable<Document> limited = sortable.find().limit(10);
    List<Document> limitedList = new ArrayList<>();
    limited.forEach(limitedList::add);

      /*
     * One interesting aspect of limit() is that we can use that to
     * influence the cursor batchSize. 
      @see <a href="https://docs.mongodb.com/manual/reference/method/cursor.batchSize/">batchSize</a>
     * The cursor batch size determines the number of documents to be
     * returned in one cursor batch. If our query hits 1M elements you may
     * not want to wait till all of those elements are returned to start
     * processing the result set. Therefore each time we open a cursor or
     * iterable, the results are sent back to the application in smaller
     * batches, hence the cursor batchSize.
     */

    // By default the Java driver will set a batchSize of 0. Which means the
    // driver will use the server defined batchSize, which by default is 101
    // documents. However, you can define your own batchSize for a find
    // operation.

    sortable.find().batchSize(10);

    // In this particular case, given that we are interested in a way
    // smaller number of documents, we have all the interest to limit the
    // batch size to the same value as the limit() cursor method value
    Iterable<Document> limitedBatched = sortable.find().limit(10).batchSize(10);

    /*
     * This would be similar to running this mongo shell command:
     * <p>
     *     db.sortable.find().limit(10).batchSize(10)
     * </p>
     */
  }

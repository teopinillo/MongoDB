 @Test
  public void testFindSortMethod() {
    /*
     * More often than not, the results of our queries are required to be
     * sorted. Databases do a good job sorting those for us, so that we
     * do not have to implement extra logic to sort the results after they've
     * been sent back to the application.
     * In MongoDB, we can do this by appending the sort() method to the
     * FindIterable.
     */

    // In this case we want to sort all my documents by field `i` in descending
    // order. We can start by defining my sort Bson, using the Sorts builder
    Bson sortBy_i_Descending = Sorts.descending("i");

    // Then I'll do a full table scan, using the find() command, and
    // appending the sort method, passing the Bson object that determines
    // my sort order.
    Iterable<Document> sorted = sortable.find().sort(sortBy_i_Descending);

    // Iterating over the result set, we will find the expected set of
    // documents
    List<Document> sortedArray = new ArrayList<>();
    sorted.iterator().forEachRemaining(sortedArray::add);

  }

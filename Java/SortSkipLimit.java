    /*
     * Getting all the cursor methods together is done by appending each of
     * those methods to the resulting find iterable
     */
    Bson sortBy_i_Descending = Sorts.descending("i");
    Iterable<Document> cursor = sortable.find()
                                        .sort(sortBy_i_Descending)
                                        .skip(100)
                                        .limit(10);
  

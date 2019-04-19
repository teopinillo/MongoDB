 @Test
  public void testSortSkipLimit() {
    /*
     * Getting all the cursor methods together is done by appending each of
     * those methods to the resulting find iterable
     */
    Bson sortBy_i_Descending = Sorts.descending("i");
    Iterable<Document> cursor = sortable.find().sort(sortBy_i_Descending).skip(100).limit(10);
    int iValue = 899;

    for (Document d : cursor) {
      Assert.assertEquals(Integer.valueOf(iValue), d.getInteger("i"));
      iValue--;
    }
  }

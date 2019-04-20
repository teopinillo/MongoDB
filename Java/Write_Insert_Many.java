 @Test
  public void testInsertMany() {
    /*
     * Another option to insert new data into a collection, is to insert it
     * in bulk, or better saying several documents at once.
     */

    // In this case I'm going to set up a list of two documents

    List<Document> someGames = new ArrayList<>();

    Document doc1 = new Document("title", "Hitman 2");
    doc1.put("year", 2018);
    doc1.put("label", "Square Enix");

    Document doc2 = new Document();
    HashMap<String, Object> documentValues = new HashMap<>();
    documentValues.put("title", "Tom Raider");
    documentValues.put("label", "Eidos");
    documentValues.put("year", 2013);
    doc2.putAll(documentValues);

    // Once we have the two documents we can add them to the list of
    // documents
    someGames.add(doc1);
    someGames.add(doc2);

    // and finally insert all of these documents using insertMany
    videoGames.insertMany(someGames);

    /*
     * The same logic applies as in insertOne, if an error occurs that
     * prevents the documents to be inserted, we would need to capture a
     * Runtime exception.
     */

    // If we look back into the object references we can see that the _id
    // fields are correctly set
    Assert.assertNotNull(doc1.getObjectId("_id"));
    Assert.assertNotNull(doc2.getObjectId("_id"));

    List<ObjectId> ids = new ArrayList<>();
    ids.add(doc1.getObjectId("_id"));
    ids.add(doc2.getObjectId("_id"));

    // And that we can retrieve them back.
    Assert.assertEquals(2, videoGames.countDocuments(Filters.in("_id", ids)));
  }

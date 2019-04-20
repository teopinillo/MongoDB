
    //setup
    private MongoCollection<Document> videoGames;
    videoGames = gamesDB.getCollection("video_games");
    
    /*
     * Once we have a collection to write to let's create a Document object
     * instance that will hold some information. In this case, we will
     * have a document that represents a video game.
     */

    // we set the first key, right in the constructor.
    Document doc = new Document("title", "Fortnite");

    // then we add a new set of document fields like year of the game launch
    // by appending a new key value pair
    doc.append("year", 2018);

    // and another one using put()
    doc.put("label", "Epic Games");

    // then we can insert this document by calling the collection insertOne
    // method
    videoGames.insertOne(doc);

    /*
     * insertOne method returns void. 
     * If an error occurred, during the insert of a given document, a
     * MongoWriteException would be thrown stating the origin/cause of
     * the error.
     * Otherwise, everything get's correctly written and the document is
     * stored in the database.
     * Where can we see the _id field? We did not set one, so surely 
     * it would still need to be set.
     */

    Assert.assertNotNull(doc.getObjectId("_id"));
    // On insert, the driver will set a _id field if it is not set by the
    // application, with the default datatype of ObjectId
    // https://docs.mongodb.com/manual/reference/method/ObjectId/index.html

    // This basically means that if we try to recover the document back from
    // the database, using this _id value, we will get the same document
    Document retrieved = videoGames.find(Filters.eq("_id", doc.getObjectId("_id"))).first();

    // Which we can assert that it is true
    Assert.assertEquals(retrieved, doc);

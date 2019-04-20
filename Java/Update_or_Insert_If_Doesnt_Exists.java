  /*
     * There are times where we are not sure if a document already exists
     * in the collection, and we just want to update it if it already
     * exists. Something like with "update" or "insert" if it does not
     * exist. Well, MongoDB allows for that in a very straightforward way.
     */

    // Let's go ahead and instantiate our document
    Document doc1 = new Document("title", "Final Fantasy");
    doc1.put("year", 2003);
    doc1.put("label", "Square Enix");

    // and instead of going to the database, run a query to check if the
    // document already exists, we are going to emit an update command
    // with the flag $upsert: true.

    // We set a query predicate that finds the video game based on his title
    Bson query = new Document("title", "Final Fantasy");

    // And we try to updated. If we do not provide the upsert flag
    UpdateResult resultNoUpsert = videoGames.updateOne(query, new Document("$set", doc1));

    // if the document does not exist, the number of matched documents
    Assert.assertEquals(0, resultNoUpsert.getMatchedCount());
    // should be 0, so as the number of modified documents.
    Assert.assertNotEquals(1, resultNoUpsert.getModifiedCount());

    // on the other hand, if we do provide an upsert flag by setting the
    // UpdateOptions document
    UpdateOptions options = new UpdateOptions();
    options.upsert(true);

    // and adding those options to the update method.
    UpdateResult resultWithUpsert =
        videoGames.updateOne(query, new Document("$set", doc1), options);

    // in this case both our number of modified count
    Assert.assertEquals(0, resultWithUpsert.getModifiedCount());
    // should be still 0 given that there was no document in the
    // collection, however we do have a upsertId as result of the insert
    Assert.assertNotNull(resultWithUpsert.getUpsertedId());
    // and should be of ObjectId type
    Assert.assertTrue(resultWithUpsert.getUpsertedId().isObjectId());

    // Another component of the update or insert, upsert, is that we can
    // set values just in case we are inserting.
    // Let's say we want add a field called, just_inserted, if the
    // document did not existed before, but do not set it if the document
    // already existed

    // let's try to update "Final Fantasy", which already exists:
    // we need to setup an object that defines the update operation,
    // set's the title and appends the field just_inserted with string "yes"
    Bson updateObj1 =
        Updates.combine(
            Updates.set("title", "Final Fantasy 1"), Updates.setOnInsert("just_inserted", "yes"));

    query = Filters.eq("title", "Final Fantasy");

    UpdateResult updateAlreadyExisting = videoGames.updateOne(query, updateObj1, options);

    // In this case, the field will not be present when we query for this
    // document
    Document finalFantasyRetrieved =
        videoGames.find(Filters.eq("title", "Final Fantasy 1")).first();
    Assert.assertFalse(finalFantasyRetrieved.keySet().contains("just_inserted"));

    // on the other hand, if the document is not updated, but inserted
    Document doc2 = new Document("title", "CS:GO");
    doc2.put("year", 2018);
    doc2.put("label", "Source");

    Document updateObj2 = new Document();
    updateObj2.put("$set", doc2);
    updateObj2.put("$setOnInsert", new Document("just_inserted", "yes"));

    UpdateResult newDocumentUpsertResult =
        videoGames.updateOne(Filters.eq("title", "CS:GO"), updateObj2, options);

    // Then, we will see the field correctly set, querying the collection
    // using the upsertId field in the update result object
    Bson queryInsertedDocument = new Document("_id", newDocumentUpsertResult.getUpsertedId());

    Document csgoDocument = videoGames.find(queryInsertedDocument).first();

    Assert.assertEquals("yes", csgoDocument.get("just_inserted"));

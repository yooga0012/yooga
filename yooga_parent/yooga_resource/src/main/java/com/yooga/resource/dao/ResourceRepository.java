package com.yooga.resource.dao;

import com.yooga.resource.pojo.MongoDBFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<MongoDBFile,String> {
    MongoDBFile findMongoDBFileById(String id);
}

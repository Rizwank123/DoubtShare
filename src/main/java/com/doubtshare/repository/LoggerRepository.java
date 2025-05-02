package com.doubtshare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.doubtshare.entity.LoggerEntity;

@Repository
public interface LoggerRepository extends MongoRepository<LoggerEntity, String> {

}

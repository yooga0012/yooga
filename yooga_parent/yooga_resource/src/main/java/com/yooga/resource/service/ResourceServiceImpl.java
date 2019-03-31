package com.yooga.resource.service;

import com.yooga.resource.dao.ResourceRepository;
import com.yooga.resource.pojo.MongoDBFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService{
    @Resource
    public ResourceRepository fileDao;

    @Override
    public MongoDBFile saveFile(MongoDBFile file) {
        return fileDao.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileDao.deleteById(id);
    }

    @Override
    public MongoDBFile getFileById(String id) {
        return fileDao.findMongoDBFileById(id);
    }

}

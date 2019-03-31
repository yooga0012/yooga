package com.yooga.resource.service;

import com.yooga.resource.pojo.MongoDBFile;

import java.util.List;

public interface ResourceService {
    /**
     * 保存文件
     *
     * @return
     */
    MongoDBFile saveFile(MongoDBFile file);

    /**
     * 删除文件
     *
     * @return
     */
    void removeFile(String id);

    /**
     * 根据id获取文件
     * @return
     */
    MongoDBFile getFileById(String id);

}

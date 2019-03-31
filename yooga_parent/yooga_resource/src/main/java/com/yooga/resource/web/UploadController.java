package com.yooga.resource.web;



import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.yooga.entity.result.Result;
import com.yooga.resource.dao.ContainRepository;
import com.yooga.resource.dao.ResourceRepository;
import com.yooga.resource.pojo.Contain;
import com.yooga.resource.pojo.MongoDBFile;
import com.yooga.resource.service.ResourceService;
import com.yooga.utils.IdWorker;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@CrossOrigin
public class UploadController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Resource
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private ContainRepository containRepository;

    @Autowired
    private IdWorker idWorker;

 
    /**
     * 	获取文件片信息
     * @return
     */
    @RequestMapping(value = "/findFileInfo", produces="application/json;charset=UTF-8",method = RequestMethod.GET)

    public ResponseEntity<Object> serveFile(HttpServletResponse response, HttpServletRequest request) {
        String mongodbId = request.getParameter("mongodbId");
        MongoDBFile file = resourceService.getFileById(mongodbId);
        if (file != null) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream" )
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize()+"")
                    .header("Connection",  "close")
                    .body( file.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件找不到");
        }

    }
    /**
     * 	在线显示文件
     * @return
     */
    @RequestMapping(value = "/ViewFileOnLine", produces="application/json;charset=UTF-8",method = RequestMethod.GET)

    public void  serveFileOnline(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Query query = Query.query(Criteria.where("_id").is(fileId));
        GridFSFile one = gridFsTemplate.findOne(query);
        GridFsResource gridFsResource=new GridFsResource(one,(GridFSBuckets.create(mongoDbFactory.getDb())).openDownloadStream(one.getObjectId()));
        String fileName = one.getFilename().replace(",", "");
        //处理中文文件名乱码
//        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
//                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
//                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
//            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
//        } else {
//            //非IE浏览器的处理：
//            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
//        }
        // 通知浏览器进行文件下载
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        IOUtils.copy(gridFsResource.getInputStream(),response.getOutputStream());

    }
    /**
     * 	上传接口
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Result handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("id")String id) throws IOException {
         InputStream inputStream = file.getInputStream();
        final String originalFilename = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        ObjectId test = gridFsTemplate.store(inputStream, originalFilename,contentType);

        //与用户连接
        Contain contain = new Contain();
        contain.setId(idWorker.nextId()+"");
        contain.setResourceId(test.toString());
        contain.setFilename(originalFilename);
        contain.setType(contentType);
        contain.setUserId(id);
        containRepository.save(contain);
        String s = test.toString();
        return new Result(true,200 , s);
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll(){
        List<Contain> all = containRepository.findAll();
        return new Result(true,200 , "success",all);

    }




    /**
     * 	删除文件
     * @return
     */
    @RequestMapping(value="deleteMongoDBfile",produces="application/json;charset=UTF-8")

    public void deleteFile(HttpServletResponse response, HttpServletRequest request) {
        
    }



}

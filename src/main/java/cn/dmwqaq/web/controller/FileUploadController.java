package cn.dmwqaq.web.controller;

import cn.dmwqaq.web.service.CloudObjectStorageService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
public class FileUploadController {

    @Resource
    private CloudObjectStorageService cloudObjectStorageService;

    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(HttpServletRequest request) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(getTmpFilePath());

        ServletFileUpload uploader = new ServletFileUpload(diskFileItemFactory);
        uploader.setHeaderEncoding("UTF-8");

        if (!ServletFileUpload.isMultipartContent(request)) {
            return JSON.toJSONString(new FileUploadResponse("-1", null));
        }

        try {
            List<FileItem> fileItems = uploader.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    String fieldName = fileItem.getFieldName();
                    String fieldValue = fileItem.getString();
                    System.out.println(fieldName + " " + fieldValue);
                } else {
                    String fileName = fileItem.getName();
                    if (fileName == null || fileName.trim().isEmpty()) {
                        continue;
                    }
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    String filePath = getTmpFilePath() + "\\" + fileName;
                    InputStream is = fileItem.getInputStream();
                    OutputStream os = new FileOutputStream(filePath);

                    byte[] b = new byte[1024];
                    int len;
                    while ((len = is.read(b)) != -1) {
                        os.write(b, 0, len);
                    }
                    os.close();
                    is.close();

                    File file = new File(filePath);
                    String fileUrl = cloudObjectStorageService.uploadFile(file);
                    return JSON.toJSONString(new FileUploadResponse("0", new String[] {fileUrl}));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(new FileUploadResponse("-1", null));
    }

    private File getTmpFilePath() {
        File file = new File("D:\\tmp");
        if (!file.exists() && !file.isDirectory()) {
            return file.mkdir() ? file : null;
        } else {
            return file;
        }
    }
}

class FileUploadResponse {

    String errno;
    String[] data;

    public FileUploadResponse(String errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}

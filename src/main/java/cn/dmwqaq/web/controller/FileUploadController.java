package cn.dmwqaq.web.controller;

import cn.dmwqaq.web.service.CloudObjectStorageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    private static Logger logger = LogManager.getLogger(FileUploadController.class);

    @Resource
    private CloudObjectStorageService cloudObjectStorageService;

    @PostMapping("/post")
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

    @ResponseBody
    @PostMapping(value = "/editormdImg", produces = "application/json; charset=utf-8")
    public String editormdImageUpload(MultipartHttpServletRequest request) {
        JSONObject object = new JSONObject();
        object.put("success", "-1");
        object.put("message", "上传失败！");
        object.put("url", "");

        MultipartFile multipartFile = request.getFile("image");
        try {
            assert multipartFile != null;
            FileItem fileItem = ((CommonsMultipartFile) multipartFile).getFileItem();
            String fileName = fileItem.getName();
            File tempFile = getTempFile(fileName);
            InputStream is = fileItem.getInputStream();
            OutputStream os = new FileOutputStream(tempFile);
            copyFileFromInputStream(is, os);

            String fileUrl = cloudObjectStorageService.uploadFile(tempFile);
            tempFile.delete();
            object.put("success", "1");
            object.put("url", fileUrl);
            object.put("message", "上传成功!");
            return JSON.toJSONString(object);
        } catch (NullPointerException e) {
            //
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(object);
    }

    private File getTmpFilePath() {
        File file = new File("D:\\tmp");
        if (!file.exists() && !file.isDirectory()) {
            return file.mkdir() ? file : null;
        } else {
            return file;
        }
    }

    private void copyFileFromInputStream(InputStream is, OutputStream os) throws IOException {
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) != -1) {
            os.write(b, 0, len);
        }
        os.close();
        is.close();
    }

    private @NonNull
    File getTempFile(String fileName) throws IOException {
        File file = new File("D:\\tmp\\" + fileName);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new RuntimeException("Mkdir failed.");
            }
        }
        return file;
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

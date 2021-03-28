package com.buger.system.ctrl;

import com.buger.system.FileServiceImpl;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;

@RestController
@ResponseBody
@RequestMapping("/file")
public class FileUpload {
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private HttpServletRequest request;
    @PostMapping("/uploadImage")
    public Object uploadImage(MultipartFile file){
        try {
            if (file.isEmpty()){
                return Result.error(CodeMsg.FILE_NULL);
            }
            return fileService.uploadImage(file,request.getIntHeader("id"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error(CodeMsg.FILE_NULL);
    }

    @GetMapping(value = "/image/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public Object getFile(@PathVariable long id) {
        try {
            FileInputStream inputStream = new FileInputStream(fileService.getFilePath(id));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            inputStream.close();
            return bytes;
        }catch (Exception e){
            return null;
        }
    }
}

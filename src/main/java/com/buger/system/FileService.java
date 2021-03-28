package com.buger.system;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public interface FileService {
    public Object uploadImage(MultipartFile file, int postId);
    public File getFile(int id);
    public String getFilePath(long id);
}

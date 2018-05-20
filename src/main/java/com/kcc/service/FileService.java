package com.kcc.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kcc.utils.Constants;

@Service("FileService")
public class FileService {

  public List<String> getFilePath(String filePath) {
    File file = new File(filePath);

    List<String> files = new ArrayList<>();
    if (file.isDirectory()) {
      for (String fileName : file.list()) {
        files.addAll(getFilePath(filePath + Constants.SLASH + fileName));
      }
    } else {
      files.add(filePath);
    }
    return files;
  }

}

package com.brpl.kyc.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileHandling {

	public String uploadImage(MultipartFile file, String path) {
		if(!file.isEmpty()) {
			String filename = file.getOriginalFilename();
			
			String randomId = UUID.randomUUID().toString();
			String nfile = randomId.concat(filename.substring(filename.lastIndexOf(".")));
			
			String filePath = path + File.separator + nfile;
			
			File f = new File(path);
			if(!f.exists()) {
				f.mkdir();
			}
			
			try {
				Files.copy(file.getInputStream(), Paths.get(filePath));
			} catch(IOException ex) {
				System.out.println("Internal error. "+ex.getMessage());				
			}
			
			return nfile;
		}
		return "default.png";
	}
}

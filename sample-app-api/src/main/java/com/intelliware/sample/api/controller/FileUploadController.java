package com.intelliware.sample.api.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
	
	//FIXME MZ/DJ authorization ? can someone hit this without being logged in ?
	//, consumes="application/json;charset=UTF-8"
	@RequestMapping(value="/fileupload", method=RequestMethod.POST, consumes="multipart/form-data")
	@ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] bytes;

        if (!file.isEmpty()) {
             bytes = file.getBytes();
             saveFileOnDisk(bytes, file.getName());
        }

        System.out.println(String.format("receive %s, %d", file.getOriginalFilename(), file.getBytes().length));
    }

	private void saveFileOnDisk(byte[] bytes, String name) {
		FileOutputStream fop = null;
		File file;
		
		try {
			 
			file = new File("c:/temp/" + name);
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			fop.write(bytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


}

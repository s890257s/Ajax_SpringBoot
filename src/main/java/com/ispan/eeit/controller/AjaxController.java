package com.ispan.eeit.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.eeit.bean.Users;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AjaxController {

	@GetMapping("/GetPlainText")
	public String getPlainText() {

		return "Hello!這是純文字回應!";
	}

	@GetMapping("/GetJson")
	public List<Users> getJson(HttpServletRequest request, @RequestParam(defaultValue = "false") Boolean isWithBytes) {

		String usersFolderPath = request.getServletContext().getRealPath("") + "/users";
		List<Users> uList = getAllUsers(usersFolderPath, isWithBytes);

		return uList;
	}

	//讀取使用者的私有方法
	private static List<Users> getAllUsers(String usersFolderPath, Boolean isWithBytes) {
		String[] files = new File(usersFolderPath).list();

		ArrayList<Users> uList = new ArrayList<Users>();

		for (String fName : files) {
			Users u = new Users();
			u.setId(fName.substring(0, 3));
			u.setAge(20 + Integer.valueOf(fName.substring(1, 3)));
			u.setName("N" + fName.substring(1, 3));

			if (isWithBytes) {
				try {
					BufferedInputStream bis = new BufferedInputStream(
							new FileInputStream(usersFolderPath + "/" + fName));
					u.setPhoto(bis.readAllBytes());
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			uList.add(u);
		}

		return uList;
	}

	@PostMapping("/GetImg")
	public byte[] getImg(@RequestParam(name = "userID") String uID, HttpServletRequest request) {
		String usersFolderPath = request.getServletContext().getRealPath("") + "/users/";
		uID = uID.toUpperCase();

		if (!uID.contains("U")) {
			uID = "U" + uID;
		}

		if (!uID.contains("0")) {
			uID = uID.replace("U", "U0");
		}

		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(usersFolderPath + uID + ".png"));
			byte[] bytes = bis.readAllBytes();
			bis.close();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/PostImg")
	public void postImg(@RequestParam String fName, @RequestParam MultipartFile fFile) {
		// 若C槽下沒有temp資料夾，則建立一個。
		File folder = new File("C:\\temp");
		if (!folder.exists()) {
			folder.mkdir();
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("c:\\temp\\" + fName));
			bos.write(fFile.getBytes());
			bos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

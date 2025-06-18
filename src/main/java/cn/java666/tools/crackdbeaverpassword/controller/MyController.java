package cn.java666.tools.crackdbeaverpassword.controller;

import cn.java666.tools.crackdbeaverpassword.JsonCombUtil;
import cn.java666.tools.crackdbeaverpassword.service.MyService;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author Geek
 * @date 2021-03-25 19:48:19
 */

@RestController
@RequestMapping("/decrypt")
public class MyController {

    private String password;

    @Resource
    private MyService myService;

    @GetMapping("/default")
    public String decrypt() {
        String res = myService.decrypt(null);
        System.out.println(res);
        return res;
    }

    @PostMapping("/upload")
    public String decrypt(@RequestParam("file") MultipartFile file) {
        String res = myService.decrypt(file);
        System.out.println(res);
        password = res;
        return res;
    }

    @PostMapping("/upload2")
    public String decrypt2(@RequestParam("file") MultipartFile file) {
        String secondJsonString = null;
        try {
            if (file != null && !file.isEmpty()) {
                secondJsonString = new String(file.getBytes(), StandardCharsets.UTF_8); // Important: Specify UTF-8 encoding
            } else {
                System.err.println("Uploaded file is empty or null.");
                return "Error: No file uploaded."; // Or return an appropriate error message
            }
        } catch (IOException e) {
            System.err.println("Error reading uploaded file: " + e.getMessage());
            e.printStackTrace();
            return "Error: Could not read file."; // Or return an appropriate error message
        }

        System.out.println(secondJsonString);
        String res;
        try {
            res = JsonCombUtil.combineConnections(password, secondJsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        password= StringUtils.EMPTY;
        return res;
    }

}

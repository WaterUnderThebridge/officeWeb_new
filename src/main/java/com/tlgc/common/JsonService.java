package com.tlgc.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Service
public class JsonService {
    @Value(value = "/json/topicSetting.json")
    private Resource data;

    public JSONObject getThemeData() {
        try {
            File file = data.getFile();
            long t0 = System.nanoTime();
            String jsonString = this.jsonRead(file);

            JSONObject jsonObj = JSON.parseObject(jsonString);
            long t1 = System.nanoTime();
            long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
            //System.out.println(millis +"ms");
            //System.out.println(jsonData);
            return jsonObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取文件类容为字符串
     *
     * @param file
     * @return
     */
    private String jsonRead(File file) {
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }

    public void jsonWrite(String filePath, String name) {
        try {
            String fileName = filePath + name;
            JSONObject jsonObject = getThemeData();

            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    fileName));// 输出新的json文件

            bw.write(jsonObject.toString());
            // bw.newLine();
            bw.flush();
            bw.close();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getFullPath(String relativePath) {
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) path = new File("");
            System.out.println("path:" + path.getAbsolutePath());

            //"static/images/upload/"
            File fullPath = new File(path.getAbsolutePath(), relativePath);
            if (!fullPath.exists()) fullPath.mkdirs();
            System.out.println("upload url:" + fullPath.getAbsolutePath());
            return fullPath.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
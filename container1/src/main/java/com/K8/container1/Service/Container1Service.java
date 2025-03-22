package com.K8.container1.Service;

import com.K8.container1.Model.File_Product;
import com.K8.container1.Model.File_details;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class Container1Service {

    private final RestTemplate restTemplate;

    public Container1Service(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String directory = "/SakthiSharan_PV_dir/";

    public Map<String, Object> validateFile(File_details fileDetails){
        String fileName = fileDetails.getFile();
        String data = fileDetails.getData();
        if(fileName.isEmpty()){
            Map<String, Object> noFileName = new HashMap<>();
            noFileName.put("file",null);
            noFileName.put("error","Invalid JSON input.");
            return noFileName;
        }

        Boolean isFileCreated = createFile(fileName,data);
        Map<String, Object> noFile = new HashMap<>();
        noFile.put("file",fileName);

        if(isFileCreated){
            noFile.put("message","Success.");
            return noFile;
        }
        else{
            noFile.put("error","Error while storing the file to the storage.");
            return noFile;
        }
    }

    public Boolean createFile(String fileName, String data) {
        try {
            Path path = Paths.get(directory + fileName);
            Files.write(path, data.getBytes());
            return true;
        } catch (IOException e) {
            return false;
            }
    }

    public Map<String,Object> forwardToContainer2(File_Product fileProduct){
        String fileName = fileProduct.getFile();
        String product = fileProduct.getProduct();
        Map<String,Object> response = new HashMap<>();

        if(fileName.isEmpty()){
            response.put("file",null);
            response.put("error","Invalid JSON input.");
        }
        else {
            String container2Url = "http://k8s-container2-service:8081/calculate";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("file", fileName);
            requestBody.put("product", product);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<Map> container2Response = restTemplate.exchange(
                        container2Url,
                        HttpMethod.POST,
                        requestEntity,
                        Map.class
                );

                response = container2Response.getBody();
            } catch (Exception e) {
                System.err.println("Error while calling Container 2: " + e.getMessage());
                response.put("error", "Error while communicating with Container 2.");
                return response;
            }

        }
        return response;
    }


}

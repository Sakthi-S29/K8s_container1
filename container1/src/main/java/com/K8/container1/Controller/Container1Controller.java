package com.K8.container1.Controller;

import com.K8.container1.Model.File_Product;
import com.K8.container1.Model.File_details;
import com.K8.container1.Service.Container1Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class Container1Controller {

    private final Container1Service container1Service;

    public Container1Controller(Container1Service container1Service) {
        this.container1Service = container1Service;
    }

    @PostMapping("/store-file")
    public ResponseEntity<Map<String,Object>> file(@RequestBody File_details file_details) {
        Map<String,Object> responseFromService = container1Service.validateFile((file_details));
        if(responseFromService.containsKey("error")){
            return ResponseEntity.internalServerError().body(responseFromService);
        }
        else {
            return ResponseEntity.ok().body(responseFromService);
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<Map<String,Object>> calculate(@RequestBody File_Product fileProduct) {
        Map<String,Object> responseFromService = container1Service.forwardToContainer2(fileProduct);
        if(responseFromService.containsKey("error")){
            return ResponseEntity.internalServerError().body(responseFromService);
        }
        else {
            return ResponseEntity.ok().body(responseFromService);
        }
    }

}

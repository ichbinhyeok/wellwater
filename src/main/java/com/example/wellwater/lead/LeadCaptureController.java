package com.example.wellwater.lead;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

@Controller
public class LeadCaptureController {

    @PostMapping("/lead/submit")
    @ResponseBody
    public ResponseEntity<Void> submit() {
        return ResponseEntity.status(410).build();
    }
}

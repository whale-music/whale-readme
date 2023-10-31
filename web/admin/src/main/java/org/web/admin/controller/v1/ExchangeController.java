package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.service.ExchangeApi;
import org.common.result.R;
import org.core.common.annotation.AnonymousAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@RestController(AdminConfig.ADMIN + "UploadController")
@RequestMapping("/admin/exchange")
@Slf4j
public class ExchangeController {
    @Autowired
    private ExchangeApi exchangeApi;
    
    @AnonymousAccess
    @PostMapping("/import/excel")
    public R importExcel(@RequestBody MultipartFile file) throws IOException {
        exchangeApi.importExcel(file);
        return R.success();
    }
    
    @AnonymousAccess
    @GetMapping("/export/excel")
    public ResponseEntity<StreamingResponseBody> exportExcel() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "music.xlsx");  // 下载文件的名称
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(exchangeApi.exportExcel(), headers, HttpStatus.OK);
    }
}

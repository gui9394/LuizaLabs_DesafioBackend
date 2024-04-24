package com.gui9394.order.file_process;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileProcessController {

    private final FileProcessService fileProcessService;

    @PostMapping(
            path = "/file/process",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Object fileImport(

            @RequestParam("file")
            MultipartFile file

    ) {
        return fileProcessService.process(file.getResource()).getStatus();
    }

}

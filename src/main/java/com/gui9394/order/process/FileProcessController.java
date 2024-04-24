package com.gui9394.order.process;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Pedidos")
@RestController
@RequiredArgsConstructor
public class FileProcessController {

    private final FileProcessService fileProcessService;

    @PostMapping(
            path = "/orders/process",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void process(

            @RequestParam("file")
            MultipartFile file

    ) {
        fileProcessService.process(file.getResource());
    }

}

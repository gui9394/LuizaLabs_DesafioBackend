package com.gui9394.order.process;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class FileProcessService {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final FileLineReader reader;

    @SneakyThrows
    public JobExecution process(Resource resource) {
        this.reader.setResource(resource);
        var parameters = new JobParametersBuilder()
                .addString("fileName", resource.getFilename())
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();

        return this.jobLauncher.run(this.job, parameters);
    }

}
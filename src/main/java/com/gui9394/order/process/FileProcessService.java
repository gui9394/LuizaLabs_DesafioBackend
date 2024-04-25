package com.gui9394.order.process;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileProcessService {

    private final Tracer tracer;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final FileLineReader reader;

    @SneakyThrows
    public FileProcessResult process(Resource resource) {
        this.reader.setResource(resource);
        var parameters = new JobParametersBuilder()
                .addString("fileName", resource.getFilename())
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();

        return buildResult(this.jobLauncher.run(this.job, parameters));
    }

    private FileProcessResult buildResult(JobExecution jobExecution) {
        return new FileProcessResult(
                tracer.currentSpan().context().traceId(),
                jobExecution.getJobId(),
                jobExecution.getStatus(),
                Objects.isNull(jobExecution.getStartTime()) || Objects.isNull(jobExecution.getEndTime())
                        ? Duration.ZERO
                        : Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime())
        );
    }

}
package com.gui9394.order.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;
import java.util.Objects;

@Slf4j
class FileProcessJobListener implements JobExecutionListener {

    public static final String AFTER_MESSAGE = "id={} status={} durationMs={} fileName={}";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(
                "id={} status={} fileName={}",
                jobExecution.getJobId(),
                jobExecution.getStatus().name(),
                jobExecution.getJobParameters().getString("fileName")
        );
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        var params = new Object[]{
                jobExecution.getJobId(),
                jobExecution.getStatus().name(),
                Objects.isNull(jobExecution.getStartTime()) || Objects.isNull(jobExecution.getEndTime())
                        ? 0
                        : Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis(),
                jobExecution.getJobParameters().getString("fileName")
        };

        switch (jobExecution.getStatus()) {
            case COMPLETED -> log.info(AFTER_MESSAGE, params);
            case FAILED -> log.error(AFTER_MESSAGE, params);
            default -> log.warn(AFTER_MESSAGE, params);
        }
    }

}

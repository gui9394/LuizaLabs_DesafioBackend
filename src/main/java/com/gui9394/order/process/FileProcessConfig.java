package com.gui9394.order.process;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FileProcessConfig {

    @Bean
    public Job fileProcessJob(
            JobRepository jobRepository,
            Step step
    ) {
        return new JobBuilder("FILE_PROCESS", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step fileProcessStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FileLineReader reader,
            FileLineWriter writer
    ) {
        return new StepBuilder("FILE_PROCESS", jobRepository)
                .<FileLine, FileLine>chunk(10000, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

}

package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired 
	DataSource dataSource;
	
	
	@Bean
    JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("Job Started: " + jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("Job Completed: " + jobExecution.getJobInstance().getJobName());
            }
        };
    }

    @Bean
    FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeReader")
                .resource(new ClassPathResource("employees.csv"))
                .delimited()
                .names("name", "department", "salary")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                    setTargetType(Employee.class);
                }})
                .build();
    }

    @Bean
    EmployeeProcessor processor() {
        return new EmployeeProcessor();
    }
    

    @Bean
    JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO employee (name, department, salary) VALUES (:name, :department, :salary)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    Job importEmployeeJob(JobBuilderFactory jobBuilderFactory, JobExecutionListener listener, Step step1) {
        return jobBuilderFactory.get("importEmployeeJob")
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    Step step1(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step1")
                .<Employee, Employee>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource))
                .build();
    }
}

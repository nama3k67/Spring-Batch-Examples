package springbatchproject.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import springbatchproject.components.JobCompletionNotificationListener;
import springbatchproject.model.Information;
import springbatchproject.processor.InforItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Value("${file.input}")
	private String fileInput;

	
	@Bean
	public InforItemProcessor processor() {
		return new InforItemProcessor();
	}
	
	@Bean
	public FlatFileItemReader<Information> reader() {
		return new FlatFileItemReaderBuilder<Information>().name("InformationReader")
				.resource(new ClassPathResource(fileInput)).delimited()
				.names(new String[] { "name", "classes", "avgMark" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Information>() {
					{
						setTargetType(Information.class);
					}
				}).build();
	}

	@Bean
	public JdbcBatchItemWriter<Information> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Information>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO information (name, class, avg_mark, classification) VALUES (:name, :classes, :avgMark, :classification)")
				.dataSource(dataSource).build();
	}
	
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
	
	@Bean
	public Step step1(JdbcBatchItemWriter<Information> writer) {
		return stepBuilderFactory.get("step1")
	            .<Information, Information> chunk(10)
	            .reader(reader())
	            .processor(processor())
	            .writer(writer)
	            .build();
	}
}

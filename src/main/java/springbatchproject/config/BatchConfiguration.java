package springbatchproject.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbatchproject.tasklets.LinesProcessor;
import springbatchproject.tasklets.LinesReader;
import springbatchproject.tasklets.LinesWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public LinesReader linesReader(){
		return new LinesReader();
	}

	@Bean
	public LinesProcessor linesProcessor(){
		return new LinesProcessor();
	}

	@Bean
	public LinesWriter linesWriter(){
		return new LinesWriter();
	}

	@Bean
	protected Step readLines() {
		return steps
				.get("readLines")
				.tasklet(linesReader())
				.build();
	}

	@Bean
	protected Step processLines() {
		return steps
				.get("processLines")
				.tasklet(linesProcessor())
				.build();
	}

	@Bean
	protected Step writeLines() {
		return steps
				.get("writeLines")
				.tasklet(linesWriter())
				.build();
	}

	@Bean
	public Job job() {
		return jobs
				.get("taskletJob")
				.start(readLines())
				.next(processLines())
				.next(writeLines())
				.build();
	}
}

package springbatchproject.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbatchproject.chunks.LineProcessor;
import springbatchproject.chunks.LineReader;
import springbatchproject.chunks.LineWriter;
import springbatchproject.model.Line;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public ItemReader<Line> itemReader(){
		return new LineReader();
	}

	@Bean
	public ItemProcessor<Line, Line> itemProcessor(){
		return new LineProcessor();
	}

	@Bean
	public ItemWriter<Line> itemWriter(){
		return new LineWriter();
	}

	@Bean
	protected Step processLines(ItemReader<Line> reader, ItemProcessor<Line, Line> processor, ItemWriter<Line> writer){
		return steps.get("processLines").<Line, Line> chunk(2)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}

	@Bean
	public Job job() {
		return jobs
				.get("chunksJob")
				.start(processLines(itemReader(), itemProcessor(), itemWriter()))
				.build();
	}
}

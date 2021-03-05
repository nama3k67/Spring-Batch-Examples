package springbatchproject.components;

//**Listen notification (after/before) job to execute reasonable function **// 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import springbatchproject.model.Information;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("Job finished! Time to verify the results");

			String query = "SELECT name, class, avg_mark, classification FROM information";
			jdbcTemplate.query(query,
					(rs, row) -> new Information(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)))
					.forEach(infor -> LOGGER.info("Found < {} > in database.", infor));
		}
	}
}

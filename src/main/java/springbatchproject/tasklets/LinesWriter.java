package springbatchproject.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import springbatchproject.model.Line;
import springbatchproject.utils.FileUtils;

import java.util.List;

public class LinesWriter implements Tasklet, StepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(LinesWriter.class);
    private List<Line> lines;
    private FileUtils fu;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        this.lines = (List<Line>) executionContext.get("lines");
        fu = new FileUtils("D:\\dl khach\\Spring-Batch-Examples\\src\\main\\resources\\result.csv");
        logger.debug("Lines Writer initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fu.closeWriter();
        logger.debug("Line Writer ended");
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        for (Line line : lines){
            fu.writeLine(line);
            logger.debug("Wrote line "+ line.toString());
        }
        return RepeatStatus.FINISHED;
    }
}

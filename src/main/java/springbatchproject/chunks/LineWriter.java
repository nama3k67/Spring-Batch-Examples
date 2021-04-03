package springbatchproject.chunks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import springbatchproject.model.Line;
import springbatchproject.utils.FileUtils;
import java.util.List;

public class LineWriter implements ItemWriter<Line>, StepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(LineWriter.class);
    private FileUtils fu;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        fu = new FileUtils("D:\\dl khach\\Spring-Batch-Examples\\src\\main\\resources\\result.csv");
        logger.debug("Line Writer initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fu.closeWriter();
        logger.debug("Line Writer ended.");
        return ExitStatus.COMPLETED;
    }

    @Override
    public void write(List<? extends Line> lines) throws Exception {
        for (Line line : lines) {
            fu.writeLine(line);
            logger.debug("Wrote line " + line.toString());
        }
    }
}

package springbatchproject.chunks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import springbatchproject.model.Line;
import springbatchproject.utils.FileUtils;

public class LineReader implements ItemReader<Line>, StepExecutionListener{
    private final Logger logger = LoggerFactory.getLogger(LineReader.class);
    private FileUtils fu;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        fu = new FileUtils("D:\\dl khach\\Spring-Batch-Examples\\src\\main\\resources\\inforList.csv");
        logger.debug("Line Reader initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fu.closeReader();
        logger.debug("Line Reader ended.");
        return ExitStatus.COMPLETED;
    }

    @Override
    public Line read() throws Exception{
        Line line = fu.readLine();
        if (line != null) {
            logger.debug("Read line: "+ line.toString());
        }
        return line;
    }
}


package springbatchproject.processor;

//** apply some business logic to item **//
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import springbatchproject.model.Information;

public class InforItemProcessor implements ItemProcessor<Information, Information>{
	private static final Logger LOGGER = LoggerFactory.getLogger(InforItemProcessor.class);

	@Override
	public Information process(final Information information) throws Exception {
		String name = information.getName().toUpperCase();
		String classes = information.getClasses();
		double avgMark = information.getAvgMark(); 
		
		String classification = "Excellent"; 
		if (avgMark<0 || avgMark>10) {
			classification = "Error! Please check input again.";
		}else if (avgMark<6) {
			classification = "Failing"; 
		}else if (avgMark<7) {
			classification = "Bellow Average"; 
		}else if (avgMark<8) {
			classification = "Average"; 
		}else if (avgMark<9) {
			classification = "Very good";
		}
		
		Information transformedInformation = new Information(name, classes, avgMark, classification); 
		
		LOGGER.info("Converting ( {} into ( {} )", information, transformedInformation);
		
		return transformedInformation;
	}	
}

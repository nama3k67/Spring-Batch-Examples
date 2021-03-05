package springbatchproject.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Information {
	private String name;
	private String classes;
	private double avgMark;
	private String classification;
}

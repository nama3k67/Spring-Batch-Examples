package springbatchproject.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Line {
    private String name;
    private LocalDate dob;
    private Long age;

    public Line(String name, LocalDate dob) {
        this.name = name;
        this.dob = dob;
    }
}

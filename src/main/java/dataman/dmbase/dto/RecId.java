package dataman.dmbase.dto;



import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Data
@AllArgsConstructor
public class RecId implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    String recIdValue;
    Long counter;
    String prefix;

    public RecId() {

    }

    public RecId(String recIdValue, Long counter) {
        this.recIdValue = recIdValue;
        this.counter = counter;
    }

}


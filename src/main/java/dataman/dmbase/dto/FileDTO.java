package dataman.dmbase.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO{
	
	private byte[] data;
	private String fileType;
	private Long fileSize;
	
}
package dataman.dmbase.documentutil;

import com.mongodb.client.gridfs.model.GridFSFile;
import dataman.dmbase.dto.FileDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DocumentUtil {

    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final MongoTemplate mongoTemplate;
    private final GridFsTemplate gridFsTemplate;

    public DocumentUtil(MongoDatabaseFactory mongoDatabaseFactory, MongoTemplate mongoTemplate) {
        this.mongoDatabaseFactory = mongoDatabaseFactory;
        this.mongoTemplate = mongoTemplate;
        this.gridFsTemplate = new GridFsTemplate(mongoDatabaseFactory, mongoTemplate.getConverter());
    }

    /**
     * Store file in the specified bucket.
     */
//    public String storeFile(String bucketName, MultipartFile file) throws IOException {
//        ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
//        return objectId.toString();
//    }
    
    
    public String storeFile(String bucketName, MultipartFile file) throws IOException {
        GridFsTemplate customGridFsTemplate = new GridFsTemplate(mongoDatabaseFactory, mongoTemplate.getConverter(), bucketName);
        ObjectId objectId = customGridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return objectId.toString();
    }


    public String storeFile(String bucketName, byte[] fileData, String fileName, String contentType) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
        ObjectId objectId = gridFsTemplate.store(inputStream, fileName, contentType);
        return objectId.toString();
    }

    /**
     * Retrieve file as a GridFsResource.
     */
    public GridFsResource getFile(String bucketName, String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        return gridFSFile != null ? gridFsTemplate.getResource(gridFSFile) : null;
    }

    public byte[] getFileAsBytes(String bucketName, String id) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        if (gridFSFile != null) {
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            try (InputStream inputStream = resource.getInputStream()) {
                return inputStream.readAllBytes();
            }
        } else {
            throw new IllegalArgumentException("File with ID " + id + " not found in bucket: " + bucketName);
        }
    }

    /**
     * Delete file from the specified bucket.
     */
    public void deleteFileByIdAndCollectionName(String bucketName, String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
    }

    public FileDTO getFileResponse(String bucketName, String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        if (gridFSFile == null) {
            return null;
        }

        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);

        try (InputStream inputStream = resource.getInputStream()) {
            byte[] data = inputStream.readAllBytes();
            String contentType = resource.getContentType();

            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            FileDTO fileResponse = new FileDTO();
            fileResponse.setData(data);
            fileResponse.setFileType(contentType);
            return fileResponse;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file from GridFS", e);
        }
    }
}




//package dataman.dmbase.documentutil;
//
//import com.mongodb.client.gridfs.model.GridFSFile;
//
//import dataman.dmbase.dto.FileDTO;
//
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.gridfs.GridFsResource;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//
//import org.springframework.http.MediaType;
//
//
//@Service
//public class DocumentUtil {
//	
//	
//	@Autowired
//    private MongoDatabaseFactory mongoDatabaseFactory;
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    /**
//     * Dynamically create GridFsTemplate with a custom bucket name.
//     */
//    private GridFsTemplate getGridFsTemplate(String bucketName) {
//        return new GridFsTemplate(mongoDatabaseFactory, mongoTemplate.getConverter(), bucketName);
//    }
//
//    /**
//     * Store file in the specified bucket.
//     */
//    public String storeFile(String bucketName, MultipartFile file) throws IOException {
//        GridFsTemplate gridFsTemplate = getGridFsTemplate(bucketName);
//        ObjectId objectId = gridFsTemplate.store(
//                file.getInputStream(),
//                file.getOriginalFilename(),
//                file.getContentType()
//        );
//        return objectId.toString();
//    }
//    
//    
//	// data
//	public String storeFile(String bucketName, byte[] fileData, String fileName, String contentType)
//			throws IOException {
//		// Create an InputStream from byte[]
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
//
//		GridFsTemplate gridFsTemplate = getGridFsTemplate(bucketName);
//		// Store the file in the specified bucket
//		ObjectId objectId = gridFsTemplate.store(inputStream, // InputStream containing file data
//				fileName, // Original file name
//				contentType // MIME type of the file (e.g., "image/png", "text/plain")
//		);
//
//		// Return the file ID as a String
//		return objectId.toString();
//	}
//
//    /**
//     * Retrieve file from the specified bucket.
//     */
//    public GridFsResource getFile(String bucketName, String id) {
//        GridFsTemplate gridFsTemplate = getGridFsTemplate(bucketName);
//        GridFSFile gridFSFile = gridFsTemplate.findOne(
//                new org.springframework.data.mongodb.core.query.Query(
//                        org.springframework.data.mongodb.core.query.Criteria.where("_id").is(id)
//                )
//        );
//        return gridFSFile != null ? gridFsTemplate.getResource(gridFSFile) : null;
//    }
//    
//    
//    //Get file as array of bytes
//    public byte[] getFileAsBytes(String bucketName, String id) throws IOException {
//        // Get the GridFsTemplate for the specified bucket
//        GridFsTemplate gridFsTemplate = getGridFsTemplate(bucketName);
//
//        // Fetch the file using its ID
//        GridFSFile gridFSFile = gridFsTemplate.findOne(
//                new Query(Criteria.where("_id").is(id))
//        );
//
//        if (gridFSFile != null) {
//            // Get the file as a resource
//            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
//
//            // Read the file data into a byte array
//            try (InputStream inputStream = resource.getInputStream()) {
//                return inputStream.readAllBytes(); // Java 9+ method
//            } catch (IOException e) {
//                throw new IOException("Failed to read file from GridFS", e);
//            }
//        } else {
//            throw new IllegalArgumentException("File with ID " + id + " not found in bucket: " + bucketName);
//        }
//    }
//
//    /**
//     * Delete file from the specified bucket.
//     */
//    public void deleteFileByIdAndCollectionName(String bucketName, String id) {
//        GridFsTemplate gridFsTemplate = getGridFsTemplate(bucketName);
//        gridFsTemplate.delete(
//                new org.springframework.data.mongodb.core.query.Query(
//                        org.springframework.data.mongodb.core.query.Criteria.where("_id").is(id)
//                )
//        );
//    }
//    
//    
//    @SuppressWarnings("unused")
//	@Autowired
//    private GridFsTemplate gridFsTemplate;
//
//    /**
//     * Get file content and type from GridFS.
//     *
//     * @param bucketName The bucket name.
//     * @param id         The file ID.
//     * @return FileDataResponse containing fileType and data.
//     */
//    public FileDTO getFileResponse(String bucketName, String id) {
//        GridFsTemplate gridFsTemplate = getGridFsTemplate(bucketName);
//
//        // Find file by ID
//        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
//
//        if (gridFSFile == null) {
//            return null;
//        }
//
//        // Get file resource
//        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
//
//        try (InputStream inputStream = resource.getInputStream()) {
//            byte[] data = inputStream.readAllBytes(); // Read file content into byte[]
//            String contentType = resource.getContentType();
//
//            // Fallback to default type if null
//            if (contentType == null) {
//                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//            }
//            
//            //return new FileDataResponse(contentType, data);
//            
//            FileDTO fileResponse = new FileDTO();
//            
//            fileResponse.setData(data);
//            fileResponse.setFileType(contentType);
//            
//            return fileResponse;
//
//            
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read file from GridFS", e);
//        }
//    }
//    
//    
//    
//
//}
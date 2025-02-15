package dataman.dmbase.redissessionutil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisObjectUtil {

    private final RedisTemplate<String, Object> redisTemplateForObject;

    @Autowired
    public RedisObjectUtil(@Qualifier("redisTemplateForObject") RedisTemplate<String, Object> redisTemplateForObject) {
        this.redisTemplateForObject = redisTemplateForObject;
    }

    // Save the object as a hash and set a timeout on the entire key
    public void saveObjectAsHash(String objectKey, Map<String, Object> objectData, long timeout, TimeUnit timeUnit) {
        redisTemplateForObject.opsForHash().putAll(objectKey, objectData);
        // Set expiration for the entire hash
        redisTemplateForObject.expire(objectKey, timeout, timeUnit);
    }
    public void addFieldToHash(String objectKey, String fieldKey, Object fieldValue) {
        redisTemplateForObject.opsForHash().put(objectKey, fieldKey, fieldValue);
        redisTemplateForObject.expire(objectKey, 300, TimeUnit.SECONDS);
    }
    
    public void deleteFieldFromHash(String objectKey, String fieldKey) {
        redisTemplateForObject.opsForHash().delete(objectKey, fieldKey);
    }


    public Object getObjectValue(String objectKey, String fieldKey) {
        return redisTemplateForObject.opsForHash().get(objectKey, fieldKey);
    }
    
    public Map<Object, Object> getObjectAsHash(String objectKey) {
        return redisTemplateForObject.opsForHash().entries(objectKey);
    }
    
    public void deleteHash(String objectKey) {
        redisTemplateForObject.delete(objectKey);
    }


}





//package dataman.dmbase.redissessionutil;
//
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RedisObjectUtil {
//
//    private final RedisTemplate<String, Object> redisTemplateForObject;
//
//    @Autowired
//    public RedisObjectUtil(RedisTemplate<String, Object> redisTemplateForObject) {
//        this.redisTemplateForObject = redisTemplateForObject;
//    }
//
// // Save the object as a hash and set a timeout on the entire key
//    public void saveObjectAsHash(String objectKey, Map<String, Object> objectData, long timeout, TimeUnit timeUnit) {
//    	redisTemplateForObject.opsForHash().putAll(objectKey, objectData);
//        // Set expiration for the entire hash
//    	redisTemplateForObject.expire(objectKey, timeout, timeUnit);
//    }
//
//    public Object getObjectValue(String objectKey, String fieldKey) {
//        return redisTemplateForObject.opsForHash().get(objectKey, fieldKey);
//    }
//}
//

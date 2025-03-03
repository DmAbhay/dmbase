package dataman.dmbase.redissessionutil;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSimpleKeyValuePairUtil {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisSimpleKeyValuePairUtil(@Qualifier("redisTemplateForString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeKey(String key, String value, long expirationTime, TimeUnit timeUnit) {
    	
    	//System.out.println("store Auth Key method is called");
//    	System.out.println("Key is about to save");
//        long expiry = (expirationTime > 0) ? expirationTime : TimeUnit.MINUTES.toMillis(30);
//        redisTemplate.opsForValue().set(key, "authKey", expiry, TimeUnit.MILLISECONDS);
    	
    	System.out.println("Key is about to save");
        long expiry = (expirationTime > 0) ? expirationTime : 30;
        timeUnit = (expirationTime > 0) ? timeUnit : TimeUnit.MINUTES;
        redisTemplate.opsForValue().set(key, value, expiry, timeUnit);
    }
    
    

    public void deleteKey(String key) {
    	System.out.println("Key is about to save");
        redisTemplate.delete(key);
    }

    public String getKey(String key) {
    	System.out.println("you are about to dive in redis database");
        return redisTemplate.opsForValue().get(key);
    }

}


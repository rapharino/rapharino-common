
package org.rapharino.common.guavacache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

/**
 * Created By Rapharino on 2017/11/29 下午3:55
 */
public class LoadingCacheTest {

    private static LoadingCache<String, Long> CACHE;

    @Before
    public void setUp() throws Exception {

        CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).initialCapacity(10)
                .maximumSize(Long.MAX_VALUE).removalListener((RemovalListener<String, Long>) notification -> System.out
                        .println(notification.getKey() + "is removal cache"))
                .build(new CacheLoader<String, Long>() {

                    @Override
                    public Long load(String key) throws Exception {
                        return new Long(0);
                    }
                });
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(CACHE.size());
    }

    @Test
    public void Test() throws Exception {
        Map<String,Long> map = CACHE.asMap();


        for (int i = 0; i < 10; i++) {
            map.put(i+"",new Long(i));
            Thread.sleep(1000);
        }
    }
}

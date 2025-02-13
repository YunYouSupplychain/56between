package org.mybatis.spring;

import com.google.common.collect.Sets;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.Set;

/**
 * @author yunyou
 * @since 2023/3/28 18:39
 */
public class TypeAliasesUtils {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static Class<?>[] findTypeAliases(String scanPackageName) {
        if (StringUtils.isBlank(StringUtils.stripToNull(scanPackageName))) {
            return new Class[0];
        }

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        Resource[] resources;
        try {
            resources = resolver.getResources(scanPackageName);
        } catch (IOException e) {
            return new Class[0];
        }
        Set<Class<?>> classList = Sets.newHashSet();
        for (Resource resource : resources) {
            if (!resource.isReadable()) {
                continue;
            }
            try {
                Class<?> aClass = Class.forName(metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName());
                if (BaseEntity.class.isAssignableFrom(aClass)) {
                    classList.add(aClass);
                }
            } catch (ClassNotFoundException | IOException ignored) {
            }
        }
        return classList.toArray(new Class[0]);
    }
}

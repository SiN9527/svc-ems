package com.svc.ems.utils;

/**
 * ClassName: com.sweetolive.exhibition_backend.util.MapperUtil
 * Package: com.sweetolive.exhibition_backend.util
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午8:57
 * @Version 1.0
 */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供對象之間映射的工具類，通過 Jackson 的 ObjectMapper 實現對象的轉換。
 */
@Component
public class MapperUtils {
    private static ObjectMapper objectMapper;

    @Autowired
    public MapperUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 將原對象轉換為目標對象類型。
     *
     * @param source           原對象，即需要轉換的對象。
     * @param destinationClass 目標對象的類，即要轉換成的目標對象類型。
     * @param <S>              原對象類型。
     * @param <D>              目標對象類型。
     * @return 目標對象類型的實例，其中包含了原對象的属性。
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {
        ObjectMapper mapper = objectMapper.copy();
        // 根據需要配置ObjectMapper，例如忽略未知屬性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁用@JsonProperty注解，對應屬性名稱
        mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        return mapper.convertValue(source, destinationClass);
    }

    /**
     * 將原對象列表轉換為目標對象列表，直接對應屬性名
     *
     * @param sourceList       原對象列表，即需要轉換的對象列表。
     * @param destinationClass 目標對象的類，即要轉換成的目標對象類型。
     * @param <S>              原對象類型。
     * @param <D>              目標對象類型。
     * @return 包含轉換後的目標對象的列表。
     */
    public static <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationClass) {
        ObjectMapper mapper = objectMapper.copy();
        // 根據需要配置ObjectMapper，例如忽略未知屬性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁用@JsonProperty注解，對應屬性名稱
        mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        return sourceList.stream()
                .map(source -> mapper.convertValue(source, destinationClass))
                .collect(Collectors.toList());
    }

    /**
     * 將原對象列表轉換為目標對象列表，對應JsonProperty或JsonAlias
     *
     * @param sourceList       原對象列表，即需要轉換的對象列表。
     * @param destinationClass 目標對象的類，即要轉換成的目標對象類型。
     * @param <S>              原對象類型。
     * @param <D>              目標對象類型。
     * @return 包含轉換後的目標對象的列表。
     */
    public static <S, D> List<D> mapListJson(List<S> sourceList, Class<D> destinationClass) {
        ObjectMapper mapper = objectMapper.copy();
        // 根據需要配置ObjectMapper，例如忽略未知屬性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return sourceList.stream()
                .map(source -> mapper.convertValue(source, destinationClass))
                .collect(Collectors.toList());
    }
}

package com.company.inlineeditingfragments.utils;


import io.jmix.flowui.Fragments;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ComponentUtil {

    @Autowired
    private Fragments fragments;

    private final Map<String, FragmentRenderer> globalCache = new ConcurrentHashMap<>();

    // ========== FRAGMENT CACHE MANAGEMENT ==========
    public String generateCacheKey(String columnKey, String itemId) {
        return columnKey + "_" + itemId;
    }

    public <T> FragmentRenderer getOrCreateFragment(
            String columnKey,
            String itemId,
            T item,
            View<?> parentView,
            Class<? extends FragmentRenderer> fragmentClass) {

        String cacheKey = generateCacheKey(columnKey, itemId);
        FragmentRenderer cachedField = globalCache.get(cacheKey);

        if (cachedField != null) {
            return cachedField;
        }

        // Create new fragment
        FragmentRenderer fragment = fragments.create(parentView, fragmentClass);
        fragment.setItem(item);

        globalCache.put(cacheKey, fragment);
        return fragment;
    }

}

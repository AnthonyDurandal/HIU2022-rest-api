package com.hackaton.restapi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Tuple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class Util {

    public static HashMap<String, Object> metaData(Long totalElements, int totalPages, int page, int size) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("totalElements", totalElements);
        res.put("totalPages", totalPages);
        res.put("pageSize", size);
        res.put("pageNumber", page);
        return res;
    }

    public static HashMap<String, Object> links(String sort, int totalPages, int page, int size) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("self", buildUrl(sort, totalPages, page, size));
        res.put("first", buildUrl(sort, totalPages, 1, size));
        res.put("prev", buildUrl(sort, totalPages, page - 1, size));
        res.put("next", buildUrl(sort, totalPages, page + 1, size));
        res.put("last", buildUrl(sort, totalPages, totalPages, size));
        return res;
    }

    private static String buildUrl(String sort, int totalPages, int page, int size) {
        String url = getPrincipalUrl() + "?";
        if (sort != null)
            url += "sort=" + sort + "&";
        url += "page-num=" + page + "&";
        url += "page-size=" + size;
        if (page < 1)
            url = null;
        if (page > totalPages)
            url = null;
        return url;
    }

    public static Pageable pageable(String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (sort != null)
            pageable = PageRequest.of(page, size, strToSort(sort));
        return pageable;
    }

    public static Sort strToSort(String sort) {
        String[] listSort = sort.split(",");
        List<Order> lOrder = new ArrayList<>();
        for (int i = 0; i < listSort.length; i++) {
            int index = listSort[i].lastIndexOf(".");
            String col = listSort[i].substring(0, index);
            String direc = listSort[i].substring(index + 1, listSort[i].length());
            if (direc.compareTo("asc") == 0)
                lOrder.add(Sort.Order.asc(col));
            else if (direc.compareTo("desc") == 0)
                lOrder.add(Sort.Order.desc(col));
        }
        Sort res = Sort.by(lOrder);
        return res;
    }

    public static String getPrincipalUrl() {
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String path = builder.buildAndExpand().getPath();
        String host = builder.buildAndExpand().getHost();
        // String port = String.valueOf(builder.buildAndExpand().getPort());
        return host + path;
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null || value.length() == 0)
            return true;
        return false;
    }

    public static List<HashMap<String, Object>> listTupleToListHashMap(String[] clesString, List<Tuple> listeTuple) {
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        for (Tuple tuple : listeTuple) {
            int x = 0;
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            for (String key : clesString) {
                hashMap.put(key, tuple.get(x));
                x++;
            }
            result.add(hashMap);
        }
        return result;
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}

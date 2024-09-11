package com.project.tripmate.global.commons;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Pagination {

    private int page;
    private int total;
    private int ranges;
    private int limit;

    private int firstRangePage;
    private int lastRangePage;

    private int prevRangePage;
    private int nextRangePage;

    private int totalPages;
    private String baseURL;

    public Pagination (int page , int total ,int ranges , int limit, HttpServletRequest request){
        page = Math.max(page,1);
        total = Math.max(total,0);
        ranges = ranges<1 ? 10 : ranges;
        limit = limit < 1 ? 20 : limit;


        int totalPages = (int)Math.ceil(total / (double)limit);

        int rangeCnt = (page -1 )/ranges;
        int firstRangePage = rangeCnt * ranges +1;
        int lastRangePage = firstRangePage +ranges -1;
        lastRangePage = lastRangePage > totalPages ? totalPages : lastRangePage;


        if(rangeCnt > 0){
            prevRangePage = firstRangePage - ranges;
        }

        int lastRangeCnt = (totalPages - 1) / ranges;

        if (rangeCnt < lastRangeCnt) {
            nextRangePage = firstRangePage +ranges;
        }

        String baseURL = "?";
        if(request != null){
            String queryString = request.getQueryString();
            if (StringUtils.hasText(queryString)) {
                queryString = queryString.replace("?","");
                baseURL += Arrays.stream(queryString.split("&"))
                        .filter(s ->!s.contains("page="))
                        .collect(Collectors.joining("&"));

                baseURL= baseURL.length()> 1? baseURL +="&" :baseURL;
            }
        }


        this.page = page;
        this.total =total;
        this.ranges = ranges;
        this.limit  = limit;
        this.firstRangePage = firstRangePage;
        this.lastRangePage = lastRangePage;
        this.totalPages = totalPages;
        this.baseURL = baseURL;

    }

        public Pagination (int page , int total , int ranges , int limit){
            this(page , total , ranges ,limit , null);
        }

        public List<String[]> getPages(){
        return IntStream.rangeClosed(firstRangePage , lastRangePage)
                .mapToObj( p -> new String[]{
                String.valueOf(p),baseURL+"page="+p})
                .toList();
        }


}

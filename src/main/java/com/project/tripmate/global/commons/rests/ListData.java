package com.project.tripmate.global.commons.rests;


import com.project.tripmate.global.commons.Pagination;
import lombok.Data;

import java.util.List;

@Data
public class ListData<T> {
    private List<T> content;
    private Pagination pagination;

}

package com.project.tripmate.config.commons.rests;


import com.project.tripmate.config.commons.Pagination;
import lombok.Data;

import java.util.List;

@Data
public class ListData<T> {
    private List<T> content;
    private Pagination pagination;

}

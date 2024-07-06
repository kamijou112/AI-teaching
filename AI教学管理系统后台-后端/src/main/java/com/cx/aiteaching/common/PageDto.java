package com.cx.aiteaching.common;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageDto<T> implements Serializable {

    protected List<T> list;
    protected long total;
    protected long size;
    protected long current;
    protected long totalPage;
}

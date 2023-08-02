package com.s360.springjsp.model.alok;

import java.sql.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Report {

    private String accountId;
    private Date startDate;
    private Date endDate;
    private String source;
    private String sourceType;
    private int pagination;
    private int offset;
    private int limit;
}

package ru.yandex.incoming34.pg_diploma.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MetaData {

    public static final String METADATA_FIELD_NAME = "metadata";
    public static final Integer PAGE_SIZE = 50;


    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Long executionTime;
    private final Long totalRecords;

    public MetaData(JSONObject jsonObject, Long totalRecords) {
        this.startTime = LocalDateTime.parse((CharSequence) jsonObject.get("start_time"));
        this.totalRecords = totalRecords;
        this.endTime = LocalDateTime.parse((CharSequence) jsonObject.get("end_time"));;
        this.executionTime = ChronoUnit.MILLIS.between(startTime, endTime);
    }
}

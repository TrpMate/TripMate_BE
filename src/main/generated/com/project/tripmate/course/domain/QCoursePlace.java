package com.project.tripmate.course.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoursePlace is a Querydsl query type for CoursePlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoursePlace extends EntityPathBase<CoursePlace> {

    private static final long serialVersionUID = -1518144173L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoursePlace coursePlace = new QCoursePlace("coursePlace");

    public final StringPath contentTypeId = createString("contentTypeId");

    public final QCourseDay courseDay;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> mapX = createNumber("mapX", Double.class);

    public final NumberPath<Double> mapY = createNumber("mapY", Double.class);

    public final StringPath memo = createString("memo");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath placeName = createString("placeName");

    public final TimePath<java.time.LocalTime> visitEndTime = createTime("visitEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> visitStartTime = createTime("visitStartTime", java.time.LocalTime.class);

    public QCoursePlace(String variable) {
        this(CoursePlace.class, forVariable(variable), INITS);
    }

    public QCoursePlace(Path<? extends CoursePlace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoursePlace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoursePlace(PathMetadata metadata, PathInits inits) {
        this(CoursePlace.class, metadata, inits);
    }

    public QCoursePlace(Class<? extends CoursePlace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.courseDay = inits.isInitialized("courseDay") ? new QCourseDay(forProperty("courseDay"), inits.get("courseDay")) : null;
    }

}


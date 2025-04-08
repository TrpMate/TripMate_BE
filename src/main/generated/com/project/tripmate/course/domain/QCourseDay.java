package com.project.tripmate.course.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseDay is a Querydsl query type for CourseDay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseDay extends EntityPathBase<CourseDay> {

    private static final long serialVersionUID = -765836568L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseDay courseDay = new QCourseDay("courseDay");

    public final QCourse course;

    public final DatePath<java.time.LocalDate> dayDate = createDate("dayDate", java.time.LocalDate.class);

    public final NumberPath<Integer> dayNum = createNumber("dayNum", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCourseDay(String variable) {
        this(CourseDay.class, forVariable(variable), INITS);
    }

    public QCourseDay(Path<? extends CourseDay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseDay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseDay(PathMetadata metadata, PathInits inits) {
        this(CourseDay.class, metadata, inits);
    }

    public QCourseDay(Class<? extends CourseDay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course"), inits.get("course")) : null;
    }

}


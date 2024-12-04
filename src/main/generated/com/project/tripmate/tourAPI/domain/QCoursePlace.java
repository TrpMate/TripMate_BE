package com.project.tripmate.tourAPI.domain;

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

    private static final long serialVersionUID = 2071003824L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoursePlace coursePlace = new QCoursePlace("coursePlace");

    public final StringPath contentId = createString("contentId");

    public final StringPath contentTypeId = createString("contentTypeId");

    public final QCourseDay courseDay;

    public final NumberPath<Integer> courseOrder = createNumber("courseOrder", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> placeTime = createDateTime("placeTime", java.time.LocalDateTime.class);

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


package com.project.tripmate.course.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseUser is a Querydsl query type for CourseUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseUser extends EntityPathBase<CourseUser> {

    private static final long serialVersionUID = 2029393407L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseUser courseUser = new QCourseUser("courseUser");

    public final QCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedDate = createDateTime("joinedDate", java.time.LocalDateTime.class);

    public final com.project.tripmate.user.domain.QUser user;

    public QCourseUser(String variable) {
        this(CourseUser.class, forVariable(variable), INITS);
    }

    public QCourseUser(Path<? extends CourseUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseUser(PathMetadata metadata, PathInits inits) {
        this(CourseUser.class, metadata, inits);
    }

    public QCourseUser(Class<? extends CourseUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course"), inits.get("course")) : null;
        this.user = inits.isInitialized("user") ? new com.project.tripmate.user.domain.QUser(forProperty("user")) : null;
    }

}


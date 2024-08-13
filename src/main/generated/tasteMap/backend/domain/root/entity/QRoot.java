package tasteMap.backend.domain.root.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoot is a Querydsl query type for Root
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoot extends EntityPathBase<Root> {

    private static final long serialVersionUID = -826861816L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoot root = new QRoot("root");

    public final StringPath address = createString("address");

    public final StringPath content = createString("content");

    public final tasteMap.backend.domain.course.entity.QCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QRoot(String variable) {
        this(Root.class, forVariable(variable), INITS);
    }

    public QRoot(Path<? extends Root> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoot(PathMetadata metadata, PathInits inits) {
        this(Root.class, metadata, inits);
    }

    public QRoot(Class<? extends Root> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new tasteMap.backend.domain.course.entity.QCourse(forProperty("course"), inits.get("course")) : null;
    }

}


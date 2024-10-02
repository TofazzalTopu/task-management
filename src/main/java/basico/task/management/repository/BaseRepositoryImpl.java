package basico.task.management.repository;

import com.infobip.spring.data.ExtendedQueryDslJpaRepository;
import com.infobip.spring.data.StoredProcedureQueryBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLTemplatesRegistry;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseRepositoryImpl<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID>  implements BaseRepository<T, ID>, ExtendedQueryDslJpaRepository<T, ID> {

    private final EntityPath<T> path;
    private final JPAQueryFactory jpaQueryFactory;
    private final Supplier<JPASQLQuery<T>> jpaSqlFactory;
    private final EntityManager entityManager;

    BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) throws Exception{
        super(entityInformation, entityManager, SimpleEntityPathResolver.INSTANCE);
        this.jpaQueryFactory = new JPAQueryFactory(HQLTemplates.DEFAULT, entityManager);
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
        SQLTemplates sqlTemplates = getSQLServerTemplates(entityManager.getEntityManagerFactory());
        this.jpaSqlFactory = () -> new JPASQLQuery<>(entityManager, sqlTemplates);
        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }

//    @Override
//    @Transactional
//    public void refresh(T t) {
//
//        entityManager.refresh(t);
//    }
//
//    @Override
//    @Transactional
//    public void detach(T t) {
//        entityManager.detach(t);
//    }

//    @Override
//    public JPQLQuery getDslQuery() {
//        return new JPAQuery(entityManager);
//    }

    @SafeVarargs
    @Override
    public final List<T> save(T... iterable) {
        return saveAll(Arrays.asList(iterable));
    }

    @Override
    public <O> O query(Function<JPAQuery<?>, O> query) {
        return query.apply(jpaQueryFactory.query());
    }

    @Transactional
    @Override
    public void update(Consumer<JPAUpdateClause> update) {

        update.accept(jpaQueryFactory.update(path));
    }

    @Transactional
    @Override
    public long deleteWhere(Predicate predicate) {

        return jpaQueryFactory.delete(path).where(predicate).execute();
    }

    @Override
    public <O> O jpaSqlQuery(Function<JPASQLQuery<T>, O> query) {
        return query.apply(jpaSqlFactory.get());
    }

    @Override
    public SubQueryExpression<T> jpaSqlSubQuery(Function<JPASQLQuery<T>, SubQueryExpression<T>> query) {
        return jpaSqlQuery(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JPQLQuery<T> createQuery(Predicate... predicate) {
        return (JPQLQuery<T>) super.createQuery(predicate);
    }

    @Transactional
    @Override
    public <O> O executeStoredProcedure(String name, Function<StoredProcedureQueryBuilder, O> query) {
        return null;
    }

//    @Override
//    public <O> O executeStoredProcedureV2(String name, Function<basico.task.management.repository.StoredProcedureQueryBuilder, O> query) {
//        return query.apply(new basico.task.management.repository.StoredProcedureQueryBuilder(name, entityManager));
//    }

    private SQLTemplates getSQLServerTemplates(EntityManagerFactory entityManagerFactory) throws SQLException {
        DatabaseMetaData databaseMetaData = getDatabaseMetaData(entityManagerFactory.createEntityManager());
        return new SQLTemplatesRegistry().getTemplates(databaseMetaData);
    }

    private DatabaseMetaData getDatabaseMetaData(EntityManager entityManager) throws SQLException {
        SessionImplementor sessionImplementor = entityManager.unwrap(SessionImplementor.class);
        DatabaseMetaData metaData = sessionImplementor.connection().getMetaData();
        entityManager.close();
        return metaData;
    }
}

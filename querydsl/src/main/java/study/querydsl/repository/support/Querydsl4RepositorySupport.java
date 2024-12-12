package study.querydsl.repository.support;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

@Repository
public abstract class Querydsl4RepositorySupport {

    private final Class domainClass;
    private Querydsl querydsl;
    private EntityManager entityManager;
    private JPAQueryFactory jpaQueryFactory;

    public Querydsl4RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null");
        this.domainClass = domainClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null");

        JpaEntityInformation entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath path = resolver.createPath(entityInformation.getJavaType());
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null");
        Assert.notNull(querydsl, "Querydsl must not be null");
        Assert.notNull(jpaQueryFactory, "JPAQueryFactory must not be null");
    }

    protected JPAQueryFactory getJpaQueryFactory() { return jpaQueryFactory; };
    protected Querydsl getQuerydsl() { return querydsl; }
    protected EntityManager getEntityManager() { return entityManager; }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getJpaQueryFactory().select(expr);
    }
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getJpaQueryFactory().selectFrom(from);
    }

    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery> contentQuery) {
        JPAQuery jpaQuery = contentQuery.apply(getJpaQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaQuery).fetch();
        return PageableExecutionUtils.getPage(content, pageable, jpaQuery::fetchCount);
    }

    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery> contentQuery, Function<JPAQueryFactory, JPAQuery> countQuery) {
        JPAQuery jpaQuery = contentQuery.apply(getJpaQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaQuery).fetch();

        JPAQuery countResult = countQuery.apply(getJpaQueryFactory());
        return PageableExecutionUtils.getPage(content, pageable, countResult::fetchCount);
    }
}

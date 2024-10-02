package basico.task.management.repository;

import com.querydsl.core.types.Path;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoredProcedureQueryBuilder {

    private final String name;
    private final List<Parameter> inParameters;
    private final EntityManager entityManager;
    private Class[] resultClasses;

    StoredProcedureQueryBuilder(String name, EntityManager entityManager) {
        this.name = name;
        this.entityManager = entityManager;
        this.inParameters = new ArrayList();
    }

    public <T> List<T> getResultList() {
        return this.build().getResultList();
    }

    public StoredProcedureQuery build() {
        StoredProcedureQuery storedProcedureQuery = this.createStoredProcedureQuery();
        this.inParameters.forEach((parameter) -> {
            storedProcedureQuery.registerStoredProcedureParameter(parameter.name, parameter.type, ParameterMode.IN);
            storedProcedureQuery.setParameter(parameter.name, parameter.value);
        });
        return storedProcedureQuery;
    }

    private StoredProcedureQuery createStoredProcedureQuery() {
        return this.resultClasses == null ? this.entityManager.createStoredProcedureQuery(this.name) : this.entityManager.createStoredProcedureQuery(this.name, this.resultClasses);
    }

    public <T> StoredProcedureQueryBuilder addInParameter(Path<T> parameter, T value) {
        Class type = parameter.getType();
        this.inParameters.add(new Parameter(type, parameter.getMetadata().getName(), value));
        return this;
    }

    public StoredProcedureQueryBuilder addInParameter(String name, Object value) {
        this.inParameters.add(new Parameter(value.getClass(), name, value));
        return this;
    }

    public StoredProcedureQueryBuilder setResultClasses(Class... resultClasses) {
        this.resultClasses = resultClasses;
        return this;
    }

    private static final class Parameter {
        private final Class type;
        private final String name;
        private final Object value;

        private Parameter(Class type, String name, Object value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                Parameter parameter = (Parameter)o;
                return Objects.equals(this.type, parameter.type) && Objects.equals(this.name, parameter.name) && Objects.equals(this.value, parameter.value);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.type, this.name, this.value});
        }

        public String toString() {
            return "Parameter{type=" + this.type + ", name='" + this.name + '\'' + ", value=" + this.value + '}';
        }
    }
}

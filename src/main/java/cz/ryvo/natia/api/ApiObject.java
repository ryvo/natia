package cz.ryvo.natia.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.ryvo.natia.exception.InternalServerException;
import io.swagger.annotations.ApiModelProperty;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public abstract class ApiObject implements Serializable {

    /**
     * Returns true is all public getters of the object returns null. Otherwise returns false.
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean isEmpty() {
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(this.getClass(), ApiObject.class).getPropertyDescriptors()) {
                Method method = descriptor.getReadMethod();
                if (method == null) {
                    continue;
                }
                if (Modifier.isPublic(method.getModifiers())) {
                    Object value = method.invoke(this);
                    if ((value != null && !isEmptyCollection(value) && !isEmptyMap(value))) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new InternalServerException(e);
        }
    }

    private boolean isEmptyCollection(Object o) {
        return (o instanceof Collection) && ((Collection) o).isEmpty();
    }

    private boolean isEmptyMap(Object o) {
        return (o instanceof Map) && ((Map) o).isEmpty();
    }
}

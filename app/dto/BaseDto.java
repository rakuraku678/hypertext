package dto;

import java.lang.reflect.Field;

abstract class BaseDto implements Dto{

    public boolean set(String fieldName, Object fieldValue) {
        Class<?> clazz = getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object castValue = field.getType().cast(fieldValue);
                field.set(this, castValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }
}

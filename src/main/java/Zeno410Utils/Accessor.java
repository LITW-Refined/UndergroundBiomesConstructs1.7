package Zeno410Utils;

import java.lang.reflect.Field;

/**
 *
 * @author Zeno410
 */
public class Accessor<ObjectType, FieldType> {

    private Field field;
    private final Class FieldTypeVar;

    public Accessor(Class _FieldType) {
        FieldTypeVar = _FieldType;
    }

    private Field field(ObjectType example) {
        Class classObject = example.getClass();
        if (field == null) {
            try {
                setField(classObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return field;
    }

    private void setField(Class classObject) throws IllegalAccessException {
        // hunts through the class object and all superclasses looking for the field name
        Field[] fields;
        do {
            fields = classObject.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if ((FieldTypeVar.equals(fields[i].getType()))
                    || (FieldTypeVar.isAssignableFrom(fields[i].getClass()))) {
                    field = fields[i];
                    field.setAccessible(true);
                    return;
                }
            }
            classObject = classObject.getSuperclass();
        } while (classObject != Object.class);
        throw new RuntimeException(FieldTypeVar.getName() + " not found in class " + classObject.getName());
    }

    public FieldType get(ObjectType object) {
        try {
            return (FieldType) (field(object).get(object));
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setField(ObjectType object, FieldType fieldValue) {
        try {
            field(object).set(object, fieldValue);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}

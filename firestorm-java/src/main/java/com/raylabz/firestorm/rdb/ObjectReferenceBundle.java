package com.raylabz.firestorm.rdb;

import com.google.firebase.database.DatabaseReference;

public final class ObjectReferenceBundle {

    private final Object object;
    private final DatabaseReference reference;

    public ObjectReferenceBundle(Object object, DatabaseReference reference) {
        this.object = object;
        this.reference = reference;
    }

    public Object getObject() {
        return object;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectReferenceBundle that = (ObjectReferenceBundle) o;

        if (!object.equals(that.object)) return false;
        return (object.equals(that.object) && reference.equals(that.reference));
    }

    @Override
    public int hashCode() {
        int result = object.hashCode();
        result = 31 * result + reference.hashCode();
        return result;
    }

}

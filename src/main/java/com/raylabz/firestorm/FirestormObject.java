package com.raylabz.firestorm;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class FirestormObject {

    @Exclude private String documentID;
    @Exclude private ArrayList<ListenerRegistration> listeners = new ArrayList<>();

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public ArrayList<ListenerRegistration> getListeners() {
        return listeners;
    }

    public void addListener(ListenerRegistration listenerRegistration) {
        listeners.add(listenerRegistration);
    }

    public void removeListener(ListenerRegistration listenerRegistration) {
        listeners.remove(listenerRegistration);
    }

}

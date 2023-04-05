package com.raylabz.firestorm;

import com.google.cloud.firestore.*;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements logic for Firestore update events on filterables.
 * @author Nicos Kasenides
 * @version 1.3.2
 */
public class FilterableListener<T> implements EventListener<QuerySnapshot> {

    /**
     * The listener is listening for changes to this filterable item.
     */
    private final FirestormFilterable<T> filterable;

    /**
     * A callback to execute when an update is received.
     */
    private final RealtimeUpdateCallback<List<ObjectChange<T>>> callback;

    /**
     * Creates a FilterableListener.
     * @param filterable The filterable that this listener will be attached to.
     */
    public FilterableListener(FirestormFilterable<T> filterable, RealtimeUpdateCallback<List<ObjectChange<T>>> callback) {
        this.filterable = filterable;
        this.callback = callback;
    }

    /**
     * Executes when an update to an object is made.
     * @param querySnapshot The query snapshot retrieved upon update.
     * @param e An exception thrown by Firestore if the data retrieval was not successful.
     */
    @Override
    public final void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirestoreException e) {
        if (e != null) {
            callback.onError(e);
            return;
        }

        if (querySnapshot != null) {
            List<DocumentChange> documentChanges = querySnapshot.getDocumentChanges();
            ArrayList<ObjectChange<T>> objectChanges = new ArrayList<>();
            for (DocumentChange documentChange : documentChanges) {
                QueryDocumentSnapshot document = documentChange.getDocument();
                ObjectChange<T> objectChange = new ObjectChange<T>(document.toObject(filterable.objectClass), document, documentChange.getOldIndex(), documentChange.getNewIndex(), ObjectChange.Type.fromDocumentChangeType(documentChange.getType()));
                objectChanges.add(objectChange);
            }
            callback.onUpdate(objectChanges);

        }
        else {
            callback.onError(new RuntimeException("Failed to retrieve update to collection."));
        }
    }

    /**
     * Retrieves the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public FirestormFilterable<T> getFilterable() {
        return filterable;
    }

}

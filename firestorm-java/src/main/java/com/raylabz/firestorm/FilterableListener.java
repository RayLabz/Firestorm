package com.raylabz.firestorm;

import com.google.cloud.firestore.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements logic for Firestore update events on filterables.
 * @author Nicos Kasenides
 * @version 1.3.2
 */
public abstract class FilterableListener<T> implements EventListener<QuerySnapshot> {

    /**
     * The listener is listening for changes to this filterable item.
     */
    private final FirestormFilterable<T> filterable;

    /**
     * Creates a FilterableListener.
     * @param filterable The filterable that this listener will be attached to.
     */
    public FilterableListener(final FirestormFilterable<T> filterable) {
        this.filterable = filterable;
    }

    /**
     * Executes when an update to an object is made.
     * @param querySnapshot The query snapshot retrieved upon update.
     * @param e An exception thrown by Firestore if the data retrieval was not successful.
     */
    @Override
    public final void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirestoreException e) {
        if (e != null) {
            onFailure(e.getMessage());
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
            onSuccess(objectChanges);

        }
        else {
            onFailure("Failed to retrieve update to collection.");
        }
    }

    /**
     * Retrieves the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public FirestormFilterable<T> getFilterable() {
        return filterable;
    }

    /**
     * Implements logic upon success of data update delivery.
     * @param objectChanges A list of the update objectChanges.
     */
    public abstract void onSuccess(List<ObjectChange<T>> objectChanges);

    /**
     * Implements logic upon failure of data update delivery.
     * @param failureMessage The message of the failure.
     */
    public abstract void onFailure(final String failureMessage);

}

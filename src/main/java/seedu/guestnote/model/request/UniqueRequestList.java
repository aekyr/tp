package seedu.guestnote.model.request;

import static java.util.Objects.requireNonNull;
import static seedu.guestnote.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.guestnote.model.request.exceptions.DuplicateRequestException;
import seedu.guestnote.model.request.exceptions.RequestNotFoundException;

/**
 * A list of requests that enforces uniqueness between its elements and does not allow nulls.
 * A request is considered unique by comparing using {@code Request#isSameRequest(Request)}.
 * Supports a minimal set of list operations.
 *
 * @see Request#isSameRequest(Request)
 */
public class UniqueRequestList implements Iterable<Request> {

    private final ObservableList<Request> internalList = FXCollections.observableArrayList();
    private final ObservableList<Request> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent request as the given argument.
     */
    public boolean contains(Request toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRequest);
    }

    /**
     * Adds a request to the list.
     * The request must not already exist in the list.
     */
    public void add(Request toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRequestException(toAdd.toString());
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the request {@code target} in the list with {@code editedRequest}.
     * {@code target} must exist in the list.
     * The request identity of {@code editedRequest} must not be the same as another existing request in the list.
     */
    public void setRequest(Request target, Request editedRequest) {
        requireAllNonNull(target, editedRequest);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RequestNotFoundException(target.toString());
        }

        if (!target.isSameRequest(editedRequest) && contains(editedRequest)) {
            throw new DuplicateRequestException(editedRequest.toString());
        }

        internalList.set(index, editedRequest);
    }

    /**
     * Removes the equivalent request from the list.
     * The request must exist in the list.
     */
    public void remove(Request toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RequestNotFoundException(toRemove.toString());
        }
    }

    public void setRequests(UniqueRequestList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code requests}.
     * {@code requests} must not contain duplicate requests.
     */
    public void setRequests(List<Request> requests) {
        requireAllNonNull(requests);
        if (!requestsAreUnique(requests)) {
            throw new DuplicateRequestException(requests.toString());
        }

        internalList.setAll(requests);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Request> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Request> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueRequestList)) {
            return false;
        }

        UniqueRequestList otherUniqueRequestList = (UniqueRequestList) other;
        return internalList.equals(otherUniqueRequestList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code requests} contains only unique requests.
     */
    private boolean requestsAreUnique(List<Request> requests) {
        for (int i = 0; i < requests.size() - 1; i++) {
            for (int j = i + 1; j < requests.size(); j++) {
                if (requests.get(i).isSameRequest(requests.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if the list is empty.
     */
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    /**
     * Adds all the requests in the list to the current list.
     */
    public void addAll(List<Request> requests) {
        for (Request request : requests) {
            add(request);
        }
    }

    /**
     * Removes all the requests in the list from the current list.
     */
    public void removeAll(List<Request> requests) {
        for (Request request : requests) {
            remove(request);
        }
    }
}

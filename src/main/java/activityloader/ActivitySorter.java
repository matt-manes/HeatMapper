package activityloader;

import models.Activity;

import java.util.ArrayList;

// Sort activities from earliest to latest using heap sort
public class ActivitySorter {

    /**
     * Returns `activities` sorted by date in ascending order.
     *
     * @param activities The activities to sort.
     * @return The sorted activities.
     */
    public ArrayList<Activity> sort(ArrayList<Activity> activities) {
        heap.clear();
        for (Activity activity : activities) {
            add(activity);
        }
        ArrayList<Activity> sorted = new ArrayList<>();
        while (!heap.isEmpty()) {
            sorted.add(pop());
        }
        return sorted;
    }

    private final ArrayList<Activity> heap = new ArrayList<>();

    private int getParentdex(int index) {
        return (index - 1) / 2;
    }

    private int getLeftdex(int index) {
        return (index * 2) + 1;
    }

    private int getRightdex(int index) {
        return (index * 2) + 2;
    }

    private boolean hasChildren(int index) {
        return getLeftdex(index) < heap.size() || getRightdex(index) < heap.size();
    }

    /**
     * Returns the child index of activity at `index` that has the earlier date.
     * If there's only one child, its index will be returned.
     * Whether the node at `index` has children should be checked before calling this function.
     *
     * @param index The parent index.
     * @return The index of the child node with the earlier activity date.
     */
    private int getEarlierChildex(int index) {
        int leftdex = getLeftdex(index);
        int rightdex = getRightdex(index);
        int earlierChildex;
        if (leftdex >= heap.size()) {
            earlierChildex = rightdex;
        } else if (rightdex >= heap.size()) {
            earlierChildex = leftdex;
        } else {
            earlierChildex = isBefore(leftdex, rightdex) ? leftdex : rightdex;
        }
        return earlierChildex;
    }

    /**
     * Add the activity to the heap and swim.
     *
     * @param activity The activity to add to the heap.
     */
    private void add(Activity activity) {
        heap.add(activity);
        swim();
    }

    /**
     * Pop an activity from the heap, move the bottom node to the top, and sink.
     *
     * @return The activity that currently has the earliest date.
     */
    private Activity pop() {
        if (heap.isEmpty()) return null;
        Activity activity = heap.removeFirst();
        if (heap.size() <= 1) return activity;
        // If two or more nodes, move bottom to top
        heap.addFirst(heap.removeLast());
        sink();
        return activity;
    }

    /**
     * @param index1 A node index.
     * @param index2 A node index.
     * @return Whether activity at `index1` is after that at `index2`.
     */
    private boolean isAfter(int index1, int index2) {
        return heap.get(index1).date().after(heap.get(index2).date());
    }

    /**
     * @param index1 A node index.
     * @param index2 A node index.
     * @return Whether activity at `index1` is before that at `index2`.
     */
    private boolean isBefore(int index1, int index2) {
        return !isAfter(index1, index2);
    }

    /**
     * Swim the bottom node up the heap until order in satisfied.
     */
    private void swim() {
        int index = heap.size() - 1;
        while (index > 0) {
            int parentdex = getParentdex(index);
            if (isAfter(parentdex, index)) {
                swap(index, parentdex);
                index = parentdex;
                continue;
            }
            break;
        }

    }

    /**
     * Sink the top node down the heap until order is satisfied.
     */
    private void sink() {
        int index = 0;
        while (index < heap.size() - 1) {
            if (!hasChildren(index)) break;

            int earlierChildex = getEarlierChildex(index);
            if (isBefore(earlierChildex, index)) {
                swap(index, earlierChildex);
                index = earlierChildex;
                continue;
            }
            break;
        }
    }

    /**
     * Swap the order of two nodes.
     *
     * @param index1 A node index.
     * @param index2 A node index.
     */
    private void swap(int index1, int index2) {
        Activity temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }
}

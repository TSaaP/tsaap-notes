package org.tsaap;

import java.util.*;

/**
 * @author Franck Silvestre
 */
public class CustomPagedResultList implements List {

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    private List list = new ArrayList();
    private Integer totalCount;

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }


    public boolean add(Object o) {
        return list.add(o);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection c) {
        return list.addAll(index, c);
    }

    public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public Object get(int index) {
        return list.get(index);
    }

    public Object set(int index, Object element) {
        return list.set(index, element);
    }

    public void add(int index, Object element) {
        list.add(index, element);
    }

    public Object remove(int index) {
        return list.remove(index);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return list.listIterator();
    }

    public ListIterator listIterator(int index) {
        return list.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}

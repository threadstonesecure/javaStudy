package dlt.study.guava.collection;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * 需要处理所有Add的情况
 * @param <E>
 */
public class AddLoggingList<E>  extends ForwardingList<E> {


    public AddLoggingList(List<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<E> delegate() {
        return delegate;
    }


    @Override
    public void add(int index, E element) {
        System.out.println("add -> " + index + " : "+ element);
        super.add(index, element);
    }

    @Override
    public boolean add(E element) {
        return standardAdd(element);
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        return standardAddAll(elements);
    }


    @Override
    public boolean addAll(int index, Collection<? extends E> elements) {
        return standardAddAll(index,elements);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return standardListIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return standardListIterator();
    }

    private List<E> delegate;

    public static void main(String[] args) {
        List<String> list =  new AddLoggingList<>(Lists.newArrayList());
        list.add("denglt");
        list.add("zyy");
        list.addAll(Lists.newArrayList("dzy","dwx"));
        list.addAll(4,Lists.newArrayList("A","B"));

        ListIterator listIterator = list.listIterator();
        listIterator.add("first");

        System.out.println("=================");
        list.forEach(System.out::println);

    }
}

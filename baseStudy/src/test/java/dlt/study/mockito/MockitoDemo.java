package dlt.study.mockito;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoDemo {

    /**
     * In reality, please don't mock the List class. Use a real instance instead.
     */
    @Test
    public void mockList() {

        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.clear();
        System.out.println(mockedList.get(0));

        verify(mockedList).add("one");
        verify(mockedList).clear();

        when(mockedList.get(0)).thenReturn("one");
        System.out.println(mockedList.get(0));
        verify(mockedList, times(2)).get(0);
        reset(mockedList);
    }

    @Test
    public void mackList2() {

        //You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        //doThrow(new RuntimeException()).when(mockedList).get(1);

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
        System.out.println(mockedList.get(1));

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList).get(0);
        verify(mockedList, never()).isEmpty();
        verify(mockedList, times(0)).get(1);
    }

    /**
     * 当方法 return void时, when()将无法使用，
     * 可以使用doReturn()|doThrow()| doAnswer()|doNothing()|doCallRealMethod() family of methods
     */

    @Test
    public void getStubber() {
        LinkedList mockedList = mock(LinkedList.class);
        //when(mockedList.clear()).thenThrow(new RuntimeException()); is error
        doThrow(new RuntimeException()).when(mockedList).clear();
        // doReturn()
        // doAnswer()
        // doNothing()
        mockedList.add("one");
        doCallRealMethod().when(mockedList).size();
        System.out.println(mockedList.size());

        mockedList.clear();
    }

    /**
     * Mockito 提供的 spy 方法可以包装一个真实的 Java 对象, 并返回一个包装后的新对象.
     * 若没有特别配置的话, 对这个新对象的所有方法调用, 都会委派给实际的 Java 对象
     */
    @Test
    public void spyObject() {
        List list = new LinkedList();
        List spy = spy(list);
        // 对 spy.size() 进行定制.
        when(spy.size()).thenReturn(100);

        spy.add("one");
        spy.add("two");

        // 因为我们没有对 get(0), get(1) 方法进行定制,
        // 因此这些调用其实是调用的真实对象的方法.
        Assert.assertEquals(spy.get(0), "one");
        Assert.assertEquals(spy.get(1), "two");

        Assert.assertEquals(spy.size(), 100);

        //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
        when(spy.get(0)).thenReturn("foo");
        Assert.assertEquals(spy.get(0), "foo");

        //You have to use doReturn() for stubbing
        doReturn("foo").when(spy).get(0);
        Assert.assertEquals(spy.get(0), "foo");
    }

    @Test
    public void deepStubs() {
        LinkedList mockedList = mock(LinkedList.class);
        Iterator mockIterator = mock(Iterator.class);
        when(mockedList.iterator()).thenReturn(mockIterator);
        when(mockIterator.next()).thenReturn("one", "tow", "three");
        System.out.println(mockedList.iterator().next());
        System.out.println(mockedList.iterator().next());
        System.out.println(mockedList.iterator().next());

        // is equivalent of

        mockedList = mock(LinkedList.class, RETURNS_DEEP_STUBS);
        when(mockedList.iterator().next()).thenReturn("one", "tow", "three");
        System.out.println(mockedList.iterator().next());
        System.out.println(mockedList.iterator().next());
        System.out.println(mockedList.iterator().next());
    }


    @Test
    public void answer() {
        List mockedList = mock(List.class);
        when(mockedList.get(anyInt())).thenAnswer(m -> {
            Object[] args = m.getArguments();
            Object mock = m.getMock();
            return "called with arguments: " + args;
        });

        System.out.println(mockedList.get(1));
    }

}

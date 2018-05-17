package dlt.study.mockito;

import com.beust.jcommander.internal.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class ArgumentDemo {



    @Test
    public void matcher() {
        List<String> mockedList = mock(List.class,withSettings().serializable());

        anyString();// 这儿是通过ThreadSafeMockingProgress中的ThreadLocal<MockingProgress>上下文实现的,
        // MockingProgress.ArgumentMatcherStorage(ArgumentMatcherStorageImpl)实现中维护了一个Stack<LocalizedMatcher>
        when(mockedList.contains("")).thenReturn(true);
        when(mockedList.add(anyString())).thenReturn(true);
        when(mockedList.get(anyInt())).thenReturn("hello denglt!");
        Assert.assertTrue(mockedList.contains("denglt"));
        Assert.assertTrue(mockedList.add("denglt"));
        System.out.println(mockedList.get(100));

        //any(); anyObject();
        when(mockedList.contains(argThat(Any.ANY))).thenReturn(true);
        Assert.assertTrue(mockedList.contains(1));

        mockedList.get(1000);
        verify(mockedList, times(2)).get(anyInt());

        verify(mockedList).add(argThat(someString -> someString.length() > 5));
        // verify(mockedList).add(argThat(someString -> someString.length() > 10));
    }

    @Test
    public void threadSafeMockingProgress() {
        List<String> mockedList = mock(List.class);
        // anyString();
        anyInt();
        when(mockedList.contains("")).thenReturn(true);
        System.out.println(mockedList.contains(1));
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().pullLocalizedMatchers().forEach(t -> System.out.println(t.getMatcher()));
    }

    @Test
    public void captureArgument(){
        List<String> list = Lists.newArrayList("1", "2", "3");
        List mockedList = mock(List.class);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        mockedList.addAll(list);
        verify(mockedList).addAll(argument.capture());

        Assert.assertEquals(3, argument.getValue().size());
        Assert.assertEquals(list, argument.getValue());

        ArgumentCaptor<String> argument2 = ArgumentCaptor.forClass(String.class);
        mockedList.add("4");
        mockedList.add("5");

        verify(mockedList,times(2)).add(argument2.capture());
        System.out.println(argument2.getValue());  // 5

    }


}

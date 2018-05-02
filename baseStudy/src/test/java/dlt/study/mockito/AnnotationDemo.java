package dlt.study.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

// @Captor, @Spy, @InjectMocks
public class AnnotationDemo {
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    List mockedList;
    @Test
    public  void annotation(){
        // MockitoAnnotations.initMocks(this);
        System.out.println(mockedList);
    }
}

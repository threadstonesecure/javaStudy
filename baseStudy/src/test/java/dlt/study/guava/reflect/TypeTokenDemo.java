package dlt.study.guava.reflect;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;

public class TypeTokenDemo {

    @Test
    public void test() {
        ArrayList<String> stringList = Lists.newArrayList();
        ArrayList<Integer> intList = Lists.newArrayList();
        System.out.println(stringList.getClass().isAssignableFrom(intList.getClass()));
        // returns true, even though ArrayList<String> is not assignable from ArrayList<Integer>
        System.out.println(stringList.getClass());
        System.out.println(intList.getClass());

        System.out.println(stringList.getClass().getGenericSuperclass());
        System.out.println(intList.getClass().getGenericSuperclass());

        Type superclass = stringList.getClass();
        if (superclass instanceof ParameterizedType) {
            Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            System.out.println(type);
        } else {
            System.out.println(superclass + " not instanceof ParameterizedType");
        }
        superclass = stringList.getClass().getGenericSuperclass();//getSuperclass();
        if (superclass instanceof ParameterizedType) {
            System.out.println(superclass + " instanceof ParameterizedType");
            ParameterizedType type = ((ParameterizedType) superclass);
            System.out.println(type.getActualTypeArguments()[0]);
            System.out.println(type.getRawType());
            System.out.println(type.getOwnerType());
        } else {
            System.out.println(superclass + " not instanceof ParameterizedType");
        }

        System.out.println("===========");
        System.out.println(List.class.getTypeParameters());
    }


    @Test
    public void tet() {
        System.out.println(TypeToken.of(String.class));
        Type type = String.class;
        System.out.println(TypeToken.of(type));
        TypeToken<String> of = TypeToken.of(String.class);
        System.out.println(of.getType());
        System.out.println(of.getRawType());

        TypeToken<List<String>> stringListTok = new TypeToken<List<String>>() {
        };
        System.out.println(stringListTok.getType());
        System.out.println(stringListTok.getRawType());


    }

    @Test
    public void typeToken() throws Exception {
        TypeToken<String> stringTok = TypeToken.of(String.class);
        System.out.println(stringTok);
        TypeToken<Integer> intTok = TypeToken.of(Integer.class);

        TypeToken<List<String>> stringListTok = new TypeToken<List<String>>() {
        }; // <T>
        System.out.println(stringListTok);

        ArrayList<String> arrayList = Lists.newArrayList();
        System.out.println(TypeToken.of(arrayList.getClass()));

        TypeToken<Map<?, ?>> wildMapTok = new TypeToken<Map<?, ?>>() {
        };
        TypeToken<Map<String, BigInteger>> mapToken = mapToken(
                TypeToken.of(String.class),
                TypeToken.of(BigInteger.class));
        TypeToken<Map<Integer, Queue<String>>> complexToken = mapToken(
                TypeToken.of(Integer.class),
                new TypeToken<Queue<String>>() {
                });

        ArrayList<String> stringList = Lists.newArrayList();
        TypeToken of = TypeToken.of(stringList.getClass());
        System.out.println(of);

    }

    static <K, V> TypeToken<Map<K, V>> mapToken(TypeToken<K> keyToken, TypeToken<V> valueToken) {
        return new TypeToken<Map<K, V>>() {
        }
                .where(new TypeParameter<K>() {
                }, keyToken)
                .where(new TypeParameter<V>() {
                }, valueToken);
    }


    static <K, V> TypeToken<Map<K, V>> incorrectMapToken() {
        return new TypeToken<Map<K, V>>() {
        };
    }


    @Test
    public void testIncorrectMapToken() {
        System.out.println(TypeTokenDemo.<String, BigInteger>incorrectMapToken());
        // just prints out "java.util.Map<K, V>"
    }
}

package dlt.study.guava;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;


/**
 * Primitive Type	Guava Utilities (all in com.google.common.primitives)
 * byte	    Bytes, SignedBytes, UnsignedBytes
 * short	    Shorts
 * int	    Ints, UnsignedInteger, UnsignedInts
 * long	    Longs, UnsignedLong, UnsignedLongs
 * float	    Floats
 * double	    Doubles
 * char	    Chars
 * boolean	Booleans
 */
public class PrimitivesDemo {

    @Test
    public void ints() {
        System.out.println(Ints.tryParse("344ttt"));
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("denglt", 0);
        multiset.add("denglt", Ints.tryParse("344ttt"));
        System.out.println(multiset.size());

        Ints.checkedCast(1000);
        Ints.saturatedCast(1000);
        //Ints.join()
        //Ints.lexicographicalComparator()
        //  Ints.asList()

        // Ints.toArray()

        //   Ints.toByteArray()  public static byte[] toByteArray(int value)
        // Ints.fromByteArray
    }

    /**
     * like Arrays.asList
     */
    @Test
    public void asList() {
        // Chars.asList()
        // Booleans.asList()
    }

    /**
     * like Collection.toArray()
     */
    @Test
    public void toArray() {
        //Ints.toArray()
        //Chars.toArray()
    }

    /**
     * like Iterables.concat()
     */
    @Test
    public void concat(){
        //Ints.concat()

    }
}

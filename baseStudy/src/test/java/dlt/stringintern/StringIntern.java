package dlt.stringintern;

public class StringIntern {

	public static void main(String[] args) throws Exception {
        String cmd="code";
        String a = "code";
        String b = "code";
        System.out.println(a==b);

        a = new String("code");
        b = new String("code");
        System.out.println(a==b);
        
        a = a.intern();
        b = "code";
        System.out.println(a ==b );
	}

}

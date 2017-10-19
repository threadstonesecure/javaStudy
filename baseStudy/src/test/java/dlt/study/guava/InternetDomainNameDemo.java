package dlt.study.guava;

import com.google.common.net.InternetDomainName;
import org.junit.Test;

public class InternetDomainNameDemo {


    @Test
    public  void domainName(){
        InternetDomainName owner = InternetDomainName.from("mail.google.com");
        System.out.println(owner.topPrivateDomain());
        System.out.println(owner.child("denglt"));
        System.out.println(owner.parent());
        System.out.println(owner.publicSuffix());
        owner.parts().forEach(System.out::println);

        System.out.println(InternetDomainName.isValid("denglt.ddd"));
    }
}

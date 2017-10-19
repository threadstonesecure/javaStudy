package dlt.study.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by denglt on 16/9/19.
 */
@ContextConfiguration(locations={"/springtest/spring-context.xml"})
@TransactionConfiguration(defaultRollback=false)
public class TransactionJUnit4Spring extends AbstractTransactionalJUnit4SpringContextTests {

}

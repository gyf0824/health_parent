import com.itheima.utils.SMSUtils;
import org.junit.Test;


public class SMStest {

    //@Test
    public void test01() throws Exception{
        SMSUtils.sendShortMessage("SMS_179225778","17805969842","1234");
    }

    //@Test
    public void test02() throws Exception{
        SMSUtils.sendShortMessage("SMS_179220796","17805969842","2019年12月8日");
    }
}

import com.tensquare.sms.rabbit.RabbitApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) //RunWith就是一个运行器，SpringRunner.class就是在spring的容器下运行
@SpringBootTest(classes= RabbitApplication.class)  //这个springBootTest需要指定一个程序的入口
//如果没有spring配置文件，那么就使用类来启动 在没有配置文件的时候，那么需要使用springBootTest来进行测试
public class Product {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //在点对点发布的时候不需要使用转换器
    @Test
    public void sendMsg1(){
        rabbitTemplate.convertAndSend("itcast","直接模式测试");
    }

    //在广播模式下是不需要指定路由键的
    @Test
    public void sendMsg2(){
        rabbitTemplate.convertAndSend("chuanzhi","","广播模式");
    }

    //根据主题进行发送
    @Test
    public void sendMsg3(){
        rabbitTemplate.convertAndSend("topic84","good.fdsa.fsaf.fsf.fdsa.log","log.#的主题模式");
    }
}

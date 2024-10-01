package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력")
    public void findAllBean(){
        String[] beanDefinitionNames=ac.getBeanDefinitionNames();
        for(String beanDefinitionName:beanDefinitionNames){
            Object object=ac.getBean(beanDefinitionName);
            System.out.println("name="+beanDefinitionName+", object:"+object);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력")
    public void findApplicationBean(){
        String[] beanDefinitionNames=ac.getBeanDefinitionNames();
        for(String beanDefinitionName:beanDefinitionNames){
            BeanDefinition object=ac.getBeanDefinition(beanDefinitionName);
            if(object.getRole()==BeanDefinition.ROLE_APPLICATION) {
                System.out.println("name=" + beanDefinitionName + ", object:" + object);
            }
        }
    }
}

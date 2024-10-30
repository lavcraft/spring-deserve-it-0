package spring.deserve.it.game;

import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

public class ConditionalOnOnlySpider implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Получаем текущее имя игрока из метаданных аннотации @PlayerQualifier
       String playerName= (String) metadata.getAnnotations().get(PlayerQualifier.class).getValue("value").get();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        // Проверяем, есть ли уже другие пауки с таким же квалифаером
        return Arrays.stream(beanFactory.getBeanDefinitionNames())
                .map(beanFactory::getBeanDefinition)
                .filter(beanDefinition -> beanDefinition.getBeanClassName()!=null)
                .filter(beanDefinition -> !beanDefinition.getBeanClassName().equals(metadata.toString()))
                .noneMatch(beanDefinition -> hasSamePlayerQualifier(beanDefinition, playerName));
    }

    @SneakyThrows
    private boolean hasSamePlayerQualifier(BeanDefinition beanDefinition, String playerName) {
        // Проверяем наличие аннотации @PlayerQualifier
        String beanClassName = beanDefinition.getBeanClassName();
        if(beanClassName==null) return false;
        PlayerQualifier annotation = Class.forName(beanClassName).getAnnotation(PlayerQualifier.class);


        // Пропускаем бины без аннотации @PlayerQualifier
        if (annotation == null) {
            return false;
        }

        // Сравниваем квалифаер с текущим именем игрока
        System.out.println(" &^&^&^&^&^ "+beanDefinition.getBeanClassName()+ " "+annotation.value()+ " =? "+playerName );
        return annotation.value().equals(playerName);
    }
}
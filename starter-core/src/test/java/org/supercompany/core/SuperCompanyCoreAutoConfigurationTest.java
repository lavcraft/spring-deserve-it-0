package org.supercompany.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SuperCompanyCoreAutoConfigurationTest {
    ApplicationContextRunner contextRunner = new ApplicationContextRunner();


    public static class UserConfigurations {
        @Bean
        public String myVersion(String superCompanyCoreVersion) {
            return new String(superCompanyCoreVersion + ".RELEASE");
        }
    }

    @Test
    void should_work_with_company_version_based_on_starter_version() {
        contextRunner
                .withConfiguration(AutoConfigurations.of(SuperCompanyCoreAutoConfiguration.class))
                .withUserConfiguration(UserConfigurations.class)
                .run(ctx -> {
                    assertThat(ctx).hasBean("myVersion");
                    assertThat(ctx).getBean("myVersion").isEqualTo("1.0.0.RELEASE");
                });
    }

    @Test
    void should_work_with_company_version_based_on_starter_version_on_configurations_with_string_beans() {
        contextRunner
                .withConfiguration(AutoConfigurations.of(SuperCompanyCoreAutoConfiguration.class))
                .withUserConfiguration(UserConfigurations.class)
                .withBean("superDuperVersion", String.class, () -> "1.0.1.RELEASE")
                .run(ctx -> {
                    assertThat(ctx).hasBean("myVersion");
                    assertThat(ctx).getBean("myVersion").isEqualTo("1.0.0.RELEASE");
                });
    }

    @Test
    void should_not_work_when_context_exist_version_with_similiar_name() {
        assertThatThrownBy(() -> {
            contextRunner
                    .withConfiguration(AutoConfigurations.of(SuperCompanyCoreAutoConfiguration.class))
                    .withUserConfiguration(UserConfigurations.class)
                    .withBean("superCompanyCoreVersion", String.class, () -> "1.0.1.RELEASE")
                    .run(ctx -> {
                        assertThat(ctx).hasBean("myVersion");
                        assertThat(ctx).getBean("myVersion").isEqualTo("1.0.0.RELEASE");
                    });
        });
    }

    @Test
    void should_has_custom_version_bean() {
        contextRunner
                .withConfiguration(AutoConfigurations.of(SuperCompanyCoreAutoConfiguration.class))
                .withUserConfiguration(UserConfigurations.class)
                .withPropertyValues("debug=true")
                .run(ctx -> {
                    assertThat(ctx).hasSingleBean(CustomVersion.class);
                });
    }

    @Test
    void should_no_custom_version_bean() {
        contextRunner
                .withConfiguration(AutoConfigurations.of(SuperCompanyCoreAutoConfiguration.class))
                .withUserConfiguration(UserConfigurations.class)
                .withPropertyValues("debug=false")
                .run(ctx -> {
                    assertThat(ctx).doesNotHaveBean(CustomVersion.class);
                });
    }
}
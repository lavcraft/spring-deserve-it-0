package spring.deserve.it.infra;

public interface ProxyConfigurator {
    <T> T wrapWithProxy(T obj, Class<T> implClass);
}
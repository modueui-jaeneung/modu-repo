package com.modu.authorizationServer.converter;

public interface ProviderUserConverter<T, R> {
    R converter(T t);
}

package com.github.discovery126.greenimpact.utils;

import java.util.Objects;
import java.util.function.Consumer;

public class UpdateUtils {

    private UpdateUtils() {
        throw new UnsupportedOperationException("Этот класс не предназначен для инстанцирования.");
    }

    public static <T> void updateFieldIfChanged(T oldValue, T newValue, Consumer<T> setter) {
        if (!Objects.equals(oldValue, newValue)) {
            setter.accept(newValue);
        }
    }
}

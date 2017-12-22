
package org.rapharino.common.utils;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created By Rapharino on 2017/11/15 下午6:17
 */
public abstract class Either {

    public static <T> IF<T> either(T value, Predicate<? super T> predicate) {
        return new IF<>(Optional.of(value).filter(predicate));
    }

    static private class IF<T> {

        private Optional<T> filter;

        public IF(Optional<T> filter) {
            this.filter = filter;
        }

        public T or(T other) {
            return filter.orElse(other);
        }
    }

    public static void main(String[] args) {
        System.out.println(Either.either(-1, obj -> obj > 0).or(100));
    }

}

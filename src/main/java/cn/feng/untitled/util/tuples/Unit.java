package cn.feng.untitled.util.tuples;

import cn.feng.untitled.util.tuples.immutable.ImmutableUnit;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author cedo
 * @since 05/24/2022
 */
public abstract class Unit<A> implements Serializable {

    public static <A> Unit<A> of(A a) {
        return ImmutableUnit.of(a);
    }

    public abstract A get();

    public abstract <R> R apply(Function<? super A, ? extends R> func);

    public abstract void use(Consumer<? super A> func);

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that instanceof Unit<?>) {
            final Unit<?> other = (Unit<?>) that;
            return Objects.equals(get(), other.get());
        }
        return false;
    }

}

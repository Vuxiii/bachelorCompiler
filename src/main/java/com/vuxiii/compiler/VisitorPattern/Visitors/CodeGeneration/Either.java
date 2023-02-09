package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Optional;


public class Either<L, R> {
    private Optional<L> left;
    private Optional<R> right;

    private EitherOrientation orientation;

    public static<L, R> Either<L, R> left( L left ) {
        Either<L, R> ei = new Either<>();
        ei.left = Optional.of(left);
        ei.right = Optional.empty();
        ei.orientation = EitherOrientation.LEFT;
        return ei;
    }

    public static<L, R> Either<L, R> right( R right ) {
        Either<L, R> ei = new Either<>();
        ei.left = Optional.empty();
        ei.right = Optional.of(right);
        ei.orientation = EitherOrientation.RIGHT;
        return ei;
    }

    public EitherOrientation orientation() {
        return orientation;
    }

    public L left() {
        return left.get();
    }

    public R right() {
        return right.get();
    }
}

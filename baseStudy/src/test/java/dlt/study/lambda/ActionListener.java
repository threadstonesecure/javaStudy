package dlt.study.lambda;


@FunctionalInterface
public interface ActionListener<T> {

    void actionPerformed(T e);

    boolean equals(Object other);

    default ActionListener<T> and(ActionListener<? super T> after) {
        return (e) -> {
            this.actionPerformed(e);
            after.actionPerformed(e);
        };
    }
}

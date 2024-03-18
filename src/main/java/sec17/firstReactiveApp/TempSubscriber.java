package sec17.firstReactiveApp;

import java.util.concurrent.Flow.*;

public class TempSubscriber implements Subscriber<TempInfo>{

    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription; // 구독을 저장하고
        subscription.request(1); // 첫 번째 요청을 전달
    }

    @Override
    public void onNext(TempInfo item) {
        System.out.println(item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }
}

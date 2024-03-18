package sec17.firstReactiveApp;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class TempProcessor implements Processor<TempInfo, TempInfo> { // TempInfo를 다른 TempInfo로 변환하는 프로세서
    private Subscriber<? super TempInfo> subscriber;

    @Override
    public void subscribe(Subscriber<? super TempInfo> subscriber) {
        this.subscriber = subscriber;
    }
    @Override
    public void onNext(TempInfo item) {
        subscriber.onNext(new TempInfo(item.getTown(),
                (item.getTemp()-32) * 5/9)); // 섭씨로 변환한 다음 TempInfo를 다시 전송
    }


    @Override
    public void onSubscribe(Subscription subscription) { // 다른 모든 신호는 업스트림 구독자에게 전달.
        subscriber.onSubscribe(subscription);
    }



    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}

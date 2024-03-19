package sec17.firstReactiveApp;


import io.reactivex.Observable;
import java.util.Arrays;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //getTemperatures("New York").subscribe(new TempSubscriber());
        //getCelsiusTemperatures("New York").subscribe(new TempSubscriber());

        //Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS);
        //onePerSec.blockingSubscribe(i-> System.out.println(TempInfo.fetch("New York")));

        //Observable<TempInfo> observable = getTemperatures("New York"); // 매 초마다 뉴욕의 온도 보고 방출하는 Observable 만들기

//        Observable<TempInfo> observable = getNegativeTemperatures("New York");
//        observable.blockingSubscribe(new TempObserver()); // 단순 Observer로 이 Observable에 가입해서 온도 출력

        Observable<TempInfo> observable = getCelsiusTemperatures(
                "New Youk", "Chicago", "San Francisco");
        observable.blockingSubscribe(new TempObserver());
    }

//    private static Publisher<TempInfo> getTemperatures(String town){
//        // 구독한 Subscriber에게 TempSubscription을 전송하는 Publisher 반환
//        return subscriber -> subscriber.onSubscribe(
//                new TempSubscription(subscriber, town)
//        );
//    }
    private static Observable<TempInfo> getTemperatures(String town){
        return Observable.create(emitter -> // Observer를 소비하는 함수로부터 Observable 만들기
                Observable.interval(1, TimeUnit.SECONDS) // 매 초마다 무한으로 증가하는 일련의 long 값을 방출하는 Observable
                        .subscribe(i-> {
                            if(!emitter.isDisposed()) { // 소비된 옵저버가 아직 폐기되지 않았으면 어떤 작업을 수행(이전 에러)
                                if (i >= 5) { // 온도를 5번 보고했으면 옵저버를 완료하고 스트림을 종료
                                    emitter.onComplete();
                                } else {
                                    try {
                                        emitter.onNext(TempInfo.fetch(town)); // 아니면 온도를 Observer로 보고
                                    } catch (Exception e) {
                                        emitter.onError(e); // 에러가 발생하면 Observer에 알림.
                                    }
                                }
                            }})
        );
    }
    public static Observable<TempInfo> getCelsiusTemperatures(String town){
        return getTemperatures(town)
                .map(temp -> new TempInfo(temp.getTown(),
                        (temp.getTemp() - 32) * 5/9));
    }

//    private static Publisher<TempInfo> getCelsiusTemperatures(String town){
//        return subscriber -> {
//            TempProcessor processor = new TempProcessor(); // TempProcessor를 만들고 Subscriber와 반환된 Publisher사이로 연결
//            processor.subscribe(subscriber);
//            processor.onSubscribe(new TempSubscription(processor, town));
//        };
//    }

    public static Observable<TempInfo> getNegativeTemperatures(String town){
        return getCelsiusTemperatures(town)
                .filter(i-> i.getTemp()<=0);
    }

    public static Observable<TempInfo> getCelsiusTemperatures(String... towns){
        return Observable.merge(Arrays.stream(towns)
                .map(i-> getCelsiusTemperatures(i))
                .collect(Collectors.toList())
        );
    }

}

package sec17.firstReactiveApp;


import java.util.concurrent.Flow.Publisher;

public class Main {
    public static void main(String[] args) {
        //getTemperatures("New York").subscribe(new TempSubscriber());
        getCelsiusTemperatures("New York").subscribe(new TempSubscriber());
    }

    private static Publisher<TempInfo> getTemperatures(String town){
        // 구독한 Subscriber에게 TempSubscription을 전송하는 Publisher 반환
        return subscriber -> subscriber.onSubscribe(
                new TempSubscription(subscriber, town)
        );
    }

    private static Publisher<TempInfo> getCelsiusTemperatures(String town){
        return subscriber -> {
            TempProcessor processor = new TempProcessor(); // TempProcessor를 만들고 Subscriber와 반환된 Publisher사이로 연결
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));
        };
    }
}

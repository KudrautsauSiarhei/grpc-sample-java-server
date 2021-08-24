package org.example;

import com.example.grpc.GreetingServiceGrpc;
import com.example.grpc.GreetingServiceOuterClass;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
    @Override
    public void greeting(
            final GreetingServiceOuterClass.HelloRequest request,
            final StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver
    ) {
        final GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
                .setGreeting("Hello from server, " + request.getName())
                .build();

        responseObserver.onNext(response);
        System.out.println(response);
        responseObserver.onCompleted();
    }

    @Override
    public void greetings(
            final GreetingServiceOuterClass.HelloRequest request,
            final StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver
    ) {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
                    .setGreeting("Hello from server, " + request.getName() + " " + i)
                    .build();

            responseObserver.onNext(response);
            System.out.println(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void endlessGreetings(GreetingServiceOuterClass.HelloRequest request, StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
        final ServerCallStreamObserver<GreetingServiceOuterClass.HelloResponse> serverCallStreamObserver =
                ((ServerCallStreamObserver<GreetingServiceOuterClass.HelloResponse>) responseObserver);

        int i = 0;
        while (!serverCallStreamObserver.isCancelled()) {
            i++;
            saveSleep(500);

            final GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
                    .setGreeting("Hello from server, " + request.getName() + " " + i)
                    .build();

            responseObserver.onNext(response);
            System.out.println(response);
        }
    }

    private void saveSleep(long microtime) {
        try {
            Thread.sleep(microtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

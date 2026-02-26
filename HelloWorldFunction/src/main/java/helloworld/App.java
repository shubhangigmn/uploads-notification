package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class App implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event event, Context context) {

        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();

        String message = "File uploaded to bucket: " + bucket + " , File name: " + key;

        SnsClient snsClient = SnsClient.create();

        PublishRequest request = PublishRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:249563935244:uploads-notification-topic")
                .message(message)
                .subject("New S3 File Uploaded")
                .build();

        snsClient.publish(request);

        return message;
    }
}
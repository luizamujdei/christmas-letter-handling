import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.node.TextNode;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SendChristmasLetterTest {

    public static final String DUMMY = "dummy";
    private static final String LOCALSTACK_URL = "http://localhost:4566";
    private static final String LOCALSTACK_ARN = "arn:aws:sqs:us-east-1:000000000000";
    private static final String REGION = "us-east-1";

    private SnsClient snsClient;
    private SqsClient sqsClient;
    private String topicArn;
    private String queueURL;

    @BeforeAll
    void setup(){
        initialiseSnsClient();

        initialiseSqsClient();

        // Creating the SNS Topic
        CreateTopicResponse topicResponse = snsClient.createTopic(CreateTopicRequest.builder().name("TestTopic").build());
        topicArn = topicResponse.topicArn();

        // Creating the SQS Queue
        CreateQueueResponse queueResponse = sqsClient.createQueue(CreateQueueRequest.builder().queueName("TestQueue").build());
        queueURL = queueResponse.queueUrl();

        //Subscribing the SQS to the SNS
        snsClient.subscribe(SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol("sqs")
                .endpoint(LOCALSTACK_ARN + ":TestQueue")
                .build());
    }

    void initialiseSnsClient(){
        snsClient = SnsClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_URL))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(DUMMY, DUMMY)))
                .region(Region.of(REGION))
                .build();
    }

    void initialiseSqsClient(){
        sqsClient = SqsClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_URL))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(DUMMY, DUMMY)))
                .region(Region.of(REGION))
                .build();
    }

    @Test
    public void whenSendChristmasLetter_ThenMessageReceivedOnQueue() throws IOException {
        //Publish message to the SNS Topic
        String testMessage = "{\n" +
                "    \"email\": \"luiza.mujdei@gmail.com\",\n" +
                "    \"name\": \"Luiza\",\n" +
                "    \"wishes\": \"fimo\",\n" +
                "    \"location\": \"Iasi\"\n" +
                "}";

        snsClient.publish(PublishRequest.builder().topicArn(topicArn).message(testMessage).build());

        //Polling the SQS queue
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueURL)
                .maxNumberOfMessages(1)
                .waitTimeSeconds(5)
                .build());

        //Assert the received message is the one we sent
        List<Message> messageList = receiveMessageResponse.messages();
        assertFalse(messageList.isEmpty(), "No messages received from the SQS queue");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(messageList.get(0).body());
        JsonNode messageNode = objectMapper.readTree( rootNode.get("Message").asText());

        assertEquals("luiza.mujdei@gmail.com", messageNode.get("email").asText() , "The email in the message received is not the expected one");
        assertEquals("Luiza", messageNode.get("name").asText() , "The name in the message received is not the expected one");
        assertEquals("fimo", messageNode.get("wishes").asText(), "The wishes in the message received are not the expected ones");
        assertEquals("Iasi",messageNode.get("location").asText(),  "The location in the message received is not the expected one");
    }

    @Test
    public void whenSendChristmasLetter_ToNonExistentTopic_thenExceptionIsThrown() {
        //Publish message to the SNS Topic
        String testMessage = "{\n" +
                "    \"email\": \"luiza.mujdei@gmail.com\",\n" +
                "    \"name\": \"Luiza\",\n" +
                "    \"wishes\": \"fimo\",\n" +
                "    \"location\": \"Iasi\"\n" +
                "}";

        SnsException exception = assertThrows(SnsException.class, ()
                -> snsClient.publish(PublishRequest.builder().topicArn("arn:aws:sqs:us-east-1:000002000000:invalidTopic")
                .message(testMessage).build()));

        assertTrue(exception.getMessage().contains("Topic does not exist"));
    }

    @AfterAll
    void tearDown(){
        snsClient.deleteTopic(DeleteTopicRequest.builder().topicArn(topicArn).build());
        sqsClient.deleteQueue(DeleteQueueRequest.builder().queueUrl(queueURL).build());
    }
}

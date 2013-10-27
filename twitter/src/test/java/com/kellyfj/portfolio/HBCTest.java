package com.kellyfj.portfolio;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import scala.actors.threadpool.Arrays;

public class HBCTest {

	public static void oauth(String consumerKey, String consumerSecret,
			String token, String secret) throws InterruptedException {
		// Create an appropriately sized blocking queue
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);

		// Define our endpoint: By default, delimited=length is set (we need
		// this for our processor)
		// and stall warnings are on.
		StatusesSampleEndpoint endpoint = new StatusesSampleEndpoint();
		endpoint.stallWarnings(false);

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token,
				secret);
		// Authentication auth = new
		// com.twitter.hbc.httpclient.auth.BasicAuth(username, password);

		// Create a new BasicClient. By default gzip is enabled.
		BasicClient client = new ClientBuilder().name("sampleExampleClient")
				.hosts(Constants.STREAM_HOST).endpoint(endpoint)
				.authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		// Do whatever needs to be done with messages
		for (int msgRead = 0; msgRead < 1000; msgRead++) {
			if (client.isDone()) {
				System.out.println("Client connection closed unexpectedly: "
						+ client.getExitEvent().getMessage());
				break;
			}

			String msg = queue.poll(5, TimeUnit.SECONDS);
			if (msg != null) {
				printMessage(msg);
			} else {
				System.out.println("Did not receive a message in 5 seconds");
			}
		}

		client.stop();

		// Print some stats
		System.out.printf("The client read %d messages!\n", client
				.getStatsTracker().getNumMessages());
	}

	@SuppressWarnings(value = { "unchecked" })
	private static void printMessage(String msg) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> data = mapper.readValue(msg.getBytes(), Map.class);
			if (!data.containsKey("delete")) {
				//System.out.println(msg);

				String language = (String) data.get("lang");
				// String createdAt = (String) data.get("created_at");
				String text = (String) data.get("text");
				Map<String, Object> user = (Map<String, Object>) data.get("user");
				String userID = (String) user.get("screen_name");
				
				//if(data.containsKey("entities"))
				//	System.out.println("Entities present" + data.get("entities"));
								
				if (language.startsWith("en")) {
					System.out.println(userID + "\t" + language + "\t" + text);	
				}
				if(data.get("place")!=null){
					//System.out.println("Place present" + data.get("place"));
					Map<String,Object> placeData = (Map<String, Object>) data.get("place");
					String placeType = (String) placeData.get("place_type");
					String placeName = (String) placeData.get("name");
					String country = (String) placeData.get("country");
					System.out.println("Place Details:" + placeType + "\t" + placeName + "\t" + country);	
				}
				if(data.get("entities")!=null) {
					Map<String,Object> entityData = (Map<String, Object>) data.get("entities");
					Object hashtags = data.get("hashtags");
					if (hashtags != null) {
						String[] tags = (String[]) hashtags;
						System.out.println("HashTags: "+Arrays.toString(tags));
					}
				}
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {

			String consumerKey = "R2srtfc5kEIxuQfiyG9pEA";
			String consumerSecret = "0id5kQT8ZSkQuNRBDhIlKOrE3sKvPDGAfmgIVVZPI";
			String accessToken = "312942836-Zki7Zhlrpjhp5N0XRU9vW6INtgZ6PeAeJQzzkoXM";
			String accessTokenSecret = "RxpmR4XnvpDRd3gH1AXgIzniAI57AzscUiXyQqmUnLv0T";

			HBCTest.oauth(consumerKey, consumerSecret, accessToken,
					accessTokenSecret);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
}
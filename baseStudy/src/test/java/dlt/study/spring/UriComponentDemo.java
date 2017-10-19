package dlt.study.spring;

import java.net.URI;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class UriComponentDemo {

	public static void main(String[] args) {
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(
		        "http://example.com/hotels/{hotel}/bookings/{booking}").build();

		URI uri = uriComponents.expand("42", "邓隆通").encode().toUri();
		System.out.println(uri.toString());
		System.out.println("ok");
	}


}

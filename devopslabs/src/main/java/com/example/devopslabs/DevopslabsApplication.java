package com.example.devopslabs;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevopslabsApplication {

	// Since this method is there only as a bridge to Spring's run, I annotated the method with @lombok.Generated and now sonar ignores it when calculating the test coverage.
	@Generated
	public static void main(String[] args) {
		SpringApplication.run(DevopslabsApplication.class, args);
	}

}

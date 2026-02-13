package com.TaskmanagementSystem.Security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
@Configuration
public class CloudinaryConfig {
	
	@Value("${cloudinary.cloud_name}")
	private String cloudName;
	@Value("${cloudinary.api_key}")
	private String apiKey;
	@Value("${cloudinary.api_Secret}")
	private String apiSecret;

	
	
	@Bean
	public Cloudinary cloudinary() {
		
		Map<String,Object> config = new HashMap<>();
		config.put("cloud_Name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_Secreat", apiSecret);
		return new Cloudinary(config);
	}
	

}
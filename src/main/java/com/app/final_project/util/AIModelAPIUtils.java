package com.app.final_project.util;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AIModelAPIUtils {

    private static final String AI_MODEL_API = "https://finalprojectdut.pythonanywhere.com/";

    public static Boolean isModelExist(Integer userId) {
        try {
            String url = AI_MODEL_API + "api/check_model/" + userId;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                System.out.println(responseBody);
                return jsonNode.get("exists").asBoolean();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isTraining(Integer userId) {
        try {
            String url = AI_MODEL_API + "api/isTraining/" + userId;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                System.out.println(responseBody);
                return jsonNode.get("isTraining").asBoolean();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean trainingModel(List<String> imageUrls, Integer userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = AI_MODEL_API + "api/train_model";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Tạo JSON body
            String jsonBody = createJsonBody(imageUrls, userId);
            // Tạo request
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Training model API call successful. message: " + response.getBody().toString());
            } else {
                System.out.println("Training model API call failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Boolean predict(String imageUrl, Integer userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = AI_MODEL_API + "api/predict";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Tạo JSON body
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("url", imageUrl);
            jsonMap.put("userId", userId.toString());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(jsonMap);

            // Tạo request
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("predict API call successful. message: " + response.getBody().toString());
                String responseBody = response.getBody();

                ObjectMapper obj = new ObjectMapper();
                JsonNode jsonNode = obj.readTree(responseBody);
                return jsonNode.get("predicted").asBoolean();
            } else {
                System.out.println("predict API call failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String createJsonBody(List<String> imageUrls, Integer userId) {
        // Khởi tạo một đối tượng Map để chứa các thông tin
        Map<String, Object> jsonMap = new HashMap<>();

        // Thêm danh sách URL vào Map
        jsonMap.put("urls", imageUrls);

        // Thêm userId vào Map
        jsonMap.put("userId", userId.toString()); // Chuyển Integer thành String

        // Khởi tạo một ObjectMapper từ thư viện Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Chuyển đổi Map thành JSON string bằng ObjectMapper
            return objectMapper.writeValueAsString(jsonMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

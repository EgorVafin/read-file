package org.example.app.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.config.AuthProvider;
import org.example.entity.User;
import org.example.lib.NotFoundException;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OAuthVKController {
    private final UserRepository userRepository;
    public static final String VkOauthUrl = "https://oauth.vk.com/authorize?client_id=51774023&display=page&redirect_uri=http://localhost:8080/oauth/vk&scope=email&response_type=code&v=5.131";
    private final AuthProvider authProvider;

    @SneakyThrows
    @GetMapping("/oauth/vk")
    public String process(
            HttpServletRequest servletRequest,
            @RequestParam String code,
            Principal principal) {
        String url = "https://oauth.vk.com/access_token?client_id=51774023&client_secret=VF4859PVUlohduyNTSOU&redirect_uri=http://localhost:8080/oauth/vk&code=" + code;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        String responseBodyStr = response.body();

        ObjectMapper objectMapper = new ObjectMapper();
        VKOAuthResponse vkResponse = objectMapper.readValue(responseBodyStr, new TypeReference<VKOAuthResponse>() {
        });

        Long vkUserId = vkResponse.getUser_id();

        // если уже локально залогинены, то записываем VKId в поле к этому пользователю
        if (principal != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            User user = (User) token.getPrincipal();
            user.setVkUserId(vkUserId);
            userRepository.save(user);

        } else {
            Optional<User> user = userRepository.findByVkUserIdIs(vkUserId);
            if (user.isPresent()) {
                authProvider.login(servletRequest, user.get());
                return "redirect:/";

            } else {
                return "redirect:/login";
            }
        }


// 2 варианта 1 - есть залогиненый пользователь, привязать ВК
        //2 - заход через ВК. Ищем по ВКid, если есть то аутентифицируем
        //искусственно входим
        // если нет то идем на страницу логин

        return "redirect:/";
    }
}

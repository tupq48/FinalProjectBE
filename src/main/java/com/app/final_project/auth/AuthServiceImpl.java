package com.app.final_project.auth;

import com.app.final_project.enums.RoleType;
import com.app.final_project.security.jwt.JwtService;
import com.app.final_project.user.User;
import com.app.final_project.user.UserRepository;
import com.app.final_project.userInfor.UserInfor;
import com.app.final_project.userInfor.UserInforRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.DuplicateFormatFlagsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserInforRepository userInforRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${application.security.jwt.refresh-token.expiration-day}")
    private long refreshExpirationDay;

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        String dateTimeStr;
        User newUser = new User();
        UserInfor newDetailInfor = new UserInfor();
        newDetailInfor.setFullname(request.getName().trim().toLowerCase());
        newDetailInfor.setGender(request.getGender());
        newDetailInfor.setAddress(request.getAddress());
        if(request.getDateOfBirth() != null) {
            dateTimeStr = request.getDateOfBirth().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            newDetailInfor.setDateOfBirth(dateTime);
        }
        newDetailInfor.setUser(newUser);

        newUser.setUsername(request.getUsername().trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        newUser.setEmail(request.getGmail().trim().toLowerCase());
        newUser.setRole(RoleType.USER);
        newUser.setPhoneNumber(request.getPhoneNumber().trim().toLowerCase());
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new DuplicateFormatFlagsException("Username already exists: " + request.getUsername());
        }

        newUser = userRepository.save(newUser);
        newDetailInfor = userInforRepository.save(newDetailInfor);

            return getAuthResponse(newUser);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername().toLowerCase().trim(),
                            request.getPassword()
                    )
            );
//        } catch (BadCredentialsException ex) {
//            log.error(ex.toString());
//            throw new InvalidUserException("Invalid username or password");
//        } catch (AuthenticationException ex) {
//            log.error(ex.toString());
//            throw new LockedOrDisableUserException(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("lỗi ở đăng nhập");
        }

        User user = (User) authentication.getPrincipal();
        return getAuthResponse(user);
    }
//
//
//    /**
//     * Làm mới mã thông báo truy cập (access token) bằng mã thông báo làm mới (refresh token).
//     *
//     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu HTTP.
//     * @param response Đối tượng HttpServletResponse để trả về phản hồi.
//     * @throws IOException                  Nếu xảy ra lỗi khi ghi phản hồi vào luồng đầu ra.
//     * @throws UsernameNotFoundException    Nếu không tìm thấy tên người dùng trong hệ thống.
//     * @throws LockedOrDisableUserException Nếu người dùng bị khóa bởi quản trị viên hoặc bị vô hiệu hóa.
//     */
//    @Override
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
//            return;
//        }
//        final String refreshToken = authHeader.substring(7);
//        User user = getUserByToken(refreshToken);
//
//        if (user == null) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
//            return;
//        }
//
//        if (user.isLocked()) {
//            throw new LockedOrDisableUserException("User is locked by admin : " + user.getUsername());
//        }
//
//        String accessToken = jwtService.generateToken(user);
//        AuthResponse authResponse = AuthResponse.builder()
//                .id(user.getId())
//                .fullName(user.getFullName())
//                .username(user.getUsername())
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//    }
//
//
//    private User getUserByToken(String refreshToken) {
//        TokenRefresh tokenRefresh = tokenRefreshRepository.findByToken(refreshToken);
//        if (tokenRefresh != null &&
//                tokenRefresh.getExpirationDate().isAfter(LocalDateTime.now())) {
//            tokenRefresh.setExpirationDate(LocalDateTime.now().plusDays(refreshExpirationDay));
//            tokenRefreshRepository.save(tokenRefresh);
//            return tokenRefresh.getUser();
//        }
//        return null;
//    }
//
    private AuthResponse getAuthResponse(User user) {
//
        String accessToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .id(user.getUser_id())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();
    }
}
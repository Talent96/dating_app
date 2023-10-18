package africa.semicolon.promeescuous.controllers;


import africa.semicolon.promeescuous.dtos.requests.MediaReactionRequest;
import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.requests.UpdateUserRequest;
import africa.semicolon.promeescuous.dtos.requests.UploadMediaRequest;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.exceptions.UserNotFoundException;
import africa.semicolon.promeescuous.services.UserService;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest registerUserRequest){
        RegisterUserResponse response = userService.register(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id) throws UserNotFoundException {
        GetUserResponse user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUserProfile(@ModelAttribute UpdateUserRequest updateUserRequest, @PathVariable Long id) throws JsonPatchException {
        UpdateUserResponse response = userService.updateProfile(updateUserRequest, id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/uploadMedia")
    public ResponseEntity<UploadMediaResponse> uploadMedia(@ModelAttribute UploadMediaRequest mediaRequest){
        MultipartFile mediaToUpload = mediaRequest.getMedia();
        UploadMediaResponse response = userService.uploadMedia(mediaToUpload);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<UploadMediaResponse> uploadProfilePicture(@ModelAttribute UploadMediaRequest mediaRequest){
        MultipartFile mediaToUpload = mediaRequest.getMedia();
        UploadMediaResponse response = userService.uploadProfilePicture(mediaToUpload);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/react/{id}")
    public ResponseEntity<?> reactToMedia(@RequestBody MediaReactionRequest mediaReactionRequest){
        ApiResponse<?> response = userService.reactToMedia(mediaReactionRequest);
        return ResponseEntity.ok(response);
    }

}

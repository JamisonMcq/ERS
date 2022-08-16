package project2.reviewapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.reviewapp.models.User;
import project2.reviewapp.repos.UserRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    @Value("${server.port}")
    int serverPort;

    private UserRepository userRepository;
    private List<User> users;//is this necessary?

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.users = new ArrayList<>();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(){
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody User newUser) throws URISyntaxException{
        users.add(newUser);
        userRepository.save(newUser);
        return ResponseEntity.created(new URI("http://localhost:"+serverPort+"/reviewapp/users/"+newUser.getId())).build();
    }

    //TODO: delete user



    @GetMapping(path="{id}")
    public ResponseEntity getById(@PathVariable("id") int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path="s/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getByEmail(@RequestParam(required = false) String email){
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path="{id}")
    public ResponseEntity updateUserInfo(@PathVariable("id") int id, @RequestBody User userDetails){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setFirstname(userDetails.getFirstname());
            user.get().setLastname(userDetails.getLastname());
            user.get().setEmail(userDetails.getEmail());
            userRepository.updateUserInfo(userDetails.getFirstname(), userDetails.getLastname(), userDetails.getEmail(), id);
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path="{id}/reset")
    public ResponseEntity resetPassword(@PathVariable("id")int id, @RequestBody String password){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setPassword(password);
            userRepository.updateUserPassword(password, id);
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }










}

package com.example.WhatsappAPI;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class WhatsappController {

    WhatsappService whatsappService = new WhatsappService();

    @PostMapping("/add-user")
    public String createUser( String name, String mobile) throws Exception {
        return whatsappService.createUser(name, mobile);
    }

    @PostMapping("/add-group")
    public Group createGroup( List<User> users) {
        return whatsappService.createGroup(users);
    }

    @PostMapping("/add-message")
    public int createMessage(String content) {
        return whatsappService.createMessage(content);
    }

    @PutMapping("/send-message")
    public int sendMessage(Message message,User sender, Group group) throws  Exception{
        return whatsappService.sendMessage(message, sender, group);
    }

    @PutMapping("/change-admin")
    public String changeAdmin(User approver, User user ,Group group) throws Exception{
        return whatsappService.changeAdmin(approver, user, group);
    }

    @DeleteMapping("/delete-user")
    public int removeUser(User user) throws  Exception{
        return whatsappService.removeUser(user);
    }

    @GetMapping("/find-message")
    public String findMessage(Date start, Date end, int K) throws Exception{
        return whatsappService.findMessage(start, end, K);
    }

    @DeleteMapping("/delete-group")
    public String deleteGroup(Group group,User user) throws Exception {
        return whatsappService.deleteGroup(group, user);
    }

    @DeleteMapping("/Delete-Multiple-Message")
    public String deleteMultipleMessages(Date start, Date end){
        return whatsappService.deleteMultipleMessages(start, end);
    }


}

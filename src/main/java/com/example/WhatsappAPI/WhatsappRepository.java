package com.example.WhatsappAPI;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.*;
@Repository
public class WhatsappRepository {

    private Map<Group, List<User>>  groupUserMap;
    private Map<Group,List<Message>> groupMessageMap;
    private HashMap<Message,User> senderMap;
    private HashMap<Group,User> adminMap;
    private HashSet<String> userMobile;

    private int customGroupCount;
    private int messageId;

    public WhatsappRepository() {
        this.groupUserMap = new HashMap<>();
        this.groupMessageMap = new HashMap<>();
        this.senderMap = new HashMap<>();
        this.adminMap = new HashMap<>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String name, String mobile) throws Exception{
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }

        User user = new User(name,mobile);
        userMobile.add(mobile);
        return "User created successfully";
    }

    public Group createGroup(List<User> users){
        String groupName = "";
        if(users.size() > 2){
            this.customGroupCount++;
            groupName = "Group "+this.customGroupCount;
        }else{
            groupName = users.get(1).getName();   // if group size is 2, then group name will be as 2nd user name.
        }

        Group group = new Group(groupName,users.size());
        groupUserMap.put(group,users);
        adminMap.put(group,users.get(0));
        groupMessageMap.put(group,new ArrayList<Message>());

        return group;
    }

    public int createMessage(String content){
        // the 'i^th' created message has message id 'id'
        // Return the message id

        this.messageId++;
        Message message = new Message(messageId,content);
        return message.getId();
    }

    public int sendMessage(Message message,User sender, Group group) throws  Exception{
        if(groupUserMap.containsKey(group)==false){
            throw new Exception("Invalid Group ( group Does not exist)");
        }

        // checking that sender is the member of group or not !
        boolean isMemberOfGroup = false;
        List<User> userListInAGroup = groupUserMap.get(group);
        for(User user : userListInAGroup){
            if(user.equals(sender)){
                isMemberOfGroup = true;
                break;
            }
        }

        if(isMemberOfGroup == false) {
            throw new Exception("you are not allow to send message in group!");
        }

        List<Message>  messageList = new ArrayList<>();

        if(groupMessageMap.containsKey(group)){
            messageList = groupMessageMap.get(group);
        }

        messageList.add(message);
        groupMessageMap.put(group,messageList);

        return messageList.size();     // showing how many message in the group ( messageList )
    }

    public String changeAdmin(User approver, User user ,Group group) throws Exception{
        if(groupUserMap.containsKey(group) == false){
            throw new Exception("Group doesn't exist");
        }
        if(adminMap.get(group).equals(approver) == false){
            throw new Exception("Not valid approver");
        }

        List<User> usersList = groupUserMap.get(group);

        boolean isMemberOfGroup = false;
        for(User user1 : usersList){
            if(user1.equals(user)){
                isMemberOfGroup = true;
                break;
            }
        }

        if(isMemberOfGroup == false){
            throw new Exception("User is not a member of group");
        }

        adminMap.put(group,user);

        return "SUCCESS";
    }

    // Remove User

    public int removeUser(User user) throws  Exception{
        boolean userFound = false;
        Group userGroup = null;
        for(Group group : groupUserMap.keySet()){
            if(groupUserMap.get(group).contains(user)){
                userGroup = group;
                if(adminMap.get(userGroup).equals(user)){
                    throw new Exception("Cannot remove admin");
                }
                userFound = true;
                break;
            }
        }

        if(userFound==false){
            throw new Exception("User not found");
        }

        List<User> userList = groupUserMap.get(userGroup);
        List<User> updatedUserList = new ArrayList<>();

        for(User user1 : userList){
            if(user1.equals(user)){
                continue;
            }
            updatedUserList.add(user1);
        }
        groupUserMap.put(userGroup, updatedUserList);

        List<Message> messageList = groupMessageMap.get(userGroup);
        List<Message> updatedMessageList = new ArrayList<>();

        for(Message message : messageList){
            if(senderMap.get(message).equals(user)){
                continue;
            }
            updatedMessageList.add(message);
        }
        groupMessageMap.put(userGroup, updatedMessageList);

        HashMap<Message, User> updatedSenderMap = new HashMap<>();
        for(Message message : senderMap.keySet()){
            if(senderMap.get(message).equals(user)){
                continue;
            }
            updatedSenderMap.put(message, senderMap.get(message));
        }

        senderMap = updatedSenderMap;

        return updatedUserList.size() + updatedMessageList.size() + updatedSenderMap.size();
    }

    public String findMessage(Date start, Date end, int K) throws Exception{

        List<Message> messageList = new ArrayList<>();
        for(Group group : groupUserMap.keySet()){
            messageList.addAll(groupMessageMap.get(group));
        }

        List<Message> filteredMessageList = new ArrayList<>();
        for(Message message : messageList){
            if(message.getTimeStamp().after(start) && message.getTimeStamp().before(end)){
                filteredMessageList.add(message);
            }
        }

        if(filteredMessageList.size()< K){
            throw new Exception("K is greater than the number of messages");
        }

        Collections.sort(filteredMessageList, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m2.getTimeStamp().compareTo(m1.getTimeStamp());
            }
        });

        return filteredMessageList.get(K-1).getContent();
    }

    public String deleteGroup(Group group,User user) throws Exception{
        if(groupUserMap.containsKey(group)==false) throw new Exception("Group Not Found, Denied!");

        //check if User is admin or not .if user is not admin the they can not delete group
        if(adminMap.get(group).equals(user) == false){
            throw new Exception("Member can not Delete, Only Admin can !");
        }

        groupUserMap.remove(group);
        groupMessageMap.remove(group);
        adminMap.remove(group);
        return "Group deleted successfully";
    }

    public String deleteMultipleMessages(Date start, Date end){
        List<Message> messageList = new ArrayList<>();
        for(Group group : groupUserMap.keySet()){
            messageList.addAll(groupMessageMap.get(group));
        }

        List<Message> updatedList = new ArrayList<>();
        for(Message message : messageList){
            if(message.getTimeStamp().after(start) && message.getTimeStamp().before(end)){
                continue;
            }
            updatedList.add(message);
        }

        for(Group group : groupUserMap.keySet()){
            List<Message> list = groupMessageMap.get(group);
            List<Message> newList = new ArrayList<>();
            for(Message message : list){
                if(!updatedList.contains(message)){
                    continue;
                }
                newList.add(message);
            }
            groupMessageMap.put(group, newList);
        }

        return "Message Deleted Successfully";
    }


}
